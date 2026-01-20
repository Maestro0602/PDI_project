package Backend.Main;

import Backend.src.database.DatabaseManager;
import java.util.Scanner;

public class AuthService {

    /**
     * Handle user login
     */
    public static void handleLogin(String usernameOrEmail, String password) {
        String loggedInUser = DatabaseManager.verifyLogin(usernameOrEmail, password);

        if (loggedInUser != null) {
            System.out.println("\n✓ Login successful!");
            System.out.println("Welcome, " + loggedInUser + "!");
            System.out.println("\nYou are now logged in to the system.");
        } else {
            System.out.println("\n✗ Login failed! Invalid password.");
        }
    }

    /**
     * Handle user registration
     */
    public static void handleRegistration(Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("          REGISTRATION");
        System.out.println("========================================");

        System.out.print("Enter username: ");
        String newUsername = scanner.nextLine();

        System.out.print("Enter email: ");
        String newEmail = scanner.nextLine();

        System.out.print("Enter password (min 8 characters): ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("\n✗ Passwords don't match!");
            return;
        }

        if (newPassword.length() < 8) {
            System.out.println("\n✗ Password must be at least 8 characters long!");
            return;
        }

        if (!isValidEmail(newEmail)) {
            System.out.println("\n✗ Invalid email format!");
            return;
        }

        boolean success = DatabaseManager.registerUser(newUsername, newEmail, newPassword);

        if (success) {
            System.out.println("\n✓ Registration successful!");
            System.out.println("Data saved to MySQL database:");
            System.out.println("  - Username: " + newUsername);
            System.out.println("  - Email: " + newEmail);
            System.out.println("  - Password: " + maskPassword(newPassword) + " (hashed)");
            System.out.println("\nYou can now login with your credentials.");
        }
    }

    /**
     * Validate email format
     */
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Mask password with asterisks
     */
    public static String maskPassword(String password) {
        return "*".repeat(password.length());
    }
}
