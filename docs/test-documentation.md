# Testdokumentation

Tests: `src/test/java/ch/bbw/m450/tictactoe/`. Start: `.\gradlew.bat test`

| Klasse | Was |
| --- | --- |
| `TicTacToeMainTest` | `givenBoard_whenIsWin_thenExpected`, `toString`, `play` (Sieg, Draw, Fehler). Stdout mit `@StdIo` |
| `GreedyPlayerTest` | `givenBoard_whenPlay_thenFirstFreeField`, volles Board wirft |
| `StoneTest` | `givenStone_whenOpponent_thenOtherStone` |
| `PerfectPlayerTest` | `givenBoard_whenPlay_thenBestMove`, volles Board, Draw, Perfect schlägt Greedy |
| `PioneerConsoleTest` | HumanPlayer über `@StdIo`: Eingabe `4`, Text `abc`, Gewinnzeile |
| `PlayerArchitectureTest` | `givenPlayerPackage_whenChecked_thenClassesImplementTicTacToePlayer` |

`BoardFixtures` und `BoardTestHelper` legen nur die Boards an (`X`, `O`, `.`).

JaCoCo-Grenze in `build.gradle`: 90 % Branches. PIT: `.\gradlew.bat pitest`
