package multithreadedChat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Alexander Iablonski
 * */
public class SimpleMessage implements Serializable {
    private final String sender;
    private final String text;
    private LocalDateTime dateTime;

    public SimpleMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public void setDateTime() {
        dateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + sender + "] " + text + " (" + dateTime + ")";
    }

    public static SimpleMessage getMessage(String sender, String text) {
        return new SimpleMessage(sender, text);
    }

    public String getText() {
        return text;
    }
}
