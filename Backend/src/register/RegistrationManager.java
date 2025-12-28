package Backend.src.register;

import java.util.Scanner;
import Backend.src.utils.PasswordUtils;
import Backend.src.database.DatabaseManager;

public class RegistrationManager {

    /**
     * Public method to start registration process
     */
    public static void startRegistration(Scanner scanner) {
        handleRegistration(scanner);
    }

    /**
     * Handle user registration
     */
    public static void handleRegistration(Scanner scanner) {
        boolean registrationSuccess = false;

        while (!registrationSuccess) {
            System.out.println("\n========================================");
            System.out.println("          REGISTRATION");
            System.out.println("========================================");

            System.out.print("Enter username: ");
            String newUsername = scanner.nextLine();

            System.out.print("Enter email: ");
            String newEmail = scanner.nextLine();

            System.out.print("Enter password (min 8 characters): ");
            String newPassword = PasswordUtils.readMaskedPassword();

            System.out.print("Confirm password: ");
            String confirmPassword = PasswordUtils.readMaskedPassword();

            // Validate password match
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("\n Passwords don't match!");
                continue;
            }

            // Validate password length
            if (newPassword.length() < 8) {
                System.out.println("\n Password must be at least 8 characters long!");
                continue;
            }

            // Validate email format
            if (!isValidEmail(newEmail)) {
                System.out.println("\n Invalid email format!");
                continue;
            }

            // Register user in database
            boolean success = DatabaseManager.registerUser(newUsername, newEmail, newPassword);

            if (success) {
                System.out.println("\n Registration successful!");
                System.out.println("Data saved to MySQL database:");
                System.out.println("\nYou can now login with your credentials.");
                registrationSuccess = true;
            } else {
                System.out.println("\n Registration failed! Account already exists.");
                System.out.print("\nWould you like to try again with different details? (yes/no): ");
                String tryAgain = scanner.nextLine().toLowerCase();

                if (!tryAgain.equals("yes")) {
                    registrationSuccess = true; // Exit loop
                }
            }
        }
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
