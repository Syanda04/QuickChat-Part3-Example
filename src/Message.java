package quickchat;

import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/** One QuickChat message, including the fields used by the Part 2 JSON archive. */
public class Message {
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    @SerializedName(value = "messageText", alternate = {"message"})
    private final String messageText;
    private final String messageHash;
    private final String sender;
    private String status;

    public Message() {
        this(newId(), 0, "", null, null);
    }

    public Message(int messageNumber, String recipient, String messageText) {
        this(newId(), messageNumber, "", recipient, messageText);
    }

    public Message(String recipient, String messageText, int messageNumber) {
        this(newId(), messageNumber, "", recipient, messageText);
    }

    Message(String id, int number, String sender, String recipient, String text) {
        this.messageID = id;
        this.messageNumber = number;
        this.recipient = recipient;
        this.messageText = text;
        this.sender = sender;
        this.status = "Not processed";
        this.messageHash = createMessageHash();
    }

    private Message(Message source, String status) {
        this.messageID = source.messageID;
        this.messageNumber = source.messageNumber;
        this.recipient = source.recipient;
        this.messageText = source.messageText;
        this.messageHash = source.messageHash;
        this.sender = source.sender;
        this.status = status;
    }

    static String newId() {
        return String.format(Locale.ROOT, "%010d", ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("[0-9]{10}");
    }

    public boolean checkRecipientCell() {
        return isRecipientCell(recipient);
    }

    static boolean isRecipientCell(String number) {
        return number != null && (number.matches("\\+27[6-8][0-9]{8}")
                || number.matches("0[6-8][0-9]{8,9}"));
    }

    public String createMessageHash() {
        String trimmedMessage = messageText == null ? "" : messageText.trim();
        if (messageID == null || messageID.length() < 2) {
            return "";
        }
        String prefix = messageID.substring(0, 2) + ":" + messageNumber + ":";
        if (trimmedMessage.isEmpty()) {
            return prefix;
        }
        String[] words = trimmedMessage.split("\\s+");
        String firstWord = words[0].replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
        String lastWord = words[words.length - 1].replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
        return prefix + (firstWord + lastWord).toUpperCase(Locale.ROOT);
    }
    Message asStatus(String status) {
        return new Message(this, status);
    }

    void setStatus(String status) {
        this.status = status;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }

    public String getSender() {
        return sender == null ? "" : sender;
    }

    String details() {
        return "Message ID: " + messageID + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient + "\nMessage: " + messageText
                + "\nSender: " + (getSender().isBlank() ? "Unknown" : getSender())
                + "\nMessage Number: " + messageNumber + "\nStatus: " + status;
    }

    // Retain the public Part 2 console methods alongside the testable session.
    private static class DefaultSession {
        static final MessageHistory HISTORY = new MessageHistory(Path.of("messages.json"));
    }

    public static void startQuickChat(Scanner input) {
        startQuickChat(input, "");
    }

    public static void startQuickChat(Scanner input, String sender) {
        new QuickChat(input, DefaultSession.HISTORY, sender).run();
    }

    public void SentMessage(Scanner input) {
        System.out.println("1. Send Message\n2. Disregard Message\n3. Store Message to send later");
        while (input.hasNextLine()) {
            System.out.print("Enter your choice: ");
            MessageHistory.Action action = switch (input.nextLine().trim()) {
                case "1" -> MessageHistory.Action.SENT;
                case "2" -> MessageHistory.Action.DISREGARDED;
                case "3" -> MessageHistory.Action.STORED;
                default -> null;
            };
            if (action == null) {
                System.out.println("Please select 1, 2 or 3.");
                continue;
            }
            try {
                DefaultSession.HISTORY.record(this, action);
                System.out.println(switch (action) {
                    case SENT -> "Message successfully sent.";
                    case DISREGARDED -> "Message disregarded.";
                    case STORED -> "Message successfully stored in messages.json.";
                });
                if (action == MessageHistory.Action.SENT) {
                    printMessageDetails();
                }
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Could not record the message: " + e.getMessage());
            }
            return;
        }
    }

    public void printMessageDetails() {
        System.out.println(details());
    }

    public static void printMessages() {
        System.out.println(DefaultSession.HISTORY.sentReport());
    }

    public static int returnTotalMessages() {
        return DefaultSession.HISTORY.sentCount();
    }

    public static int returnTotalMessagess() {
        return returnTotalMessages();
    }

    public void storeMessage() {
        try {
            DefaultSession.HISTORY.record(this, MessageHistory.Action.STORED);
            System.out.println("Message successfully stored in messages.json.");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not store message: " + e.getMessage());
        }
    }
}
