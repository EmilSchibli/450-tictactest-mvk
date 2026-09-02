# TicTacToe – Testdokumentation

**Repo:** https://github.com/EmilSchibli/450-tictactest-mvk  
**Branch:** `add-junit-tests`  
**Framework:** JUnit 5 + AssertJ

## Test-Code auf GitHub

- [DummyTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/add-junit-tests/src/test/java/ch/bbw/m450/tictactoe/DummyTest.java)
- [TicTacToeMainTest.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/add-junit-tests/src/test/java/ch/bbw/m450/tictactoe/TicTacToeMainTest.java)
- [BoardTestHelper.java](https://github.com/EmilSchibli/450-tictactest-mvk/blob/add-junit-tests/src/test/java/ch/bbw/m450/tictactoe/BoardTestHelper.java)

Tests ausführen:

```bat
.\gradlew.bat test
```

---

## Tests (GIVEN – WHEN – THEN)

Board-Hilfe: `X` = Kreuz, `O` = Kreis, `.` = leer (Index 0–8)

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
