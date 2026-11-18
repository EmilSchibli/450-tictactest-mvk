# TicTacTest

Tic-Tac-Toe mit `GreedyPlayer`, `PerfectPlayer` und `HumanPlayer`. Java 25, Gradle. Start einer Partie: `.\gradlew.bat run`.

## Testen

```
.\gradlew.bat test
.\gradlew.bat jacocoTestReport
.\gradlew.bat pitest
```

`test` schreibt den JaCoCo-Report und bricht ab, wenn die Branch-Abdeckung unter 90 % liegt. HTML: `build/reports/jacoco/test/html` und `build/reports/pitest`.

Coverage-Verlauf: https://emilschibli.github.io/450-tictactest-mvk/coverage-history/

Konzept: [docs/testkonzept.md](docs/testkonzept.md). Fälle: [docs/test-documentation.md](docs/test-documentation.md). Image: [docs/devcontainer.md](docs/devcontainer.md). Release: [docs/releasekonzept.md](docs/releasekonzept.md). Browser: [docs/webkonsole.md](docs/webkonsole.md). Server: [docs/deployment.md](docs/deployment.md).
