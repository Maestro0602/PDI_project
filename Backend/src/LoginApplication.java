package Backend.src;

import java.util.Scanner;

public class LoginApplication {

    private static Scanner globalScanner;

    public static void main(String[] args) {
        globalScanner = new Scanner(System.in);

        // Initialize database
        DatabaseManager.createUserTable();

        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("========================================");
            System.out.println("          LOGIN SYSTEM");
            System.out.println("========================================");

            System.out.print("Enter username or email: ");
            String usernameOrEmail = globalScanner.nextLine();

            System.out.print("Enter password: ");
            String password = PasswordUtils.readMaskedPassword();

            // Check if user exists
            if (!DatabaseManager.checkUserExists(usernameOrEmail)) {
                System.out.println("\n✗ User not found!");
                System.out.println("You need to register first.");

                System.out.print("\nWould you like to register? (yes/no): ");
                String register = globalScanner.nextLine().toLowerCase();

                if (register.equals("yes") || register.equals("y") || register.equals("YES")) {
                    RegistrationManager.handleRegistration(globalScanner);
                }
            } else {
                loggedIn = handleLogin(usernameOrEmail, password);
            }
        }

        globalScanner.close();
    }

    /**
     * Handle user login
     * Returns true if login successful, false otherwise
     */
    private static boolean handleLogin(String usernameOrEmail, String password) {
        String loggedInUser = DatabaseManager.verifyLogin(usernameOrEmail, password);

        if (loggedInUser != null) {
            System.out.println("\n✓ Login successful!");
            System.out.println("Welcome, " + loggedInUser + "!");
            System.out.println("\nYou are now logged in to the system.");
            return true;
        } else {
            System.out.println("\n✗ Login failed! Invalid password.");

            boolean resolved = false;
            while (!resolved) {
                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Try again (Re-enter password)");
                System.out.println("2. Forgot Password");
                System.out.println("3. Go back to login screen");
                System.out.print("Enter your choice (1/2/3): ");

                String choice = globalScanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        System.out.print("Enter password again: ");
                        String newPassword = PasswordUtils.readMaskedPassword();
                        String retryUser = DatabaseManager.verifyLogin(usernameOrEmail, newPassword);
                        if (retryUser != null) {
                            System.out.println("\n✓ Login successful!");
                            System.out.println("Welcome, " + retryUser + "!");
                            System.out.println("\nYou are now logged in to the system.");
                            return true;
                        } else {
                            System.out.println("\n✗ Password is still incorrect!");
                        }
                        break;

                    case "2":
                        handleForgotPassword(usernameOrEmail);
                        resolved = true;
                        break;

                    case "3":
                        resolved = true;
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
            return false;
        }
    }

    /**
     * Handle forgot password functionality
     */
    private static void handleForgotPassword(String usernameOrEmail) {
        System.out.println("\n========================================");
        System.out.println("          PASSWORD RESET");
        System.out.println("========================================");
        
        System.out.print("Enter your registered email: ");
        String email = globalScanner.nextLine();
        
        // Verify email exists in database
        if (DatabaseManager.checkEmailExists(email)) {
            System.out.print("Enter new password (min 8 characters): ");
            String newPassword = PasswordUtils.readMaskedPassword();
            
            if (newPassword.length() < 8) {
                System.out.println("✗ Password must be at least 8 characters long!");
                return;
            }
            
            System.out.print("Confirm new password: ");
            String confirmPassword = PasswordUtils.readMaskedPassword();
            
            if (!newPassword.equals(confirmPassword)) {
                System.out.println(" Passwords don't match!");
                return;
            }
            
            // Update password in database
            boolean success = DatabaseManager.resetPassword(email, newPassword);
            
            if (success) {
                System.out.println("\n Password reset successful!");
                System.out.println("You can now login with your new password.");
            } else {
                System.out.println("\n Password reset failed. Please try again.");
            }
        } else {
            System.out.println(" Email not found in our records!");
        }
    }
}