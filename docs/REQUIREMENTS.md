# Requirements and evidence

This checklist maps the supplied PROG5121 POE requirements to the working example. It is a guide to the implementation; marks and submission acceptance are determined by the assessor.

| Requirement | Implementation | Automated evidence |
| --- | --- | --- |
| Username contains an underscore and is at most five characters | `RegistrationClassOne.checkUserName` | `RegistrationClassOneTest`, valid, invalid, empty and null values |
| Password has at least eight characters, a capital, number and special character | `RegistrationClassOne.checkPasswordComplexity` | Original password validation and registration tests |
| South African international cellphone validation | `RegistrationClassOne.checkCellPhoneNumber` | Valid country code, length, invalid characters and null tests |
| Registration messages and successful sign-in with retries | `RegistrationClassOne.registerUser`, `LoginUserOne.loginUser`, `returnLoginStatus` | Registration, login, retry and integration tests |
| Unique ten-digit message IDs and generated hashes | `Message.newId`, `createMessageHash`, `MessageHistory.compose` | Message ID and hash tests, Part 3 fixture arrays |
| Recipient and 250-character validation | `Message.isRecipientCell`, `QuickChat.readMessageText` | Recipient tests and `QuickChatConsoleTest.preservesBatchEntryAndValidatesInputBeforeReporting` |
| Send, disregard or store messages | `QuickChat.recordChoice`, `MessageHistory.record` | All five assignment fixtures populate their respective arrays |
| Sent, disregarded, stored, ID and hash arrays populated dynamically | `MessageHistory`, using `Message[]` and `String[]` snapshots | `QuickChatPart3Test.fillsAllFiveArraysInMessageOrder` |
| Read the stored JSON file into an array | `MessageArchive.load` | Legacy field compatibility, reload and malformed-file tests |
| Fourth main option named Stored Messages | `QuickChat.run` | Console menu and report integration tests |
| Show all stored senders and recipients | Stored submenu option 1 | `reloadsSendersAndRecipientsFromTheArchive` |
| Find the longest stored message | Stored submenu option 2 | `selectsTheLongestStoredMessage` |
| Find an ID and show recipient and text | Stored submenu option 3 | `findsDinnerByIdAndKeepsTheOriginalNumberAndText`, console search test |
| Find stored messages for a recipient | Stored submenu option 4 | `findsTheExpectedStoredAndSentMessagesForARecipient` |
| Delete a stored message by hash and save the change | Stored submenu option 5, `deleteStored` | Persistent deletion, ID/hash removal and console deletion tests |
| Full stored report | Stored submenu option 6 | `showsBothSentAndStoredReportsWithRequiredDetails` |
| Sent report and combined recipient search in the acceptance table | Main option 2 and stored submenu option 7 | Report and recipient-search acceptance tests |
| Five exact assignment fixtures | `QuickChatPart3Test` setup | Sent cake, stored late, disregarded gate, sent dinner and stored leaving fixtures |
| Automated test workflow called TestJava.yml | `.github/workflows/TestJava.yml` | Ant test and packaging on Ubuntu and Windows using JDK 17 |
| Minimum six GitHub commits | Repository commit history | Inspect the published history |
| Attribute JSON and regular-expression sources | README references, source comments | Gson and Oracle documentation references |
| Own-voice video and Arc submission | Student's recording and final submission | Follow the recording/submission steps in `WALKTHROUGH.md` |

The table on page 18 uses `0838884567` for message 4 and the text `It is dinner time !`. The tests preserve those exact inputs. The later acceptance table omits the space before the exclamation mark; the application preserves entered text rather than silently changing it.

The application simulates sending in the console. It does not send SMS messages. Stored messages persist in the JSON file; sent and disregarded messages are current-session data.
