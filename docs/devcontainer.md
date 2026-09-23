# DevContainer

## Image

- Definition: `.devcontainer/Dockerfile`
- Basis: `azul/zulu-openjdk-alpine:25.0.4.1-25.36` (Alpine, Java 25 von Azul Zulu)
- Gradle 9.7.0 ist im Image installiert (gleiche Version wie der Wrapper, Checksumme wird geprüft)
- JUnit kommt über die Gradle-Dependencies in `build.gradle`
- Benutzer `dev` mit UID:GID `1000:1000`
- Extensions: Java Extension Pack und Gradle (`.devcontainer/devcontainer.json`)

## Docker lokal

Docker ist auf dem Schul-PC vom Systemadministrator gesperrt. Das Image wird deshalb nur in GitHub Actions gebaut und getestet.

Ohne Docker braucht man lokal Java 25:

```
./gradlew test
```

Unter Windows: `.\gradlew.bat test`

## Image von Hand bauen

Mit Docker:

```
docker build -t ghcr.io/emilschibli/tictactest-m450:latest .devcontainer
```

Ohne Docker: in GitHub unter Actions den Workflow `Devcontainer` mit "Run workflow" starten. Er baut das Image, taggt es mit `:latest` und prüft Java, Gradle und den Benutzer `dev`. Der Button erscheint erst, wenn der Workflow auf `main` ist.

## Image in die GitHub Container Registry laden

Mit Docker (Token mit `write:packages`):

```
echo $CR_PAT | docker login ghcr.io -u EmilSchibli --password-stdin
docker push ghcr.io/emilschibli/tictactest-m450:latest
```

Ohne Docker: der Workflow `Devcontainer` pusht das Image nach der Prüfung mit dem `GITHUB_TOKEN` in die Registry (ab der Versionierung nur noch von `main`, siehe unten).

## CI mit dem Image aus der Registry

`.github/workflows/ci.yml` baut kein Image mehr. Build, Tests und PIT laufen im Image aus der GitHub Container Registry. Das Image ist mit einem festen Tag angegeben und nicht mit `:latest`, damit ein neuer Push die CI nicht unbemerkt verändert.

## Versionierung und Freigabe

Die Version steht in `.devcontainer/VERSION` im Format `MAJOR.MINOR.PATCH`. Das Image bekommt den Tag `vMAJOR.MINOR.PATCH`.

- MAJOR: anderes Base-Image oder andere Java-Version
- MINOR: neues Tool im Image
- PATCH: kleine Änderungen, z.B. Updates

Ablauf im Workflow `.github/workflows/devcontainer.yml`:

- Pull Request: das Image wird gebaut und geprüft (Java, Gradle, UID:GID, `./gradlew test`), aber nicht gepusht. Wenn das Dockerfile geändert wurde und die Version schon in der Registry ist, schlägt der Check fehl.
- Push auf `main`: das geprüfte Image wird als `vX.Y.Z` und `latest` gepusht. Das ist die Freigabe. Gibt es die Version schon, schlägt der Workflow fehl, eine freigegebene Version wird nie überschrieben.
- Manuell auf `main` starten: ist die Version schon freigegeben, wird nichts neu gebaut.

Branch-Builds kommen nie in die Registry. CI und DevContainer verwenden nur freigegebene `vX.Y.Z` Tags.

## Welches Image verwendet wird

Das verwendete Image steht nur an einer Stelle: `image` in `.devcontainer/devcontainer.json`.

- Lokal startet der DevContainer direkt dieses Image, es wird nichts selbst gebaut.
- `ci.yml` liest das Image im Job `Read image` aus `devcontainer.json` und startet damit Build, Tests und PIT.

So laufen CI und lokale Umgebung immer mit dem gleichen Image. Wer das Dockerfile lokal testen will, baut es wie oben von Hand.
