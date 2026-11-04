# Releasekonzept

Ein Release ist eine veröffentlichte Version des Spiels, nicht das DevContainer-Image. Das Image in GHCR (`v1.0.1` in `.devcontainer/VERSION`) ist die Werkstatt für CI und Cursor. Das Spiel selbst wird als JAR veröffentlicht, wenn jemand bewusst einen Tag `vX.Y.Z` auf einen Commit von `main` pusht.

Ein normaler Push startet `ci.yml` und auf `main` den Coverage-Verlauf. Er startet keinen Release. Sonst bekäme jeder Zwischenstand eine Versionsnummer.

## Version

Schema `MAJOR.MINOR.PATCH`, Tag mit `v` davor. Der Tag ist die Version. Der Workflow nimmt `v1.0.0` und baut `tictactest-1.0.0.jar`. Ohne Tag heisst das JAR lokal `tictactest-0.0.0-dev.jar`.

| Teil | Wann |
|---|---|
| MAJOR | Start, Bedienung oder Spielregeln ändern sich so, dass die bisherige Nutzung bricht |
| MINOR | Neue Funktion, bisherige Nutzung bleibt |
| PATCH | Fehlerbehebung ohne neue Funktion |

Der erste Release ist `v1.0.0`. Eine schon veröffentlichte Version wird nicht verschoben. Ein Fix ist mindestens der nächste PATCH.

## Ablauf

1. Die gewünschten Änderungen liegen auf `main`, `.\gradlew.bat test` und die CI sind grün.
2. `CHANGELOG.md` bekommt einen Abschnitt `## X.Y.Z - Datum` mit Added, Changed und Fixed. Das ist der Text für Menschen, keine Commit-Liste.
3. Diesen Stand nach `main` pushen und warten, bis die CI grün ist.
4. Tag setzen und nur den Tag pushen:

```powershell
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0
```

5. `.github/workflows/release.yml` prüft, dass der Tag auf `main` liegt und `X.Y.Z` ist, führt `test` aus (damit auch die 90 % Branch) und baut `./gradlew jar -Pversion=X.Y.Z`.
6. Der Abschnitt aus `CHANGELOG.md` wird zur Release-Beschreibung. Fehlt er, entsteht kein Release.
7. GitHub Release `TicTacTest vX.Y.Z` mit dem JAR als Asset.

Schlägt ein Test fehl, gibt es kein Release. Der Tag bleibt. Den Fehler auf `main` beheben. Wurde noch nichts veröffentlicht, den Tag löschen und neu setzen. Wurde schon veröffentlicht, eine neue Versionsnummer nehmen.

## Prüfen

Das JAR von der Release-Seite herunterladen und starten, ohne es neu zu bauen:

```powershell
java -jar tictactest-1.0.0.jar
```

Eine Partie mit den Zügen `4`, `2`, `6` endet mit `winner is: CROSS`.
