# DevContainer

## Image

- Definition: `.devcontainer/Dockerfile`
- Basis: `azul/zulu-openjdk-alpine:25.0.4.1-25.36` (Alpine, Java 25 von Azul Zulu)
- Gradle 9.7.0 ist im Image installiert (gleiche Version wie der Wrapper, Checksumme wird geprüft)
- JUnit kommt über die Gradle-Dependencies in `build.gradle`
- Benutzer `dev` mit UID:GID `1000:1000`
- Extensions: Java Extension Pack und Gradle (`.devcontainer/devcontainer.json`)

## Docker lokal

Docker ist auf dem Schul-PC vom Systemadministrator gesperrt. Das Image wird deshalb nur in GitHub Actions gebaut und getestet.

Ohne Docker braucht man lokal Java 25:

```
./gradlew test
```

Unter Windows: `.\gradlew.bat test`
