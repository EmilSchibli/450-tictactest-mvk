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

- Spielschleife `play(...)`
- `HumanPlayer` (Eingabe über Tastatur / stdin)
- `GreedyPlayer` und ein späterer Perfect Player
- `toString` (Ausgabe auf der Konsole)
- ungültige Züge (Feld schon belegt, Index ausserhalb 0–8)
- Unentschieden über 9 Runden

Kein Mocking. Mockito ist in der Aufgabe nicht erlaubt.

---

## 3. Testarten

Alles sind **Unit Tests**. Es wird eine Methode isoliert geprüft, nicht das ganze Spiel.

| Art | Wo | Wozu |
| --- | --- | --- |
| Einfacher `@Test` | `DummyTest` | Prüfen, ob JUnit und AssertJ überhaupt laufen |
| Parameterized Test | `TicTacToeMainTest` | Viele Boards, eine Testmethode |

Parameterized heisst: eine Methode, mehrere Datensätze. Die Daten kommen aus `boards()` über `@MethodSource`.

Integration Tests (z.B. zwei Spieler + ganze `play`-Schleife) gibt es noch nicht.

---

## 4. Tools

| Tool | Version / Hinweis | Wozu |
| --- | --- | --- |
| Java | 25 (Temurin) | Sprache, in Gradle und CI gleich |
| JUnit Jupiter | 6.1.3 | Tests schreiben und ausführen |
| AssertJ | 3.27.7 | Lesbare Assertions (`assertThat(...).isEqualTo(...)`) |
| Gradle | Wrapper im Repo | `assemble` und `test` |
| GitHub Actions | `.github/workflows/ci.yml` | Automatisch bauen und testen |

Kein Mockito.  
JaCoCo und PIT stehen in der Aufgabe, sind aber noch nicht eingerichtet (siehe Abschnitt 9).

Tests lokal:

```bat
.\gradlew.bat test
```

---

## 5. Aufbau der Tests

Vier Dateien unter `src/test/java/ch/bbw/m450/tictactoe/`:

**`DummyTest`**  
Zwei Mini-Tests. Einmal JUnit `assertTrue(true)`, einmal AssertJ `assertThat(true).isTrue()`. Nur zum Ausprobieren, nicht fürs Spiel.

**`BoardFixtures`**  
Feste Boards als Strings:

- `TOP_ROW_X` = `XXX......`
- `MID_COL_X` = `.X..X..X.`
- `DIAG_X` = `X...X...X`
- `NO_WIN` = `XOX.OX...`
- `TOP_ROW_O` = `OOO......`

**`BoardTestHelper`**  
- `toBoard(String)` wandelt 9 Zeichen in `Stone[]` um
- `isWin(String, Stone)` ruft danach `TicTacToeMain.isWin` auf  
Falsche Länge oder unbekannte Zeichen werfen eine Exception.

**`TicTacToeMainTest`**  
Ein Parameterized Test. Nimmt Layout, Farbe und erwartetes Ergebnis aus `boards()`.

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

Zu Fall 1 ausführlich:

- **Given:** Board `XXX......`
- **When:** `isWin(board, CROSS)`
- **Then:** `true`

Zu Fall 5 und 6:

- **Given:** Board `XOX.OX...`
- **When:** `isWin` einmal mit CROSS, einmal mit CIRCLE
- **Then:** beide `false`

Was noch fehlt (nicht in den Fixtures): untere Reihe, rechte Spalte, Gegendiagonale, volles Unentschieden-Board. Das kann man später ergänzen.

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

Lokal und CI nutzen denselben Gradle-Wrapper. Java-Version ist in `build.gradle` und in der CI gleich (25).

---

## 9. Offene Punkte / später

Das gehört zur Aufgabe, ist aber **noch nicht gemacht**. Keine Coverage-Zahlen, weil JaCoCo fehlt.

| Thema | Status | Kurz |
| --- | --- | --- |
| JaCoCo, Ziel 90 % Line Coverage | geplant | Plugin fehlt in `build.gradle` |
| PIT (Mutation Testing) | geplant | noch nicht eingerichtet |
| HumanPlayer / stdin | geplant | z.B. mit Pioneer; Eingabe ist schwer zu testen |
| Perfect Player | geplant | `GreedyPlayer` ist da, Tests dafür nicht |
| Ganze Spielschleife `play` | geplant | wäre eher Integration Test |
| Mockito | nicht vorgesehen | Mocking ist nicht erlaubt |

Wenn JaCoCo und PIT drin sind, gehört die Auswertung ins Konzept nachgetragen. Bis dahin gilt: die sieben `isWin`-Fälle und die zwei Dummy-Tests laufen.
