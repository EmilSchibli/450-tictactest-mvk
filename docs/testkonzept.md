# Testkonzept

Getestet wird die Spiellogik (`isWin`, `toString`, `play`), die Player (`GreedyPlayer`, `PerfectPlayer`, `HumanPlayer`) und die Konsole mit Pioneer (`@StdIo`).

`isWin`: alle 8 Linien, kein Gewinn, falsche Farbe, leeres Board, Unentschieden. `play`: Sieg X, Sieg O, Unentschieden, Feld ausserhalb, belegtes Feld, zweimal derselbe Spieler. `GreedyPlayer` nimmt das erste freie Feld. `PerfectPlayer` spielt den besten Zug, zwei Perfect enden unentschieden, Perfect gewinnt gegen Greedy. `Stone.opponent()` tauscht X und O. ArchUnit: die Klassen in `players` implementieren `TicTacToePlayer`.

Tools: JUnit, AssertJ, JaCoCo (mindestens 90 % Branch), PIT, ArchUnit. StdIn/StdOut über Pioneer.

Nicht getestet: `TicTacToeMain.main`.

Lokal: `.\gradlew.bat test`
