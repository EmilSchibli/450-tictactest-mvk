# TicTacTest

Coverage history: https://emilschibli.github.io/450-tictactest-mvk/coverage-history/

## Devcontainer

The devcontainer (`.devcontainer/Dockerfile`) is not used locally, because Docker is blocked on the developer's machine by the system administrator. It is used in CI instead:

- `.github/workflows/devcontainer.yml` builds the image when the Dockerfile changes and pushes it to `ghcr.io/emilschibli/tictactest-m450:<short-sha>` (`latest` only from `main`)
- on `main` it opens a PR that updates the image tag in `.github/workflows/ci.yml`
- `.github/workflows/ci.yml` runs build, tests and PIT inside that image

## Run locally without Docker

Requires Java 25.

```
./gradlew test
```

On Windows use `gradlew.bat test`.
