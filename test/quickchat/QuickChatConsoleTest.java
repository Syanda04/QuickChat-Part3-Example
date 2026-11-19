package quickchat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class QuickChatConsoleTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test(timeout = 5000)
    public void preservesBatchEntryAndValidatesInputBeforeReporting() throws Exception {
        MessageHistory history = history();
        String output = run(history, String.join("\n", "1", "wrong", "0", "2",
                "12345", "+27838884567", "", "x".repeat(251), "Sent from Rego", "9", "1",
                "+27838884567", "Saved from Rego", "3", "2", "4", "1", "2", "4",
                "+27838884567", "6", "7", "+27838884567", "8", "3") + "\n");
        assertEquals(1, history.sentCount());
        assertEquals(2, history.enteredCount());
        assertEquals(1, history.storedMessages().length);
        assertEquals(1, history.sentMessages()[0].getMessageNumber());
        assertEquals(2, history.storedMessages()[0].getMessageNumber());
        assertTrue(output.contains("Message cannot be empty."));
        assertTrue(output.contains("Message exceeds 250 characters by 1"));
        assertTrue(output.contains("Please select 1, 2 or 3."));
        assertTrue(output.contains("Sender: +27716774402 | Recipient: +27838884567"));
        assertTrue(output.contains("Message: Saved from Rego"));
        assertTrue(output.contains("Message: Sent from Rego"));
        assertTrue(output.contains("Total messages sent: 1"));
    }

    @Test(timeout = 5000)
    public void menuSearchAndDeletionPersistAcrossRestart() throws Exception {
        Path path = temporaryFolder.newFolder().toPath().resolve("messages.json");
        MessageHistory history = new MessageHistory(path);
        Message stored = history.compose("+27716774402", "+27838884567", "Delete this stored message.");
        history.record(stored, MessageHistory.Action.STORED);
        String output = run(history, "4\n3\n" + stored.getMessageID() + "\n5\n"
                + stored.getMessageHash() + "\n6\n8\n3\n");
        assertTrue(output.contains("Recipient: +27838884567\nMessage: Delete this stored message."));
        assertTrue(output.contains("successfully deleted."));
        assertTrue(output.contains("No stored messages are available."));
        assertEquals(0, new MessageHistory(path).storedMessages().length);
    }

    @Test(timeout = 5000)
    public void endOfInputReturnsFromMenusAndFailedLogin() throws Exception {
        MessageHistory history = history();
        run(history, "1\n1\n+27838884567\nMessage without an action\n");
        assertEquals(0, history.enteredCount());
        run(history, "4\n3\n");
        RegistrationClassOne user = new RegistrationClassOne();
        user.username = "r_16";
        user.password = "Password1!";
        assertFalse(LoginUserOne.loginUser(user, new Scanner("incorrect\nwrong\n")));
    }

    private MessageHistory history() throws Exception {
        return new MessageHistory(temporaryFolder.newFolder().toPath().resolve("messages.json"));
    }

    private String run(MessageHistory history, String input) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream console = new PrintStream(output, true, StandardCharsets.UTF_8);
                Scanner scanner = new Scanner(input)) {
            System.setOut(console);
            new QuickChat(scanner, history, "+27716774402").run();
        } finally {
            System.setOut(original);
        }
        return output.toString(StandardCharsets.UTF_8);
    }
}
