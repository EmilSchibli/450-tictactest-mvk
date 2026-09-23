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

Ohne Docker: der Workflow `Devcontainer` pusht das Image nach der Prüfung mit dem `GITHUB_TOKEN` nach `ghcr.io/emilschibli/tictactest-m450:latest`.
