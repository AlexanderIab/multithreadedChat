package multithreadedChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Alexander Iablonski
 * */
public class Connection implements AutoCloseable {
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final UUID clientId;

    public Connection(Socket socket) throws IOException {
        this.clientId = UUID.randomUUID();
        this.socket = socket;
        output = new ObjectOutputStream(this.socket.getOutputStream());
        input = new ObjectInputStream(this.socket.getInputStream());
    }

    public UUID getClientId() {
        return clientId;
    }

    public void sendMessage(SimpleMessage message) throws IOException {
        message.setDateTime();
        output.writeObject(message);
        output.flush();
    }

    public SimpleMessage readMessage() throws IOException, ClassNotFoundException {
        return (SimpleMessage) input.readObject();
    }

    @Override
    public void close() throws IOException {
        input.close();
        output.close();
        socket.close();
    }
}
