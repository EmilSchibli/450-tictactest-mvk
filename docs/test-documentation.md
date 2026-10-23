# TicTacToe – Testdokumentation

**Repo:** https://github.com/EmilSchibli/450-tictactest-mvk  
**Branch:** `main`  
**Framework:** JUnit 5 + AssertJ

## Test-Code auf GitHub

- [DummyTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/DummyTest.java)
- [TicTacToeMainTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/TicTacToeMainTest.java)
- [BoardTestHelper.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/BoardTestHelper.java)
- [GreedyPlayerTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/GreedyPlayerTest.java)
- [HumanPlayerTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/HumanPlayerTest.java)
- [StoneTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/StoneTest.java)
- [PerfectPlayerTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/PerfectPlayerTest.java)
- [PioneerConsoleTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/PioneerConsoleTest.java)
- [PlayerArchitectureTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/main/src/test/java/ch/bbw/m450/tictactoe/PlayerArchitectureTest.java)

Tests ausführen:

```bat
.\gradlew.bat test
```

---

## Tests (GIVEN – WHEN – THEN)

Board-Hilfe: `X` = Kreuz, `O` = Kreis, `.` = leer (Index 0–8)

Fixtures: `BoardFixtures` (Test-Boards) · Helper: `BoardTestHelper.toBoard()` / `isWin()`

### 1. `givenTrue_whenJunitAssertTrue_thenPasses`

| | |
|---|---|
| **GIVEN** | `true` |
| **WHEN** | `assertTrue(true)` (JUnit) |
| **THEN** | Assertion ist erfolgreich |

### 2. `givenTrue_whenAssertJAssertTrue_thenPasses`

| | |
|---|---|
| **GIVEN** | `true` |
| **WHEN** | `assertThat(true).isTrue()` (AssertJ) |
| **THEN** | Assertion ist erfolgreich |

### 3. `givenTopRowWin_whenIsWinCross_thenTrue`

| | |
|---|---|
| **GIVEN** | Board `XXX......` (obere Reihe gewinnt) |
| **WHEN** | `isWin(board, CROSS)` |
| **THEN** | `true` |

### 4. `givenMiddleColumnWin_whenIsWinCross_thenTrue`

| | |
|---|---|
| **GIVEN** | Board `.X..X..X.` (mittlere Spalte) |
| **WHEN** | `isWin(board, CROSS)` |
| **THEN** | `true` |

### 5. `givenMainDiagonalWin_whenIsWinCross_thenTrue`

| | |
|---|---|
| **GIVEN** | Board `X...X...X` (Hauptdiagonale) |
| **WHEN** | `isWin(board, CROSS)` |
| **THEN** | `true` |

### 6. `givenNoWinner_whenIsWinBoth_thenFalse`

| | |
|---|---|
| **GIVEN** | Board `XOX.OX...` (kein Gewinner) |
| **WHEN** | `isWin(board, CROSS)` und `isWin(board, CIRCLE)` |
| **THEN** | beide `false` |

### 7. `givenCircleTopRow_whenIsWin_thenCrossFalseCircleTrue`

| | |
|---|---|
| **GIVEN** | Board `OOO......` (Kreise gewinnen oben) |
| **WHEN** | `isWin(board, CROSS)` und `isWin(board, CIRCLE)` |
| **THEN** | `false` bzw. `true` |

### 8. `givenBoard_whenIsWin_thenExpected` (neue Boards)

| | |
|---|---|
| **GIVEN** | alle 8 Linien (`MID_ROW_O`, `BOTTOM_ROW_X`, `LEFT_COL_O`, `RIGHT_COL_O`, `ANTI_DIAG_O`, ...), `EMPTY`, `XX.......`, `......OO.`, `FULL_DRAW` |
| **WHEN** | `isWin(board, color)` |
| **THEN** | `true` bei drei in einer Linie, sonst `false` |

