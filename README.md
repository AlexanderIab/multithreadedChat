# Multi-threaded chat

The client establishes a connection with the server and does not break it after each message is sent

The client (except the main one) has two threads:

Sender of messages (user types a message into the console, the thread sends it to the server)

Message recipient
Client and server pass instances of the SimpleMessage class to each other

Server.
Threads:

A separate thread is allocated for each connection, which receives messages from the client and adds them to the blocking queue

One thread - the sender of messages: picks up a message from the blocking queue and sends messages to all clients except the sender
