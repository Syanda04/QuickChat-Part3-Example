
package quickchat;

import java.util.Scanner;

public class RegistrationClassOne {

    String firstName;
    String surname;
    String username;
    String password;
    String cellphoneNumber;

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        RegistrationClassOne user = new RegistrationClassOne();

        System.out.println("===== USER REGISTRATION =====");

        System.out.print("Enter your first name: ");
        if (!input.hasNextLine()) { return; }
        user.firstName = input.nextLine().trim();

        while (user.firstName.isEmpty()) {
            System.out.println("First name cannot be empty.");
            System.out.print("Enter your first name: ");
            if (!input.hasNextLine()) { return; }
            user.firstName = input.nextLine().trim();
        }

        System.out.print("Enter your surname: ");
        if (!input.hasNextLine()) { return; }
        user.surname = input.nextLine().trim();

        while (user.surname.isEmpty()) {
            System.out.println("Surname cannot be empty.");
            System.out.print("Enter your surname: ");
            if (!input.hasNextLine()) { return; }
            user.surname = input.nextLine().trim();
        }

        // USERNAME
        while (true) {
            System.out.print("Enter your username: ");
            if (!input.hasNextLine()) { return; }
            user.username = input.nextLine();

            if (user.checkUserName(user.username)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println(
                    "Username is not correctly formatted; it must "
                    + "contain an underscore and be no more than "
                    + "five characters in length."
                );
            }
        }

        // PASSWORD
        while (true) {
            System.out.print("Enter your password: ");
            if (!input.hasNextLine()) { return; }
            user.password = input.nextLine();

            if (user.checkPasswordComplexity(user.password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println(
                    "Password must contain at least eight characters, "
                    + "a capital letter, a number, and a special character."
                );
            }
        }

        // CELLPHONE NUMBER
        while (true) {
            System.out.print("Enter your cellphone number "
                    + "(e.g. +27838968976): ");
            if (!input.hasNextLine()) { return; }
            user.cellphoneNumber = input.nextLine();

            if (user.checkCellPhoneNumber(user.cellphoneNumber)) {
                System.out.println("Cell number successfully captured.");
                break;
            } else {
                System.out.println(
                    "Cell number is incorrectly formatted or does "
                    + "not contain a valid South African international code."
                );
            }
        }

        // REGISTRATION
        String registrationStatus = user.registerUser();
        System.out.println("\n" + registrationStatus);

        // LOGIN AND PART 2
        if (registrationStatus.equals("Registration successful!")) {
            boolean loginSuccessful =
                    LoginUserOne.loginUser(user, input);

            if (loginSuccessful) {
                Message.startQuickChat(input, user.cellphoneNumber);
            }
        }

        input.close();
    }

    // Checks username format.
    public boolean checkUserName(String username) {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    // Checks password complexity.
    public boolean checkPasswordComplexity(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapital && hasNumber && hasSpecialCharacter;
    }

    // Validates South African international mobile numbers using Java Pattern [2], README.md.
    public boolean checkCellPhoneNumber(String cellphoneNumber) {
        return cellphoneNumber != null
                && cellphoneNumber.matches("^\\+27[6-8][0-9]{8}$");
    }

    // Registers the user after validating all required details.
    public String registerUser() {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted; "
                    + "please ensure that your username contains "
                    + "an underscore and is no more than five "
                    + "characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; "
                    + "please ensure that the password contains "
                    + "at least eight characters, a capital letter, "
                    + "a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellphoneNumber)) {
            return "Cell number is incorrectly formatted or does "
                    + "not contain a valid international code.";
        }

        return "Registration successful!";
    }
}
