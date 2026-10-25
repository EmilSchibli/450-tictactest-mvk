# DevContainer

Das Image steht in `.devcontainer/Dockerfile`. Basis ist `azul/zulu-openjdk-alpine:25.0.4.1-25.36`, also Java 25. Dazu kommen bash, git, curl und Gradle 9.8.2.

Der User heisst `dev`, UID und GID sind `1000:1000`. Er darf sudo ohne Passwort. `GRADLE_USER_HOME` ist `/home/dev/.gradle`.

`.devcontainer/devcontainer.json` zeigt auf das Image in GHCR (`ghcr.io/<owner>/tictactest-m450:<tag>`) und setzt `remoteUser` auf `dev`. In Cursor: Reopen in Container.

## CI

`.github/workflows/devcontainer.yml` baut das Image, wenn sich Dockerfile oder `.devcontainer/VERSION` auf `main` ändern. Pull Requests auf `.devcontainer/**` bauen ebenfalls, pushen aber nicht.

Die Version steht in `.devcontainer/VERSION` als `x.y.z` (aktuell `1.0.1`). Der Tag ist `v` plus diese Nummer, also `ghcr.io/<owner>/tictactest-m450:v1.0.1`, zusätzlich `latest`. Nur ein Push auf `main` loggt sich bei GHCR ein. Existiert der Tag schon, wird nicht nochmals gepusht. Danach öffnet der Workflow einen PR, der den neuen Tag in `devcontainer.json` und `ci.yml` einträgt.

Vor dem Push prüft der Workflow im Image `java -version`, `gradle --version` und `id` von `dev` (`1000:1000`) und führt `./gradlew test` aus.

## Lokal

Build-Kontext ist der Ordner `.devcontainer`:

```
docker build -f .devcontainer/Dockerfile .devcontainer
docker run --rm -v "${PWD}:/workspace" -w /workspace <image> ./gradlew test --no-daemon
```

Unter PowerShell den Volume-Pfad mit `${PWD}` in Anführungszeichen setzen, damit der Doppelpunkt im Laufwerk nicht als Volume-Trenner gilt.
