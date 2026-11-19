# Working example and recording guide

## Start the application

Open the project in NetBeans as a Java with Ant project, or run these commands from the project folder with JDK 17 and Apache Ant installed.

```sh
ant clean test jar
java -jar dist/QuickChatPart3.jar
```

The test results should have no failures or errors. The build produces `dist/QuickChatPart3.jar` and its Gson dependency in `dist/lib`.

## Register and sign in

Use these sample application details. They are demonstration data and have no connection to GitHub access.

| Field | Value |
| --- | --- |
| First name | Example |
| Surname | User |
| Username | kyl_1 |
| Password | Ch&&sec@ke99! |
| Cellphone | +27838968976 |

Try an invalid username such as `kyle!!!!!!!`, a weak password such as `password`, and an invalid cellphone such as `08966553` before entering the valid values. For sign-in, demonstrate one failed attempt followed by the correct username and password. Expect the welcome message with the registered first name and surname.

## Enter the five assignment messages

On the main menu select **1. Send Messages** and enter **5** for the batch size. Enter each row, then select its action number.

| Number | Recipient | Message | Action |
| --- | --- | --- | --- |
| 1 | +27834557896 | Did you get the cake? | 1, Send |
| 2 | +27838884567 | Where are you? You are late! I have asked you to be on time. | 3, Store |
| 3 | +27834484567 | Yohoooo, I am at your gate. | 2, Disregard |
| 4 | 0838884567 | It is dinner time ! | 1, Send |
| 5 | +27838884567 | Ok, I am leaving without you. | 3, Store |

The app generates each ID and hash. Copy the displayed values when you need to search or delete; new messages have different generated IDs on each run.

Select **2. Show recently sent messages**. Expect the cake and dinner messages, with their IDs, hashes, recipients, sender and status. The sent count is **2**.

## Show every Part 3 feature

Select **4. Stored Messages** and use the following submenu options.

| Option | Demonstration | Expected result |
| --- | --- | --- |
| 1 | Show senders and recipients | Both stored messages show sender +27838968976 and recipient +27838884567 |
| 2 | Show longest stored message | The message beginning "Where are you?" |
| 3 | Paste message 4's generated ID | Local recipient 0838884567 and "It is dinner time !" |
| 4 | Enter +27838884567 | Both stored messages |
| 6 | Show full stored report | Full details of the late and leaving messages |
| 7 | Enter +27838884567 | Both stored messages; a sent message to this recipient would also be included |
| 5 | Paste the late message's generated hash | A successful deletion message |
| 6 | Show the report again | Only the leaving message remains |
| 8 | Return to the main menu | The four main options appear |

Choose **3. Quit**, run the application again, register and sign in, then open the stored report. The remaining leaving message reloads and the deleted late message stays removed. The new session's sent count starts at zero.

## Ready-made JSON example

For a fresh demonstration, make a separate empty working directory and copy `examples/stored-messages.json` into it with the name `messages.json`. Start the runnable JAR with that directory as the working directory. Keep existing user data separate.

This sample contains only the two stored messages, with fixed IDs for easy lookup. It does not preload sent or disregarded messages.

| Message | Fixed ID | Hash |
| --- | --- | --- |
| Late | 2000000002 | 20:2:WHERETIME |
| Leaving | 2000000005 | 20:5:OKYOU |

## Explain the code and tests

Show how `MessageHistory` populates the three message arrays and the ID/hash arrays from entered and loaded messages. Explain how `MessageArchive` reads JSON, how search operations use the arrays, and how deleting a stored row also removes its ID/hash and updates the file. Show the original tests, the five Part 3 fixtures, and a successful GitHub Actions run on Windows and Ubuntu.

## Complete the submission

Record the walkthrough in the student's own voice and upload it using the visibility required by the assignment. Add the video link to the submission. Complete the required cover details, include the rubric pages, and submit the repository link through Arc. Do not claim that these personal recording and submission steps are complete before doing them.
