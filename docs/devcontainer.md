# DevContainer

Image: `azul/zulu-openjdk-alpine:25.0.4.1-25.36` (`.devcontainer/Dockerfile`).
User `dev`, UID:GID `1000:1000`.
Der Workflow `.github/workflows/devcontainer.yml` pusht das Image nach GHCR (`ghcr.io/<owner>/tictactest-m450`).
Der Tag kommt aus `.devcontainer/VERSION`, Format `vX.Y.Z`.
Lokal: Reopen in Container.
