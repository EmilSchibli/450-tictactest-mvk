# Web-Konsole

Das Spiel bleibt das Jar aus dem Release. Eine Web-Konsole startet dieses Jar und legt Ein- und Ausgabe in ein Terminal im Browser. Der Quellcode des Spiels wird dafür nicht angefasst und auf dem Server nicht gebaut.

## Werkzeug

[ttyd](https://github.com/tsl0922/ttyd) 1.7.7. Es ist ein einzelnes Programm, bringt xterm.js mit und spricht WebSocket mit einem Pseudo-Terminal. GoTTY wird kaum noch gepflegt. websocketd überträgt nur Zeilen und keine ANSI-Farben, das Brett würde ohne Extra-Seite falsch aussehen.

Ohne `-W` ist ttyd nur lesbar. Mit `-W` kommen Tastendrücke im Terminal an, also die Zahl und Enter.

## Start lokal

Jar und die beiden Dateien aus `tictactest-webconsole-X.Y.Z.zip` in einen Ordner legen. ttyd muss auf dem Rechner liegen (`ttyd` im Pfad). Java 25 auch.

```sh
sh start-webconsole.sh
```

Das Script liest die Version aus dem Dateinamen `tictactest-X.Y.Z.jar`. Die Nummer kommt vom Release-Tag, niemand schreibt sie von Hand in eine zweite Datei.

- Terminal: `http://127.0.0.1:7681`
- Version: `http://127.0.0.1:7682/version`

Das Frontend liegt daneben, aus `tictactest-frontend-X.Y.Z.zip`:

```sh
python3 -m http.server 8000
```

`frontend/config.js` sagt der Seite, wo Terminal und Version liegen. `app.js` enthält keine Adresse. Lokal steht dort `127.0.0.1`. Auf dem Server ersetzt man nur diese Datei.

Die Frontend-Version steht in `version.txt`. Im Git-Stand ist das `dev`. Der Release schreibt die Tag-Version in das Zip, nicht zurück ins Repository.

## Was beim Spielen passiert

Eine Verbindung startet einen Java-Prozess. Das Spiel ist Mensch gegen `GreedyPlayer`, so wie `main` es fest verdrahtet.

Nach Sieg oder Unentschieden beendet `play` den Prozess. ttyd zeigt die Verbindung als beendet. Neu laden startet ein neues Jar und damit eine neue Partie.

`abc` ist keine Zahl. `HumanPlayer` wirft `NumberFormatException`, der Prozess endet. Eine Zahl ausserhalb von 0 bis 8 oder ein besetztes Feld wirft `IllegalStateException`, ebenfalls Ende. Auch dann: Seite neu laden.

Zwei Tabs sind zwei WebSocket-Verbindungen und zwei Java-Prozesse. Sie sehen nicht dasselbe Brett. Das Script setzt `-Xmx64m`. Trotzdem kostet jede JVM Speicher ausserhalb vom Heap. Auf einem Server mit 1 GB RAM sind nur wenige gleichzeitige Partien drin. Viele offene Tabs können den Rechner vollmachen.

## Sicherheit

ttyd führt genau `java -Xmx64m -jar tictactest-X.Y.Z.jar` aus. Kein `bash`, kein `sh`, kein Login.

Würde man stattdessen eine Shell starten, hätte jeder mit der URL eine Kommandozeile auf dem Server. Von dort kann man Dateien lesen, Prozesse starten und weiter ins Netz gehen. Die URL wäre dann ein offener Zugang.

Lokal bindet der Start an `127.0.0.1`. Für später setzt man `TICTACTEST_BIND`. Wer die URL kennt, darf spielen. Das ist für diese lokale Übung so. Auf einem öffentlichen Server braucht es zusätzlich eine Grenze, zum Beispiel ttyd `-c benutzer:passwort` oder eine Firewall. Das Script lässt das Jar nicht weg und öffnet keine Shell.

## Was zum Release gehört

Das Jar und das Frontend-Zip gehören zum Release. Dieselbe Versionsnummer liegt auf beiden. Ein Seiten-Fix kann später nur das Frontend-Zip austauschen, ohne das Spiel neu zu bauen. Man rollt auf eine bekannte Zip zurück, nicht auf irgendeinen Stand im Git.

`start-webconsole.sh` und `version-server.py` liegen im Zip `tictactest-webconsole-X.Y.Z.zip`, weil man die Assets sonst nicht starten kann, ohne etwas aus dem Repository zu holen. ttyd selbst ist Infrastruktur: dasselbe Programm für jede Version, installiert auf der Maschine, nicht von unserem Build erzeugt.
