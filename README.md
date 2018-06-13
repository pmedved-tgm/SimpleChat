# SimpleChat
Dieses Repo beinhaltet  einen simplen Chat welcher in Java geschrieben wurde

## Technologien
* Sockets
* Threading
* JavaFX
* Gradle

## Client
Der Client hat eine Oberfläche auf der er die Möglichkeit hat mit anderen zu
chatten. Sobald der User seine erste Nachricht schickt hat er die Möglichkeit
seinen Benutzernamen zu setzten.
Falls der Benutzer sich auslogt, schickt er eine Meldung zum Server, dieser
entfernt ihn dann aus der Liste.

## Server
Der Server dient als Komonikationsglied zwischen den einzelnen Clients.
Er nimmt Nachrichten entgegen und sendet diese an alle Clients.
Falls der Server sich schließt werden alle Clients disconected.

## Build
```gradle
 gradle build
```
```gradle
 gradle jar
```