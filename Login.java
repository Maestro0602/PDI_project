import java.util.Scanner;
import Backend.Database.DatabaseManager;

public class Login extends main_page {

    protected static void startLogin(Scanner scanner) {

        System.out.println("\n========================================");
        System.out.println("              LOGIN");
        System.out.println("========================================");

        System.out.print("Enter username or email: ");
        String usernameOrEmail = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Password entered: " + maskPassword(password));

        if (!DatabaseManager.checkUserExists(usernameOrEmail)) {
            System.out.println("\n✗ User not found!");

            System.out.print("Would you like to register? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase();

            if (choice.equals("yes")) {
                Registration.startRegistration(scanner);
            } else {
                System.out.println("Returning to main menu...");
            }
            return;
        }

        handleLogin(usernameOrEmail, password);
    }

    private static void handleLogin(String usernameOrEmail, String password) {
        String loggedInUser = DatabaseManager.verifyLogin(usernameOrEmail, password);

        if (loggedInUser != null) {
            System.out.println("\n✓ Login successful!");
            System.out.println("Welcome, " + loggedInUser + "!");
        } else {
            System.out.println("\n✗ Login failed! Invalid password.");
        }
    }

    protected static String maskPassword(String password) {
        return "*".repeat(password.length());
    }
}
