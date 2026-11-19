
package quickchat;

import java.util.Scanner;

public class LoginUserOne {

    // Login continues until the correct details are entered.
    public static boolean loginUser(
            RegistrationClassOne user,
            Scanner input) {

        System.out.println("\n=========================");
        System.out.println("          LOGIN");
        System.out.println("=========================");

        while (true) {

            System.out.print("Enter your username: ");
            if (!input.hasNextLine()) { return false; }
            String enteredUsername = input.nextLine();

            System.out.print("Enter your password: ");
            if (!input.hasNextLine()) { return false; }
            String enteredPassword = input.nextLine();

            if (enteredUsername.equals(user.username)
                    && enteredPassword.equals(user.password)) {

                System.out.println("\n"
                        + returnLoginStatus(user, true));

                return true;

            } else {

                System.out.println("\n"
                        + returnLoginStatus(user, false));

                System.out.println("Please try again.\n");
            }
        }
    }

    // Returns the appropriate login status message.
    public static String returnLoginStatus(
            RegistrationClassOne user,
            boolean loginSuccessful) {

        if (loginSuccessful) {
            return "Welcome " + user.firstName + ", "
                    + user.surname
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}
