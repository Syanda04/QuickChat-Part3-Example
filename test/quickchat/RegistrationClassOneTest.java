
package quickchat;

import java.io.File;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RegistrationClassOneTest {

    private RegistrationClassOne user;

    public RegistrationClassOneTest() {
    }


    public static void setUpClass() {
    }


    public static void tearDownClass() {
    }

   
    @Before
    public void setUp() {
        user = new RegistrationClassOne();
        user.firstName = "Mashudu";
        user.surname = "Nedzi";
        user.username = "M_01";
        user.password = "Password1!";
        user.cellphoneNumber = "+27838968976";
    }

   

    // ==========================================
    // USERNAME TESTS
    // ==========================================

    @Test
    public void testCheckUserNameCorrect() {
        assertTrue("JSON file should not be empty", user.checkUserName("M_01"));
    }

    @Test
    public void testCheckUserNameWithoutUnderscore() {
        assertFalse(user.checkUserName("Mashu"));
    }

    @Test
    public void testCheckUserNameTooLong() {
        assertFalse(user.checkUserName("M_12345"));
    }

    @Test
    public void testCheckUserNameEmpty() {
        assertFalse(user.checkUserName(""));
    }

    @Test
    public void testCheckUserNameNull() {
        assertFalse(user.checkUserName(null));
    }

    // ==========================================
    // PASSWORD TESTS
    // ==========================================

    @Test
    public void testPasswordComplexityCorrect() {
        assertTrue("JSON file should not be empty", user.checkPasswordComplexity("Password1!"));
    }

    @Test
    public void testPasswordTooShort() {
        assertFalse(user.checkPasswordComplexity("Pa1!"));
    }

    @Test
    public void testPasswordWithoutCapital() {
        assertFalse(user.checkPasswordComplexity("password1!"));
    }

    @Test
    public void testPasswordWithoutNumber() {
        assertFalse(user.checkPasswordComplexity("Password!"));
    }

    @Test
    public void testPasswordWithoutSpecialCharacter() {
        assertFalse(user.checkPasswordComplexity("Password1"));
    }

    @Test
    public void testPasswordEmpty() {
        assertFalse(user.checkPasswordComplexity(""));
    }

    @Test
    public void testPasswordNull() {
        assertFalse(user.checkPasswordComplexity(null));
    }

    // ==========================================
    // CELLPHONE NUMBER TESTS
    // ==========================================

    @Test
    public void testCellphoneNumberCorrect() {
        assertTrue("JSON file should not be empty", user.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellphoneNumberWithoutCountryCode() {
        assertFalse(user.checkCellPhoneNumber("0838968976"));
    }

    @Test
    public void testCellphoneNumberWrongCountryCode() {
        assertFalse(user.checkCellPhoneNumber("+2783896897"));
    }

    @Test
    public void testCellphoneNumberInvalidCharacters() {
        assertFalse(user.checkCellPhoneNumber("+27838ABC976"));
    }

    @Test
    public void testCellphoneNumberNull() {
        assertFalse(user.checkCellPhoneNumber(null));
    }

    // ==========================================
    // REGISTRATION TESTS
    // ==========================================

    @Test
    public void testRegisterUserSuccessful() {
        assertEquals(
                "Registration successful!",
                user.registerUser()
        );
    }

    @Test
    public void testRegisterUserInvalidUsername() {
        user.username = "Mashudu";

        String result = user.registerUser();

        assertTrue("JSON file should not be empty", result.contains("Username is not correctly formatted"));
    }

    @Test
    public void testRegisterUserInvalidPassword() {
        user.password = "password";

        String result = user.registerUser();

        assertTrue("JSON file should not be empty", result.contains("Password is not correctly formatted"));
    }

    @Test
    public void testRegisterUserInvalidCellphone() {
        user.cellphoneNumber = "0838968976";

        String result = user.registerUser();

        assertTrue("JSON file should not be empty", result.contains("Cell number is incorrectly formatted"));
    }

    // ==========================================
    // LOGIN TESTS
    // ==========================================

    @Test
    public void testLoginSuccessful() {
        Scanner input = new Scanner("M_01\nPassword1!\n");

        boolean result = LoginUserOne.loginUser(user, input);

        assertTrue("JSON file should not be empty", result);
        input.close();
    }

    @Test
    public void testLoginRetriesAfterIncorrectDetails() {
        Scanner input = new Scanner(
                "Wrong_1\nWrongPass1!\nM_01\nPassword1!\n"
        );

        boolean result = LoginUserOne.loginUser(user, input);

        assertTrue("JSON file should not be empty", result);
        input.close();
    }

    @Test
    public void testReturnLoginStatusSuccessful() {
        String result = LoginUserOne.returnLoginStatus(user, true);

        assertEquals(
                "Welcome Mashudu, Nedzi it is great to see you again.",
                result
        );
    }

    @Test
    public void testReturnLoginStatusUnsuccessful() {
        String result = LoginUserOne.returnLoginStatus(user, false);

        assertEquals(
                "Username or password incorrect, please try again.",
                result
        );
    }

    // ==========================================
    // PART 2: MESSAGE ID TESTS
    // ==========================================

    @Test
    public void testMessageIDHasTenDigits() {
        Message message = new Message();

        assertTrue("JSON file should not be empty", message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertTrue("JSON file should not be empty", message.getMessageID().matches("\\d{10}"));
    }

    // ==========================================
    // PART 2: RECIPIENT TESTS
    // ==========================================

    @Test
    public void testRecipientInternationalFormat() {
        Message message = new Message(
                1, "+27838968976", "Hello there"
        );

        assertTrue("JSON file should not be empty", message.checkRecipientCell());
    }

    @Test
    public void testRecipientLocalFormat() {
        Message message = new Message(
                1, "0838968976", "Hello there"
        );

        assertTrue("JSON file should not be empty", message.checkRecipientCell());
    }

    @Test
    public void testRecipientInvalidFormat() {
        Message message = new Message(
                1, "12345", "Hello there"
        );

        assertFalse(message.checkRecipientCell());
    }

    // ==========================================
    // PART 2: MESSAGE HASH TESTS
    // ==========================================

    @Test
    public void testMessageHashFormat() {
        Message message = new Message(
                1,
                "+27838968976",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertNotNull(message.getMessageHash());

        assertTrue("JSON file should not be empty", message.getMessageHash().matches("\\d{2}:1:HITONIGHT")
        );
    }

    @Test
    public void testMessageHashIncludesMessageNumber() {
        Message message = new Message(
                2, "+27838968976", "Hello Keegan"
        );

        assertTrue("JSON file should not be empty", message.getMessageHash().contains(":2:"));
    }

    // ==========================================
    // PART 2: MESSAGE DETAILS TESTS
    // ==========================================

    @Test
    public void testMessageDetails() {
        Message message = new Message(
                1, "+27838968976", "Hello there"
        );

        assertEquals(1, message.getMessageNumber());
        assertEquals("+27838968976", message.getRecipient());
        assertEquals("Hello there", message.getMessageText());
        assertEquals("Not processed", message.getStatus());
    }

    // ==========================================
    // PART 2: JSON STORAGE TEST
    // ==========================================

    @Test
    public void testStoreMessageCreatesJsonFile() {
        Message message = new Message(
                1, "+27838968976", "Hello there"
        );

        message.storeMessage();

        File file = new File("messages.json");

        assertTrue("JSON file should exist", file.exists());
        assertTrue("JSON file should not be empty", file.length() > 0);
    }

    // ==========================================
    // INTEGRATION TESTS
    // ==========================================

    @Test
    public void testRegistrationIntegration() {
        assertTrue("JSON file should not be empty", user.checkUserName(user.username));
        assertTrue("JSON file should not be empty", user.checkPasswordComplexity(user.password));
        assertTrue("JSON file should not be empty", user.checkCellPhoneNumber(user.cellphoneNumber));

        assertEquals(
                "Registration successful!",
                user.registerUser()
        );
    }

    @Test
    public void testRegistrationAndLoginIntegration() {
        String registrationResult = user.registerUser();

        assertEquals("Registration successful!", registrationResult);

        Scanner input = new Scanner("M_01\nPassword1!\n");

        boolean loginResult = LoginUserOne.loginUser(user, input);

        assertTrue("JSON file should not be empty", loginResult);
        input.close();
    }

}
