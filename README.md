# QuickChat Part 3 Example

This complete Java with Ant example shows how the PROG5121 registration, login, messaging and Part 3 reporting requirements fit together. It adapts the supplied Rego Parts 1 and 2 project, retaining the original registration and login class names and message methods in a neutral `quickchat` package.

Start with the [walkthrough](docs/WALKTHROUGH.md) to see the expected behavior, and use the [requirements checklist](docs/REQUIREMENTS.md) to find the implementation and tests for each feature.

## Build and run

Use JDK 17 or newer and Apache Ant. From the project directory, run

```sh
ant clean test jar
java -jar dist/QuickChatPart3.jar
```

NetBeans can open this folder as an existing Java with Ant project. The first build downloads the pinned Gson, JUnit and Hamcrest libraries from Maven Central. Keep `dist/lib` beside `QuickChatPart3.jar` when moving the runnable application.

For a ready-made stored-message example, use `examples/stored-messages.json` as described in the walkthrough. The app starts empty unless you add messages or copy that file into a fresh working directory.

## Using QuickChat

Register a user and sign in. The existing retry prompts remain. Select **1. Send Messages**, enter the number of messages in that batch, and choose to send, disregard or store each message. Messages must contain text and be at most 250 characters. Sending is simulated in the console. **2. Show recently sent messages** now prints the full sent report. **3. Quit** shows the current session's sent count.

**4. Stored Messages** provides all Part 3 operations

1. Show the sender and recipient of every stored message.
2. Display the longest stored message.
3. Search all message arrays by ID and show the recipient and text.
4. Find stored messages for a particular recipient.
5. Delete a stored message by its hash and save the change.
6. Display a full stored-message report.
7. Search sent and stored messages together for a recipient.
8. Return to the main menu.

The five arrays are populated from user input and the JSON archive. The test fixtures are confined to the tests. Message IDs and hashes are rebuilt from the same ordered message rows so a deletion also removes the corresponding index entries.

Stored messages are loaded from `messages.json` in the current working directory and preserved between runs. The original Part 2 `messageText` JSON field remains supported, along with the alternate `message` field. Older entries with no sender display **Unknown**. New entries use the registered user's cellphone number as sender. Sent and disregarded messages belong to the current session. Restarting does not require entering a new message before viewing stored data.

Archive changes are written atomically and use a lock while rereading and updating the file. If the existing JSON is damaged, QuickChat reports the problem and preserves the file. Tests run in a separate working directory to protect user data.

## Tests and walkthrough

`ant test` runs the original registration, login and message tests plus the Part 3 acceptance fixtures, JSON compatibility, persistent deletion, concurrent-session and console-flow tests. The original generated assertion stubs were replaced with real JUnit assertions and the test setup now runs before each test. GitHub Actions uses `TestJava.yml` to run `ant clean test jar` on Windows and Ubuntu.

The five messages from page 18 of the assignment are used in the acceptance tests. Message 4 preserves the table's local number and its space before the exclamation mark.

For the student's walkthrough

1. Register a demo user, show an incorrect login followed by a successful retry, and open QuickChat.
2. Select a batch of messages. Send one, store two for one recipient, and disregard another. Demonstrate recipient and length validation.
3. Show the sent report and all options under **Stored Messages**, copying an ID and hash from the reports for the searches and deletion.
4. Restart, register and sign in again, and show that the remaining stored messages reload while the deleted message stays removed.
5. Explain the arrays, JSON loading, searching and deletion, then show the automated test results.

The student must record their own-voice walkthrough and complete the required Arc submission with the repository link and assignment cover/rubric details.

## Sources and assistance

[1] Google, "Gson User Guide," Array Examples. https://google.github.io/gson/UserGuide.html#array-examples

[2] Oracle, "Pattern," Java SE 17 API documentation. https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html

The example is adapted from the supplied Rego Parts 1 and 2 source and the completed Part 3 continuation. OpenAI Codex assisted with implementation, adaptation, documentation and testing.
