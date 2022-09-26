package multithreadedChat;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Alexander Iablonski
 * */
public class Client {

    private final int port;
    private final String ip;
    private final Scanner scanner;
    private Connection connection;

    public Client(int port, String ip) {
        this.port = port;
        this.ip = ip;
        scanner = new Scanner(System.in);
    }

    public void start() throws Exception {
        connection = new Connection(new Socket(ip, port));
        System.out.println("Enter your name");
        String name = scanner.nextLine();
        new Thread(() -> {
            try {
                while (true) {
                    String messageText = scanner.nextLine();
                    if (messageText.equalsIgnoreCase("Exit")) {
                        connection.close();
                        break;
                    }
                    connection.sendMessage(SimpleMessage.getMessage(name, messageText));
                }
            } catch (IOException e) {
                System.out.println("ClientWriteThread Error");
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    SimpleMessage fromServer = connection.readMessage();
                    System.out.println(fromServer);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("ClientReadThread Error");

            }
        }).start();
    }

    public static void main(String[] args) {
        int port = 8090;
        String ip = "127.0.0.1";

        try {
            new Client(port, ip).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
