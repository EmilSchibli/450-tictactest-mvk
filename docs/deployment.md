# Deployment

OpenTofu baut die leere Maschine und die Website. Welche Version läuft, steht nicht in OpenTofu. Ein neues Release braucht kein `tofu apply` und keine neue Instanz.

Das IaC-Projekt lag nicht in diesem Repository. `infra/` enthält deshalb die Instanz aus dem IaC-Auftrag und die Erweiterung für die Web-Konsole.

## Warum das Jar nicht in user_data steht

`user_data` läuft beim ersten Start. Liegt das Jar dort, bleibt eine neue Version liegen, bis die Instanz neu entsteht. `user_data_replace_on_change = true` würde die Instanz bei jeder Versionsänderung zerstören. Die Adresse und die laufende Partie hängen dann am Release. Das ist die Kopplung, die dieser Auftrag trennt.

Die Instanz liest `s3://<artifact-bucket>/current`. Darin steht nur `X.Y.Z`. Jar und `tictactest-webconsole-X.Y.Z.zip` liegen unter `releases/X.Y.Z/`. Der Dienst holt genau diese Dateien, bevor er startet. Nach einem Neustart ist `current` noch dieselbe Datei, also startet dieselbe Version.

GitHub direkt von der Instanz wäre auch möglich, solange das Repo öffentlich ist. Wird es privat, braucht die Instanz einen Token. Den legen wir nicht auf die Platte. Der private Bucket und `LabInstanceProfile` kommen ohne Access Key aus. Die Rolle existiert im Lab schon. Ein `resource "aws_iam_role"` ist im Learner Lab verboten.

## Was nie in den öffentlichen Bucket darf

Der Website-Bucket ist weltweit lesbar. Dort liegen nur die statischen Seiten: HTML, CSS, JS, `version.txt` und `config.js`.

Nicht dort: das Jar, das Startskript, `version-server.py`, SSH-Schlüssel, AWS-Zugangsdaten, der State, der Inhalt des Artefakt-Buckets.

`config.js` schreibt OpenTofu mit der Elastic IP. Das Release-Zip enthält die lokale Adresse `127.0.0.1`. Der Deploy kopiert zuerst die `config.js` aus dem Bucket über die Datei aus dem Zip und synchronisiert erst dann. Ein späteres Frontend-Release überschreibt die Adresse der Instanz nicht.

## Ports

| Port | Wer | Warum |
|---|---|---|
| 22 | nur `ssh_cidr` | Deploy und Fehlersuche per SSH |
| 7681 | Browser | ttyd. Die Seite auf S3 bettet diese Adresse ein |
| 7682 | Browser und Pipeline | `GET /version` |
| 443 raus | die Instanz | Corretto, ttyd, S3, SSM |
| 53 UDP raus | die Instanz | DNS |

Port 80 auf der Instanz bleibt zu. Die Seite kommt von S3.

Die Default-Route-Tabelle im Lab hatte kein Ziel für `0.0.0.0/0`. Ohne die Route zum vorhandenen Internet Gateway kommt die Instanz nicht ins Netz und niemand kommt zu ihr. `aws_route.public` setzt diese Route.

## Speicher

`t3.micro` hat 1 GB. Jeder Tab startet eine JVM (`-Xmx64m`) plus den Rest der JVM. Zwei bis drei gleichzeitige Partien passen, mehr verdrängt der Speicher.

## Outputs

Öffentlich und kein Geheimnis: `frontend_url`, `console_url`, `version_url`, `public_ip`.

Nur für das Deployment, nicht geheim, aber nicht auf die Webseite: `instance_id`, `artifact_bucket`, `frontend_bucket`, `aws_region`.

Geheim und nicht im Git: der private SSH-Schlüssel, die AWS-Session-Keys aus dem Lab, `infra/terraform.tfvars`, `*.tfstate`.

## Infrastruktur anlegen

Im Lab die Zugangsdaten in die Shell legen. Nicht ins Repo schreiben.

```sh
cd infra
cp terraform.tfvars.example terraform.tfvars
```

In `terraform.tfvars`: Region des Labs, `key_name` aus dem IaC-Auftrag, `ssh_cidr` als die eigene öffentliche Adresse mit `/32`.

```sh
tofu init
tofu plan
tofu apply
```

Danach existieren Instanz, feste IP und leere Buckets. `config.js` liegt schon im Website-Bucket. Der Dienst ist enabled und startet noch nichts Sinnvolles, weil `current` fehlt.

Das Learner Lab verbietet `s3:GetBucketObjectLockConfiguration`. Die Ressource `aws_s3_bucket` liest das bei jedem Plan und scheitert. Deshalb legt OpenTofu die Buckets mit der AWS-CLI an und verwaltet danach Policy, Website und `config.js`. Die Seite muss öffentlich lesbar sein, sonst sieht der Browser sie nicht.

## Release von Hand

Release A ist `1.1.0`.

```sh
sh ops/deploy-release.sh 1.1.0
```

Das Script lädt nur die drei Assets von GitHub, kopiert Jar und Webconsole-Zip in den privaten Bucket, schreibt `current`, startet den Dienst neu und legt das Frontend hoch. Anschliessend müssen `version_url` und `frontend_url/version.txt` beide `1.1.0` sagen.

Im Browser die `frontend_url` öffnen und eine Partie spielen.

Wenn SSM im Lab verweigert wird, dieselbe Wirkung per SSH:

```sh
ssh -i <privater-schluessel> ec2-user@$(tofu -chdir=infra output -raw public_ip) sudo systemctl restart tictactest
```

### Zweiter Release, ohne neue Infrastruktur

Eine sichtbare Änderung nur im Frontend, zum Beispiel ein Satz in `frontend/index.html`. Abschnitt in `CHANGELOG.md`, Commit, Tag `v1.2.0`, warten bis der Release die drei Assets hat.

```sh
tofu -chdir=infra plan
sh ops/deploy-release.sh 1.2.0
```

`tofu plan` darf keine Änderung zeigen. Die Instanz-ID bleibt. Die Seite zeigt die neue Frontend-Version, das Backend die neue Nummer aus dem neuen Jar. Die Maschine wurde nicht neu erstellt.

## Teil 4

Diese drei Versuche sind auf diesem Rechner nicht gelaufen. Hier ist kein Learner Lab angemeldet. So müssen sie ausgehen:

1. Session beenden und neu starten. Das Lab stoppt die Instanz und startet sie wieder. Die Elastic IP bleibt, die URL bleibt. systemd startet den Dienst, `fetch.sh` liest dasselbe `current`. Dieselbe Version läuft, ohne Befehl.

2. Eine Umgebungsvariable im Unit in `user_data` ändern. `tofu plan` zeigt ein Stop/Start der Instanz, kein Ersatz, weil `user_data_replace_on_change` falsch ist. Die IP bleibt. `current` bleibt, also die Version auch. `user_data` läuft beim zweiten Start nicht noch einmal. Die neue Variable ist auf der bestehenden Instanz deshalb nicht aktiv. Dafür die Unit-Datei von aussen kopieren oder die Instanz wirklich ersetzen. Eine App-Version ändert `user_data` nicht.

3. `tofu destroy` und `tofu apply`. Neu sind IP, Instanz-ID und beide Bucket-Namen. `config.js` enthält die neue IP. Die alte Version ist weg, weil der Artefakt-Bucket mit `force_destroy` gelöscht wird. Danach noch einmal `sh ops/deploy-release.sh 1.1.0`. Bis dahin läuft kein Spiel.
