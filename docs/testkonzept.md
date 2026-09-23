# Testkonzept TicTacToe

BBW M450 TicTacTest

Stand: fertig (aktueller Stand der Tests)

---

## 1. Ziel

Wir wollen prüfen, ob `isWin` in `TicTacToeMain` richtig erkennt, ob jemand drei gleiche Steine in einer Reihe hat.

Das Spiel selbst soll laufen. Die Tests sollen das automatisch prüfen, ohne dass man jedes Board von Hand anklicken muss.

Lokal: `.\gradlew.bat test`  
CI: GitHub Actions (siehe Abschnitt 8)

---

## 2. Was wird getestet / was nicht

### Wird getestet

- Methode `TicTacToeMain.isWin(Stone[] board, Stone color)`
- Gewinn in einer Reihe (oben, X und O)
- Gewinn in einer Spalte (Mitte, X)
- Gewinn auf der Hauptdiagonale (X)
- Board ohne Gewinner (weder X noch O)
- Falsche Farbe: O hat gewonnen, X nicht
- alle 8 Linien, leeres Board, volles Unentschieden-Board, fast volle Reihe
- `toString` (X, O und leere Felder)
- Spielschleife `play(...)`: Sieg X, Sieg O, Unentschieden, ungültige Züge, gleicher Spieler
- `GreedyPlayer` (erstes freies Feld, volles Board)
- `HumanPlayer` mit `System.setIn` (Zahl und ungültige Eingabe)
- `Stone.opponent()`

Board-Darstellung in den Tests: 9 Zeichen, Index 0 bis 8.

```
0 | 1 | 2
--+---+--
3 | 4 | 5
--+---+--
6 | 7 | 8
```

`X` = Kreuz, `O` = Kreis, `.` = leer

### Wird nicht getestet (noch)

- `main(...)`: startet ein Spiel mit `HumanPlayer`, der pro Zug einen neuen `Scanner` auf `System.in` macht. Mehrere Eingaben hintereinander gehen so nicht sauber.
- ein späterer Perfect Player

Kein Mocking. Mockito ist in der Aufgabe nicht erlaubt.

---

## 3. Testarten

Alles sind **Unit Tests**. Es wird eine Methode isoliert geprüft, nicht das ganze Spiel.

| Art | Wo | Wozu |
| --- | --- | --- |
| Einfacher `@Test` | `DummyTest` | Prüfen, ob JUnit und AssertJ überhaupt laufen |
| Parameterized Test | `TicTacToeMainTest` | Viele Boards, eine Testmethode |
| `@Test` / Parameterized | `TicTacToeMainTest`, `GreedyPlayerTest`, `HumanPlayerTest`, `StoneTest` | `play`, `toString`, Spieler, `Stone` |

Parameterized heisst: eine Methode, mehrere Datensätze. Die Daten kommen aus `boards()` über `@MethodSource` oder direkt aus `@CsvSource` / `@ValueSource`.

Die `play`-Tests laufen ein ganzes Spiel mit zwei Spielern durch. Die Spieler sind kleine Lambdas mit festen Zügen (kein Mocking).

---

## 4. Tools

| Tool | Version / Hinweis | Wozu |
| --- | --- | --- |
| Java | 25 (Temurin) | Sprache, in Gradle und CI gleich |
| JUnit Jupiter | 6.1.3 | Tests schreiben und ausführen |
| AssertJ | 3.27.7 | Lesbare Assertions (`assertThat(...).isEqualTo(...)`) |
| Gradle | Wrapper im Repo | `assemble` und `test` |
| GitHub Actions | `.github/workflows/ci.yml` | Automatisch bauen und testen |
| JaCoCo | Gradle-Plugin | Code Coverage |
| PIT | 1.30.0 (Gradle-Plugin 1.19.0, junit5-Plugin 1.2.3) | Mutation Testing (siehe Abschnitt 10) |

Kein Mockito.

