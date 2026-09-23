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

---

## Coverage und Mutation Score

| | vorher | nachher |
| --- | --- | --- |
| JaCoCo Instructions | 34.4 % (129/375) | 96.8 % (363/375) |
| JaCoCo Branches | 34.6 % (27/78) | 100 % (78/78) |
| PIT Mutation Score | 25 % (14/57) | 100 % (57/57) |

Nicht abgedeckt ist nur `main(...)` (und der leere Konstruktor von `TicTacToeMain`).
