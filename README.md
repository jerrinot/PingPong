PingPong
========

Network PingPong experiment. The server listens on TCP port and expects client to send 16 bytes. Then the server sends the bytes back to the client and expects another 16 bytes.


Usage:

```
java -jar pingpong-1.0-SNAPSHOT.jar <nio-server, bio-server, netty-server>
```
to create a server.

```
java -jar pingpong-1.0-SNAPSHOT.jar client 500 localhost
```
To start a client with 500 connections
