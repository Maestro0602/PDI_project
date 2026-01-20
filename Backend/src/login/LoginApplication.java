package Backend.src.login;

import Backend.Main.MainPageOwner;
import Backend.Main.MainPageStudent;
import Backend.src.database.DatabaseManager;
import Backend.src.database.MajorManager;
import Backend.src.mainpage.MainPageTeacher;
import Backend.src.register.RegistrationManager;
import Backend.src.utils.PasswordUtils;
import java.util.Scanner;

public class LoginApplication {

    private static Scanner globalScanner;
    public static String loggedInUser;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        startLogin(scanner);
        scanner.close();
    }

    /**
     * Public method to start login process
     */
    public static void startLogin(Scanner scanner) {
        globalScanner = scanner;

        // Initialize database - create database first, then tables
        DatabaseManager.DatabaseConnection.createDatabase();
        DatabaseManager.createUserTable();
        MajorManager.createDepartmentMajorTable();

        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("========================================");
            System.out.println("          LOGIN SYSTEM");
            System.out.println("========================================");

            System.out.print("Enter your name: ");
            String name = globalScanner.nextLine();

            System.out.print("Enter password: ");
            String password = PasswordUtils.readMaskedPassword();

            // Check if user exists
            if (!DatabaseManager.checkUserExists(name)) {
                System.out.println("\n✗ User not found!");
                System.out.println("You need to register first.");

                System.out.print("\nWould you like to register? (yes/no): ");
                String register = globalScanner.nextLine().toLowerCase();

                if (register.equals("yes") || register.equals("y") || register.equals("YES")) {
                    RegistrationManager.handleRegistration(globalScanner);
                }
            } else {
                loggedIn = handleLogin(name, password);
            }
        }

        // Don't close scanner here - let the caller handle it
    }

    /**
     * Handle user login
     * Returns true if login successful, false otherwise
     */
    private static boolean handleLogin(String usernameOrEmail, String password) {
        loggedInUser = DatabaseManager.verifyLogin(usernameOrEmail, password);

        if (loggedInUser != null) {
            System.out.println("\n Login successful!");
            System.out.println("Welcome, " + loggedInUser + "!");
            System.out.println("\nYou are now logged in to the system.");

            // Check if username starts with "TEACHER" (case sensitive)
            if (loggedInUser.startsWith("TEACHER")) {
                System.out.println("\nRedirecting to Teacher Page...");
                MainPageTeacher.main(null);
            }
            if (loggedInUser.startsWith("STUDENT")) {
                System.out.println("\nRedirecting to Student Page...");
                MainPageStudent.main(null);
            }
            if (loggedInUser.startsWith("OWNER")) {
                System.out.println("\nRedirecting to Owner Page...");
                MainPageOwner.main(null);
            }

            return true;
        } else {
            System.out.println("\n Login failed! Invalid password.");

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
                        loggedInUser = DatabaseManager.verifyLogin(usernameOrEmail, newPassword);
                        if (loggedInUser != null) {
                            System.out.println("\n Login successful!");
                            System.out.println("Welcome, " + loggedInUser + "!");
                            System.out.println("\nYou are now logged in to the system.");

                            // Check if username starts with "TEACHER" (case sensitive)
                            if (loggedInUser.startsWith("TEACHER")) {
                                System.out.println("\nRedirecting to Teacher Page...");
                            }

                            return true;
                        } else {
                            System.out.println("\n Password is still incorrect!");
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
    private static void handleForgotPassword(String name) {
        System.out.println("\n========================================");
        System.out.println("          PASSWORD RESET");
        System.out.println("========================================");

        System.out.print("Enter new password (min 8 characters): ");
        String newPassword = PasswordUtils.readMaskedPassword();

        if (newPassword.length() < 8) {
            System.out.println(" Password must be at least 8 characters long!");
            return;
        }

        System.out.print("Confirm new password: ");
        String confirmPassword = PasswordUtils.readMaskedPassword();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println(" Passwords don't match!");
            return;
        }

        // Update password in database
        boolean success = DatabaseManager.resetPassword(name, newPassword);

        if (success) {
            System.out.println("\n Password reset successful!");
            System.out.println("You can now login with your new password.");
        } else {
            System.out.println("\n Password reset failed. Please try again.");
        }
    }
}
