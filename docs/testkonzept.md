# Testkonzept

Getestet wird das Tic-Tac-Toe in `ch.bbw.m450.tictactoe`: die Spiellogik in `TicTacToeMain` und die drei Spieler `GreedyPlayer`, `PerfectPlayer` und `HumanPlayer`.

## Spiellogik

`isWin` prüft die acht Linien: drei Reihen, drei Spalten, zwei Diagonalen. Dazu kommen Fälle ohne Gewinn, die falsche Farbe auf einer vollen Linie, ein leeres Board, nur zwei Steine in einer Linie und ein volles Unentschieden.

`toString` zeigt auf dem leeren Board die Nummern 0 bis 8 in drei Zeilen, ohne X oder O. Ein gesetztes Feld wird als fettes X oder O ausgegeben (`\033[1m`).

`play` spielt eine ganze Partie:

- zwei `GreedyPlayer`: Kreuz gewinnt, stdout enthält `winner is: CROSS`
- Kreis macht die mittlere Reihe und gewinnt
- ein festes Zugskript endet unentschieden (`it's a draw!`, Rückgabe `null`)
- der erste Spieler bekommt `CROSS`, der zweite `CIRCLE`
- zweimal dasselbe Spieler-Objekt wirft `IllegalArgumentException`
- Zug ausserhalb (-1 und 9) und ein schon belegtes Feld werfen `IllegalStateException`; beim Zug ausserhalb steht das leere Board auf stdout

## Spieler

`GreedyPlayer` nimmt immer das erste freie Feld von links oben. Ein volles Board wirft `IllegalStateException`.

`PerfectPlayer` wählt den besten Zug, bei gleichem Wert den kleinsten Index. Das übergebene Board bleibt unverändert. Ein volles Board wirft mit der Meldung `cannot play at all`. Zwei Perfect spielen unentschieden. Perfect gewinnt gegen Greedy, einmal als Kreuz und einmal als Kreis.

`Stone.opponent()` tauscht `CROSS` und `CIRCLE`.

`HumanPlayer` liest eine Zahl von stdin und schreibt die Frage `where to to put the next CROSS?` auf stdout. Die Eingabe `abc` wirft `NumberFormatException`.

ArchUnit prüft das Paket `players`: genau drei Klassen, und jede implementiert `TicTacToePlayer`.

## Werkzeuge

JUnit 5 (`useJUnitPlatform`) und AssertJ (`WithAssertions`). Wiederholte Fälle laufen als `@ParameterizedTest` mit `@MethodSource`, `@CsvSource` oder `@ValueSource`.

Die Boards stehen als Strings in `BoardFixtures` (`X`, `O`, `.` für leer). `BoardTestHelper.toBoard` macht daraus ein `Stone[]`. `BoardTestHelper.isWin` ruft die echte Methode auf.

Stdin und Stdout laufen über JUnit Pioneer. `@StdIo` setzt die Eingabe, `StdOut` fängt die Ausgabe. So braucht `HumanPlayer` keine Tastatur.

## JaCoCo und PIT

JaCoCo 0.8.15 schreibt HTML und XML nach `build/reports/jacoco/test`. `jacocoTestCoverageVerification` hängt an `test` und bricht den Build ab, wenn die Branch-Abdeckung unter 90 % liegt (`counter = BRANCH`, `minimum = 0.9`). Für Instructions gibt es keine Grenze, die Zahl steht nur im Report.

PIT (Pitest 1.30.1) verändert den Bytecode der Klassen `ch.bbw.m450.tictactoe.*` und lässt dieselben Tests dagegen laufen. HTML und XML liegen unter `build/reports/pitest`. Eine Mindestpunktzahl ist im Build nicht gesetzt.

## Nicht getestet

`TicTacToeMain.main` startet nur `HumanPlayer` gegen `GreedyPlayer` und wartet auf die Konsole. Die Partie selbst steckt in `play` und ist getestet. `BoardFixtures` und `BoardTestHelper` liegen im Testordner und sind keine Produktklassen.

Lokal: `.\gradlew.bat test`, danach `.\gradlew.bat jacocoTestReport` und `.\gradlew.bat pitest`.