### 9. `givenEmptyBoard_whenToString_thenShowsAllIndices`

| | |
|---|---|
| **GIVEN** | leeres Board |
| **WHEN** | `TicTacToeMain.toString(board)` |
| **THEN** | Zahlen 0-8, kein X/O, 3 Zeilen |

### 10. `givenCross_whenToString_thenShowsX` / `givenCircle_whenToString_thenShowsO`

| | |
|---|---|
| **GIVEN** | Board `X........` bzw. `O........` |
| **WHEN** | `TicTacToeMain.toString(board)` |
| **THEN** | fettes X bzw. O, nicht der andere Stein, keine 0 |

### 11. `givenTwoGreedyPlayers_whenPlay_thenCrossWins`

| | |
|---|---|
| **GIVEN** | zwei `GreedyPlayer` |
| **WHEN** | `play(x, o)` |
| **THEN** | `CROSS`, Ausgabe enthält `winner is: CROSS` |

### 12. `givenCircleMoves_whenPlay_thenCircleWins`

| | |
|---|---|
| **GIVEN** | X spielt 0, 1, 6 - O spielt 3, 4, 5 |
| **WHEN** | `play(x, o)` |
| **THEN** | `CIRCLE` |

### 13. `givenDrawMoves_whenPlay_thenNull`

| | |
|---|---|
| **GIVEN** | Züge, die zu `XOXXOOOXX` führen |
| **WHEN** | `play(x, o)` |
| **THEN** | `null`, Ausgabe enthält `it's a draw!` |

### 14. `givenPlayers_whenPlay_thenEachGetsOwnColor`

| | |
|---|---|
| **GIVEN** | zwei Spieler, die ihre Farbe prüfen |
| **WHEN** | `play(x, o)` |
| **THEN** | X bekommt immer `CROSS`, O immer `CIRCLE` |

### 15. `givenSamePlayer_whenPlay_thenThrows`

| | |
|---|---|
| **GIVEN** | zweimal derselbe Spieler |
| **WHEN** | `play(player, player)` |
| **THEN** | `IllegalArgumentException` |

### 16. `givenOutOfBoardMove_whenPlay_thenThrows`

| | |
|---|---|
| **GIVEN** | Spieler spielt `-1` bzw. `9` |
| **WHEN** | `play(x, o)` |
| **THEN** | `IllegalStateException` mit dem Index, leeres Board wird ausgegeben |

### 17. `givenOccupiedField_whenPlay_thenThrows`

| | |
|---|---|
| **GIVEN** | beide Spieler spielen immer `0` |
| **WHEN** | `play(x, o)` |
| **THEN** | `IllegalStateException` |

### 18. `givenBoard_whenPlay_thenFirstFreeField` (GreedyPlayer)

| | |
|---|---|
| **GIVEN** | `.........`, `XO.......`, `XOXOXOXO.`, `.OXOXOXOX` |
| **WHEN** | `new GreedyPlayer().play(board, CROSS)` |
| **THEN** | `0`, `2`, `8`, `0` |

### 19. `givenFullBoard_whenPlay_thenThrows` (GreedyPlayer)

| | |
|---|---|
| **GIVEN** | volles Board `XOXXOOOXX` |
| **WHEN** | `new GreedyPlayer().play(board, CROSS)` |
| **THEN** | `IllegalStateException` |

### 20. `givenNumberInput_whenPlay_thenReturnsNumber` (HumanPlayer)

| | |
|---|---|
| **GIVEN** | Eingabe `4` über `System.setIn` |
| **WHEN** | `new HumanPlayer().play(board, CROSS)` |
| **THEN** | `4`, Ausgabe fragt nach dem nächsten `CROSS` |

### 21. `givenTextInput_whenPlay_thenThrows` (HumanPlayer)

| | |
|---|---|
| **GIVEN** | Eingabe `abc` |
| **WHEN** | `new HumanPlayer().play(board, CROSS)` |
| **THEN** | `NumberFormatException` |

