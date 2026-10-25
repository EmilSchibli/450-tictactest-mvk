# Testdokumentation

Alle Tests liegen in `src/test/java/ch/bbw/m450/tictactoe/`. Start mit `.\gradlew.bat test`.

Assertions laufen über AssertJ. Boards kommen aus `BoardFixtures` und werden mit `BoardTestHelper.toBoard` gebaut (`X`, `O`, `.`).

## TicTacToeMainTest

Prüft `isWin`, `toString` und `play`.

- `givenBoard_whenIsWin_thenExpected`: acht Gewinnlinien, kein Gewinn, falsche Farbe, leer, zwei Steine und Draw liefern den erwarteten boolean.
- `givenEmptyBoard_whenToString_thenShowsAllIndices`: leeres Board zeigt 0–8 in drei Zeilen, ohne X und O.
- `givenCross_whenToString_thenShowsX`: Feld 0 ist ein fettes X.
- `givenCircle_whenToString_thenShowsO`: Feld 0 ist ein fettes O.
- `givenTwoGreedyPlayers_whenPlay_thenCrossWins`: zwei Greedy, Sieger ist CROSS, stdout enthält `winner is: CROSS`.
- `givenDrawMoves_whenPlay_thenNull`: feste Zuglisten, Rückgabe `null`, stdout enthält `it's a draw!`.
- `givenCircleMoves_whenPlay_thenCircleWins`: Kreis belegt 3, 4 und 5 und gewinnt, stdout enthält `winner is: CIRCLE`.
- `givenPlayers_whenPlay_thenEachGetsOwnColor`: erster Spieler bekommt immer CROSS, zweiter immer CIRCLE; Sieger ist CROSS.
- `givenSamePlayer_whenPlay_thenThrows`: dasselbe Objekt zweimal wirft `IllegalArgumentException`.
- `givenOutOfBoardMove_whenPlay_thenThrows`: Zug -1 oder 9 wirft, stdout ist das leere Board.
- `givenOccupiedField_whenPlay_thenThrows`: beide spielen Feld 0, Meldung `cannot play to position 0`, stdout ist das Board mit X auf 0.

## GreedyPlayerTest

Prüft, dass immer das erste freie Feld genommen wird.

- `givenBoard_whenPlay_thenFirstFreeField`: leer ergibt 0, `XO.......` ergibt 2, letztes Feld frei ergibt 8, erstes Feld frei ergibt 0.
- `givenFullBoard_whenPlay_thenThrows`: volles Board wirft `IllegalStateException`.

## StoneTest

- `givenStone_whenOpponent_thenOtherStone`: CROSS wird CIRCLE und CIRCLE wird CROSS.

## PerfectPlayerTest

Prüft den besten Zug und ganze Partien.

- `givenBoard_whenPlay_thenBestMove`: der Index stimmt (eigener Gewinn, Gegner blocken, leeres Board, letztes Feld) und das Board bleibt gleich.
- `givenFullBoard_whenPlay_thenThrows`: Meldung `cannot play at all`.
- `givenTwoPerfectPlayers_whenPlay_thenDraw`: Rückgabe `null`, stdout enthält `it's a draw!`.
- `givenPerfectAgainstGreedy_whenPlay_thenPerfectWins`: Perfect gewinnt als Kreuz und als Kreis, beide Siege stehen auf stdout.

## HumanPlayerTest

`HumanPlayer` liest von stdin, Eingabe über Pioneer `@StdIo`.

- `givenStdIn_whenHumanPlays_thenMoveAndPrompt`: Eingabe `4` ergibt Zug 4, stdout enthält `where to to put the next CROSS?`.
- `givenTextInput_whenHumanPlays_thenThrows`: Eingabe `abc` wirft `NumberFormatException`.

## PlayerArchitectureTest

- `givenPlayerPackage_whenChecked_thenClassesImplementTicTacToePlayer`: das Paket `players` hat genau drei Klassen, alle implementieren `TicTacToePlayer`.

## Hilfen

`BoardFixtures` hält die String-Boards. `BoardTestHelper` wandelt sie um und ruft `isWin` auf. Beide haben keine eigenen Testmethoden.

JaCoCo: `.\gradlew.bat test jacocoTestReport` (Grenze 90 % Branch in `build.gradle`).

PIT: `.\gradlew.bat pitest`.
