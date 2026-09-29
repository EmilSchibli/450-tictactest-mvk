# DevContainer

## Image

- Definition: `.devcontainer/Dockerfile`
- Basis: `azul/zulu-openjdk-alpine:25.0.4.1-25.36` (Alpine, Java 25 von Azul Zulu)
- Gradle 9.7.0 ist im Image installiert (gleiche Version wie der Wrapper, Checksumme wird geprüft)
- JUnit kommt über die Gradle-Dependencies in `build.gradle`
- Benutzer `dev` mit UID:GID `1000:1000`
- Extensions: Java Extension Pack und Gradle (`.devcontainer/devcontainer.json`)

## Lokal verwenden

In VS Code bzw. Cursor "Reopen in Container". Das Image aus `devcontainer.json` wird aus der GitHub Container Registry geladen.

## Image von Hand bauen und testen

```
docker build -t ghcr.io/emilschibli/tictactest-m450:latest .devcontainer
docker run --rm --user dev -v "${PWD}:/workspace" -w /workspace ghcr.io/emilschibli/tictactest-m450:latest ./gradlew test
```

## Image in die GitHub Container Registry laden

Von Hand (Token mit `write:packages`):

```
echo $CR_PAT | docker login ghcr.io -u EmilSchibli --password-stdin
docker push ghcr.io/emilschibli/tictactest-m450:latest
```

Normalerweise macht das der Workflow `Devcontainer` (siehe unten).

## CI mit dem Image aus der Registry

Build, Tests und PIT in `.github/workflows/ci.yml` laufen im Image aus der GitHub Container Registry. Das Image ist mit einem festen Tag angegeben und nicht mit `:latest`, damit ein neuer Push die CI nicht unbemerkt verändert.

## Versionierung und Freigabe

Die Version steht in `.devcontainer/VERSION` im Format `MAJOR.MINOR.PATCH`. Das Image bekommt den Tag `vMAJOR.MINOR.PATCH`.

- MAJOR: anderes Base-Image oder andere Java-Version
- MINOR: neues Tool im Image
- PATCH: kleine Änderungen, z.B. Updates

Ablauf im Workflow `.github/workflows/devcontainer.yml`:

- Pull Request: das Image wird gebaut und geprüft (Java, Gradle, UID:GID, `./gradlew test`), aber nicht gepusht. Wenn das Dockerfile geändert wurde und die Version schon in der Registry ist, schlägt der Check fehl.
- Push auf `main` (nur wenn `Dockerfile` oder `VERSION` geändert): das geprüfte Image wird als `vX.Y.Z` und `latest` gepusht. Das ist die Freigabe. Gibt es die Version schon von einem anderen Commit, schlägt der Workflow fehl, eine freigegebene Version wird nie überschrieben.
- Manuell auf `main` starten: ist die Version schon freigegeben, wird nichts neu gebaut, nur der Update-PR erstellt.

Branch-Builds kommen nie in die Registry. CI und DevContainer verwenden nur freigegebene `vX.Y.Z` Tags.

## Automatischer Update-PR

Nach jeder Freigabe öffnet der Job `Create update PR` einen Pull Request auf dem Branch `update-devcontainer-image`. Er setzt den neuen Tag in `.devcontainer/devcontainer.json` und in allen Workflows (`ci.yml`) und fragt `bernedom` und `Seismix` als Reviewer an. Gibt es den PR schon, wird er aktualisiert.

Der Branch wird mit einem Deploy Key (Secret `DEPLOY_KEY`) gepusht. Mit dem `GITHUB_TOKEN` dürfte man keine Workflow-Dateien ändern und die CI würde auf dem PR nicht starten.

Erst wenn dieser PR gemergt ist, verwenden CI und lokale DevContainer die neue Version. Beim nächsten Öffnen fragt VS Code bzw. Cursor nach einem Rebuild und lädt das neue Image. Der Merge ändert weder `Dockerfile` noch `VERSION`, darum wird kein neues Image gebaut.

Einstellungen im Repo:

- Actions > General: "Allow GitHub Actions to create and approve pull requests"
- Deploy Key mit Schreibrechten, privater Schlüssel als Secret `DEPLOY_KEY`

## Gesamter Ablauf

1. Dockerfile ändern und `.devcontainer/VERSION` erhöhen, PR öffnen
2. PR-Check baut und testet das Image, pusht aber nichts
3. Merge auf `main`: Image wird als `vX.Y.Z` und `latest` gepusht (Freigabe)
4. Automatischer PR setzt `vX.Y.Z` in `devcontainer.json` und `ci.yml`, CI läuft mit dem neuen Image
5. Merge des Update-PRs: CI und lokale DevContainer verwenden ab jetzt `vX.Y.Z`
