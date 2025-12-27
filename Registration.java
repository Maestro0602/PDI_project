import java.util.Scanner;
import Backend.Database.DatabaseManager;

public class Registration extends main_page {

    protected static void startRegistration(Scanner scanner) {

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
            System.out.println("You can now login.");
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