Tests lokal:

```bat
.\gradlew.bat test
```

---

## 5. Aufbau der Tests

Dateien unter `src/test/java/ch/bbw/m450/tictactoe/`:

**`DummyTest`**  
Zwei Mini-Tests. Einmal JUnit `assertTrue(true)`, einmal AssertJ `assertThat(true).isTrue()`. Nur zum Ausprobieren, nicht fürs Spiel.

**`BoardFixtures`**  
Feste Boards als Strings:

- `TOP_ROW_X` = `XXX......`
- `MID_COL_X` = `.X..X..X.`
- `DIAG_X` = `X...X...X`
- `NO_WIN` = `XOX.OX...`
- `TOP_ROW_O` = `OOO......`
- dazu `EMPTY`, `MID_ROW_O`, `BOTTOM_ROW_X`, `LEFT_COL_O`, `RIGHT_COL_O`, `ANTI_DIAG_O`, `FULL_DRAW` (siehe Abschnitt 7)

**`BoardTestHelper`**  
- `toBoard(String)` wandelt 9 Zeichen in `Stone[]` um
- `isWin(String, Stone)` ruft danach `TicTacToeMain.isWin` auf  
Falsche Länge oder unbekannte Zeichen werfen eine Exception.

**`TicTacToeMainTest`**  
Parameterized Test für `isWin` (Layout, Farbe und erwartetes Ergebnis aus `boards()`). Dazu Tests für `toString` und `play`.

**`GreedyPlayerTest`**, **`HumanPlayerTest`**, **`StoneTest`**  
Tests für die zwei Spieler und `Stone.opponent()`.

So bleibt das Board-Layout an einem Ort. Die eigentliche Logik bleibt in `TicTacToeMain`.

---

## 6. Given-When-Then

Wir benennen Tests so: `given..._when..._then...`

Ablauf:

1. **Given:** Ausgangslage (Board, Farbe)
2. **When:** Aufruf von `isWin`
3. **Then:** `true` oder `false`

Beispiel Dummy:

- Given: `true`
- When: `assertThat(true).isTrue()`
- Then: Test ist grün

Beispiel `isWin`:

- Given: Board `XXX......` (obere Reihe X)
- When: `isWin` mit `CROSS`
- Then: `true`

Im Parameterized Test heisst die Methode `givenBoard_whenIsWin_thenExpected`. Die einzelnen Boards stehen in der Tabelle unten.

---

## 7. Testfälle für `isWin`

Quelle: `BoardFixtures` und `TicTacToeMainTest.boards()`. Nur diese Boards, keine erfundenen Extra-Fälle.

| Nr | Fixture | Board | Farbe | Erwartet | Kurz |
| --- | --- | --- | --- | --- | --- |
| 1 | `TOP_ROW_X` | `XXX......` | CROSS | true | X gewinnt oben |
| 2 | `MID_COL_X` | `.X..X..X.` | CROSS | true | X gewinnt mittlere Spalte |
| 3 | `DIAG_X` | `X...X...X` | CROSS | true | X gewinnt Hauptdiagonale |
| 4 | `TOP_ROW_O` | `OOO......` | CIRCLE | true | O gewinnt oben |
| 5 | `NO_WIN` | `XOX.OX...` | CROSS | false | kein Gewinn für X |
| 6 | `NO_WIN` | `XOX.OX...` | CIRCLE | false | kein Gewinn für O |
| 7 | `TOP_ROW_O` | `OOO......` | CROSS | false | O hat gewonnen, X nicht |
| 8 | `MID_ROW_O` | `...OOO...` | CIRCLE | true | O gewinnt mittlere Reihe |
| 9 | `BOTTOM_ROW_X` | `......XXX` | CROSS | true | X gewinnt unten |
| 10 | `LEFT_COL_O` | `O..O..O..` | CIRCLE | true | O gewinnt linke Spalte |
| 11 | `RIGHT_COL_O` | `..O..O..O` | CIRCLE | true | O gewinnt rechte Spalte |
| 12 | `ANTI_DIAG_O` | `..O.O.O..` | CIRCLE | true | O gewinnt Gegendiagonale |
| 13 | `EMPTY` | `.........` | CROSS | false | leeres Board |
| 14 | - | `XX.......` | CROSS | false | nur zwei in der Reihe |
| 15 | - | `......OO.` | CIRCLE | false | nur zwei in der Reihe |
| 16 | `FULL_DRAW` | `XOXXOOOXX` | CROSS | false | Unentschieden |
| 17 | `FULL_DRAW` | `XOXXOOOXX` | CIRCLE | false | Unentschieden |

