package quickchat;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

/** Preserves Rego's per-batch message entry and adds the Part 3 reports. */
final class QuickChat {
    private final Scanner input;
    private final MessageHistory history;
    private final String sender;

    QuickChat(Scanner input, MessageHistory history, String sender) {
        this.input = input;
        this.history = history;
        this.sender = sender;
    }

    void run() {
        System.out.println("\nWelcome to QuickChat.");
        if (history.storageError() != null) {
            System.out.println(history.storageError());
        }

        while (true) {
            System.out.println("\nQUICKCHAT MENU");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            String choice = read("Choose an option: ");
            if (choice == null) {
                return;
            }
            switch (choice) {
                case "1" -> sendMessages();
                case "2" -> System.out.println(history.sentReport());
                case "3" -> {
                    System.out.println("Total messages sent: " + history.sentCount());
                    System.out.println("Thank you for using QuickChat.");
                    return;
                }
                case "4" -> storedMenu();
                default -> System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }
    }

    private int readMessageLimit() {
        while (true) {
            String value = read("How many messages would you like to enter? ");
            if (value == null) {
                return -1;
            }
            try {
                int count = Integer.parseInt(value);
                if (count > 0) {
                    return count;
                }
            } catch (NumberFormatException ignored) {
                // Both invalid number cases return to this same prompt.
            }
            System.out.println("Please enter a whole number greater than zero.");
        }
    }

    private void sendMessages() {
        int maximumMessages = readMessageLimit();
        for (int i = 1; i <= maximumMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + maximumMessages + " ---");
            String recipient = readRecipient();
            if (recipient == null) {
                return;
            }
            String text = readMessageText();
            if (text == null) {
                return;
            }
            Message message = history.compose(sender, recipient, text);
            System.out.println("Message ID: " + message.getMessageID());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("1. Send Message\n2. Disregard Message\n3. Store Message to send later");
            if (!recordChoice(message)) {
                return;
            }
        }
    }

    private boolean recordChoice(Message message) {
        while (true) {
            String choice = read("Enter your choice: ");
            if (choice == null) {
                return false;
            }
            MessageHistory.Action action = switch (choice) {
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
                history.record(message, action);
                System.out.println(switch (action) {
                    case SENT -> "Message successfully sent.";
                    case DISREGARDED -> "Message disregarded.";
                    case STORED -> "Message successfully stored in messages.json.";
                });
                if (action == MessageHistory.Action.SENT) {
                    message.printMessageDetails();
                }
                return true;
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Could not record the message: " + e.getMessage());
                return false;
            }
        }
    }

    private String readRecipient() {
        while (true) {
            String number = read("Enter recipient cellphone number: ");
            if (number == null) {
                return null;
            }
            if (Message.isRecipientCell(number)) {
                System.out.println("Cell phone number successfully captured.");
                return number;
            }
            System.out.println("Enter a valid South African cellphone number, for example +27716774402.");
        }
    }

    private String readMessageText() {
        while (true) {
            System.out.print("Enter your message: ");
            if (!input.hasNextLine()) {
                return null;
            }
            String text = input.nextLine();
            if (!text.isBlank() && text.length() <= 250) {
                System.out.println("Message ready to send.");
                return text;
            }
            if (text.isBlank()) {
                System.out.println("Message cannot be empty.");
            } else {
                System.out.println("Message exceeds 250 characters by " + (text.length() - 250)
                        + "; please reduce the size.");
            }
        }
    }

    private void storedMenu() {
        while (true) {
            System.out.println("\nSTORED MESSAGES");
            System.out.println("1. Show senders and recipients");
            System.out.println("2. Show longest stored message");
            System.out.println("3. Find a message by ID");
            System.out.println("4. Find stored messages for a recipient");
            System.out.println("5. Delete a stored message by hash");
            System.out.println("6. Show stored messages report");
            System.out.println("7. Find sent and stored messages for a recipient");
            System.out.println("8. Back to QuickChat");
            String choice = read("Choose an option: ");
            if (choice == null || choice.equals("8")) {
                return;
            }
            switch (choice) {
                case "1" -> System.out.println(history.storedSendersAndRecipients());
                case "2" -> System.out.println(history.longestStored().map(Message::details)
                        .orElse("No stored messages are available."));
                case "3" -> searchById();
                case "4" -> searchRecipient(false);
                case "5" -> deleteByHash();
                case "6" -> System.out.println(history.storedReport());
                case "7" -> searchRecipient(true);
                default -> System.out.println("Invalid option. Choose a number from 1 to 8.");
            }
        }
    }

    private void searchById() {
        String id = read("Enter the message ID: ");
        if (id != null) {
            System.out.println(history.findById(id).map(message -> "Recipient: " + message.getRecipient()
                    + "\nMessage: " + message.getMessageText()).orElse("No message found."));
        }
    }

    private void searchRecipient(boolean includeSent) {
        String recipient = read("Enter the recipient's cellphone number: ");
        if (recipient == null) {
            return;
        }
        Message[] messages = history.findForRecipient(recipient, includeSent);
        if (messages.length == 0) {
            System.out.println("No messages found for that recipient.");
        }
        for (Message message : messages) {
            System.out.println("Message: " + message.getMessageText());
        }
    }

    private void deleteByHash() {
        String hash = read("Enter the message hash: ");
        if (hash == null) {
            return;
        }
        try {
            Optional<Message> deleted = history.deleteStored(hash);
            System.out.println(deleted.map(message -> "Message: \"" + message.getMessageText()
                    + "\" successfully deleted.").orElse("No stored message has that hash."));
        } catch (IOException e) {
            System.out.println("Could not delete the message: " + e.getMessage());
        }
    }

    private String read(String prompt) {
        System.out.print(prompt);
        return input.hasNextLine() ? input.nextLine().trim() : null;
    }
}
