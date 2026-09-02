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

### 1. DummyTest – `dummyJunitExample`

| | |
|---|---|
| **GIVEN** | `true` |
| **WHEN** | `assertTrue(true)` (JUnit) |
| **THEN** | Assertion ist erfolgreich |

### 2. DummyTest – `dummyAssertJExample`

| | |
|---|---|
| **GIVEN** | `true` |
| **WHEN** | `assertThat(true).isTrue()` (AssertJ) |
| **THEN** | Assertion ist erfolgreich |

### 3. `isWin_detectsWinningTopRow`

| | |
|---|---|
| **GIVEN** | Board `XXX......` (obere Reihe gewinnt) |
| **WHEN** | `isWin(board, CROSS)` |
| **THEN** | `true` |

### 4. `isWin_detectsWinningMiddleColumn`

| | |
|---|---|
| **GIVEN** | Board `.X..X..X.` (mittlere Spalte) |
| **WHEN** | `isWin(board, CROSS)` |
| **THEN** | `true` |

### 5. `isWin_detectsWinningMainDiagonal`

| | |
|---|---|
| **GIVEN** | Board `X...X...X` (Hauptdiagonale) |
| **WHEN** | `isWin(board, CROSS)` |
| **THEN** | `true` |

### 6. `isWin_returnsFalseWhenNoThreeInALine`

| | |
|---|---|
| **GIVEN** | Board `XOX.OX...` (kein Gewinner) |
| **WHEN** | `isWin(board, CROSS)` und `isWin(board, CIRCLE)` |
| **THEN** | beide `false` |

### 7. `isWin_returnsFalseForWrongColorDespiteWinningLine`

| | |
|---|---|
| **GIVEN** | Board `OOO......` (Kreise gewinnen oben) |
| **WHEN** | `isWin(board, CROSS)` und `isWin(board, CIRCLE)` |
| **THEN** | `false` bzw. `true` |