Zu Fall 1 ausführlich:

- **Given:** Board `XXX......`
- **When:** `isWin(board, CROSS)`
- **Then:** `true`

Zu Fall 5 und 6:

- **Given:** Board `XOX.OX...`
- **When:** `isWin` einmal mit CROSS, einmal mit CIRCLE
- **Then:** beide `false`

Fall 14 und 15 kamen dazu, weil JaCoCo dort einen Branch als nicht getestet angezeigt hat.

---

## 8. CI

Datei: `.github/workflows/ci.yml`

Läuft bei `push` und `pull_request`.

Zwei Jobs, nacheinander:

1. **Build** (`./gradlew assemble --no-daemon`)
2. **Test** (`./gradlew test --no-daemon`), nur wenn Build durch ist (`needs: build`)

Beide Jobs:

- Ubuntu
- Checkout
- JDK 25 (Temurin)
- Gradle Setup

Nach den Tests werden die Reports hochgeladen (`build/reports/tests/test/`), auch wenn Tests rot sind (`if: always()`).

Zusätzlich läuft der Job **Mutation tests** (`./gradlew pitest --no-daemon`) parallel zum Test-Job. Der PIT-Report wird als Artefakt `pitest-report` hochgeladen.

Lokal und CI nutzen denselben Gradle-Wrapper. Java-Version ist in `build.gradle` und in der CI gleich (25).

---

## 9. Offene Punkte / später

| Thema | Status | Kurz |
| --- | --- | --- |
| JaCoCo, Ziel 90 % Line Coverage | erledigt | 93 % Lines, 96.8 % Instructions, 100 % Branches |
| PIT (Mutation Testing) | erledigt | siehe Abschnitt 10 |
| HumanPlayer / stdin | erledigt | mit `System.setIn`, ohne Pioneer |
| Perfect Player | geplant | gibt es noch nicht |
| Ganze Spielschleife `play` | erledigt | mit Lambda-Spielern |
| `main(...)` | offen | siehe Abschnitt 2 |
| Mockito | nicht vorgesehen | Mocking ist nicht erlaubt |

---

## 10. Mutation Testing (PIT)

PIT ändert den Code absichtlich (z.B. `==` zu `!=`, `println` entfernt) und schaut, ob ein Test rot wird. Wird kein Test rot, hat der Mutant "überlebt" und die Tests sind an der Stelle zu schwach.

Lokal:

```bat
.\gradlew.bat pitest
```

Report: `build/reports/pitest/index.html` (HTML und XML)

Konfiguration in `build.gradle`: alle Klassen in `ch.bbw.m450.tictactoe.*`, JUnit-5-Plugin, keine Zeitstempel-Ordner.

| Stand | Mutanten | getötet | Mutation Score |
| --- | --- | --- | --- |
| vorher (nur `isWin`-Tests) | 57 | 14 | 25 % |
| nachher | 57 | 57 | 100 % |

Überlebt hatten am Schluss noch: entfernte `println`-Aufrufe in `play` und `HumanPlayer` und eine vertauschte X/O-Bedingung in `toString`. Dafür prüfen die Tests jetzt auch die Konsolenausgabe (`System.setOut`) und `toString` mit nur X bzw. nur O.
