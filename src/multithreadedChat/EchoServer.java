package multithreadedChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alexander Iablonski
 * */
public class EchoServer {
    private final int port;
    private final Map<UUID, Connection> connectionList;
    private final ArrayBlockingQueue<SimpleMessage> simpleMessages;
    private UUID clientId;

    public EchoServer(int port) {
        this.port = port;
        this.connectionList = new ConcurrentHashMap<>();
        this.simpleMessages = new ArrayBlockingQueue<>(100);
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started...");
        new Thread(new ServerWriteThread()).start();
        while (true) {
            Socket socket = serverSocket.accept();
            Connection connection = new Connection(socket);
            connectionList.put(connection.getClientId(), connection);
            new Thread(new ServerReadThread(connection)).start();
        }
    }

    class ServerReadThread implements Runnable {
        private final Connection connection;

        public ServerReadThread(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    SimpleMessage message = connection.readMessage();
                    if(message.getText().equalsIgnoreCase("Exit")){
                        connectionList.remove(connection.getClientId());
                        break;
                    }
                    clientId = connection.getClientId();
                    simpleMessages.put(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException | ClassNotFoundException e) {
                connectionList.remove(connection.getClientId());
                System.out.println("ServerReadThread error");
            }
        }
    }

    public class ServerWriteThread implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    SimpleMessage message = simpleMessages.take();
                    for (UUID uuid : connectionList.keySet()) {
                        if(!uuid.equals(clientId)) connectionList.get(uuid).sendMessage(message);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                System.out.println("ServerWriteThread error");
            }
        }
    }


    public static void main(String[] args) {
        int port = 8090;
        EchoServer messageServer = new EchoServer(port);
        try {
            messageServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