### 22. `givenStone_whenOpponent_thenOtherStone`

| | |
|---|---|
| **GIVEN** | `CROSS` bzw. `CIRCLE` |
| **WHEN** | `stone.opponent()` |
| **THEN** | `CIRCLE` bzw. `CROSS` |

### 23. `givenBoard_whenPlay_thenBestMove` (PerfectPlayer)

| | |
|---|---|
| **GIVEN** | `.OXXX.OO.` als X und als O, `XX.OO....` als X, leeres Board, `XOXXOOOX.` |
| **WHEN** | `new PerfectPlayer().play(board, color)` |
| **THEN** | `5`, `8`, `2`, `0`, `8`. Das Board wird nicht verändert |

`.OXXX.OO.` als O: Feld 8 gewinnt unten sofort. Feld 5 ist nur unentschieden, Feld 0 verliert. Greedy würde 0 spielen.

### 24. `givenFullBoard_whenPlay_thenThrows` (PerfectPlayer)

| | |
|---|---|
| **GIVEN** | volles Board `XOXXOOOXX` |
| **WHEN** | `play` |
| **THEN** | `IllegalStateException` mit Text `cannot play at all` |

### 25. `givenTwoPerfectPlayers_whenPlay_thenDraw`

| | |
|---|---|
| **GIVEN** | zwei `PerfectPlayer` |
| **WHEN** | `play(x, o)` |
| **THEN** | `null`, Ausgabe enthält `it's a draw!` |

### 26. `givenPerfectAgainstGreedy_whenPlay_thenPerfectWins`

| | |
|---|---|
| **GIVEN** | Perfect gegen Greedy, einmal als X und einmal als O |
| **WHEN** | `play` |
| **THEN** | X gewinnt als Perfect (`CROSS`), O gewinnt als Perfect (`CIRCLE`) |

### 27. `givenStdIn_whenHumanPlays_thenMoveAndPrompt` (Pioneer)

| | |
|---|---|
| **GIVEN** | `@StdIo({ "4" })` |
| **WHEN** | `HumanPlayer.play` |
| **THEN** | Zug `4`, Ausgabe enthält `where to to put the next CROSS?` |

### 28. `givenTwoGreedyPlayers_whenPlay_thenWinnerOnStdOut` (Pioneer)

| | |
|---|---|
| **GIVEN** | zwei `GreedyPlayer`, stdout über `@StdIo` |
| **WHEN** | `play` |
| **THEN** | `CROSS`, Ausgabe enthält `winner is: CROSS` |

### 29. `givenPlayerPackage_whenChecked_thenClassesImplementTicTacToePlayer` (ArchUnit)

| | |
|---|---|
| **GIVEN** | Package `ch.bbw.m450.tictactoe.players` |
| **WHEN** | ArchUnit-Regel |
| **THEN** | genau 3 Klassen, alle implementieren `TicTacToePlayer` |

---

## Coverage und Mutation Score

Gemessen mit `.\gradlew.bat test jacocoTestReport jacocoTestCoverageVerification` und `.\gradlew.bat pitest`.

| | vorher | jetzt |
| --- | --- | --- |
| JaCoCo Instructions | 34.4 % (129/375) | 97.6 % (485/497) |
| JaCoCo Branches | 34.6 % (27/78) | 100 % (100/100) |
| JaCoCo Lines | | 96.1 % (74/77) |
| PIT Mutation Score | 25 % (14/57) | 100 % (76/76) |

Vor dem PerfectPlayer standen hier 96.8 % Instructions (363/375), 100 % Branches (78/78) und PIT 100 % (57/57).

Nicht abgedeckt sind `TicTacToeMain.main` (2 Zeilen) und der leere Konstruktor (1 Zeile). Branches fehlen keine. Die Verification in `build.gradle` verlangt mindestens 90 % Branches.
