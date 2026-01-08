
import Backend.src.database.DatabaseManager;
import Backend.src.utils.PasswordUtils;
import java.util.Scanner;

public class AuthService {

    /**
     * Handle user login - checks users table only
     */
    public static String handleLogin(String usernameOrEmail, String password) {
        // Verify login against users table
        String loggedInUser = DatabaseManager.verifyLogin(usernameOrEmail, password);

        if (loggedInUser != null) {
            System.out.println("\n✓ Login successful!");
            System.out.println("Welcome, " + loggedInUser + "!");
            System.out.println("\nYou are now logged in to the system.");
            return loggedInUser;
        } else {
            System.out.println("\n✗ Login failed! Invalid username/email or password.");
            return null;
        }
    }

    /**
     * Check if user exists in users table
     */
    public static boolean userExists(String usernameOrEmail) {
        return DatabaseManager.checkUserExists(usernameOrEmail);
    }

    /**
     * Get user's role from users table
     */
    public static String getUserRole(String usernameOrEmail) {
        return DatabaseManager.getUserRole(usernameOrEmail);
    }

    /**
     * Handle password reset
     */
    public static boolean resetPassword(String email, String newPassword) {
        // Check if email exists in users table
        if (!DatabaseManager.checkEmailExists(email)) {
            System.out.println("\n✗ Email not found in system!");
            return false;
        }

        // Validate password length
        if (newPassword.length() < 8) {
            System.out.println("\n✗ Password must be at least 8 characters long!");
            return false;
        }

        // Reset password in users table
        boolean success = DatabaseManager.resetPassword(email, newPassword);

        if (success) {
            System.out.println("\n✓ Password reset successful!");
            return true;
        } else {
            System.out.println("\n✗ Password reset failed!");
            return false;
        }
    }

    /**
     * Interactive password reset with confirmation
     */
    public static boolean resetPasswordInteractive(Scanner scanner, String email) {
        System.out.println("\n========================================");
        System.out.println("          PASSWORD RESET");
        System.out.println("========================================");

        // Check if email exists
        if (!DatabaseManager.checkEmailExists(email)) {
            System.out.println("✗ Email not found in system!");
            return false;
        }

        System.out.print("Enter new password (min 8 characters): ");
        String newPassword = PasswordUtils.readMaskedPassword();

        if (newPassword.length() < 8) {
            System.out.println("\n✗ Password must be at least 8 characters long!");
            return false;
        }

        System.out.print("Confirm new password: ");
        String confirmPassword = PasswordUtils.readMaskedPassword();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("\n✗ Passwords don't match!");
            return false;
        }

        return resetPassword(email, newPassword);
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        // Minimum 8 characters
        if (password.length() < 8) {
            return false;
        }
        return true;
    }

    /**
     * Enhanced password validation with detailed feedback
     */
    public static String validatePasswordWithFeedback(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        // Optional: Add more validation rules
        // if (!password.matches(".*[A-Z].*")) {
        //     return "Password must contain at least one uppercase letter";
        // }
        // if (!password.matches(".*[a-z].*")) {
        //     return "Password must contain at least one lowercase letter";
        // }
        // if (!password.matches(".*\\d.*")) {
        //     return "Password must contain at least one number";
        // }
        
        return "valid";
    }

    /**
     * Display user information from users table
     */
    public static void displayUserInfo(String usernameOrEmail) {
        System.out.println("\n========================================");
        System.out.println("          USER INFORMATION");
        System.out.println("========================================");
        
        DatabaseManager.getUserDetails(usernameOrEmail);
    }

    /**
     * Mask password with asterisks
     */
    public static String maskPassword(String password) {
        return "*".repeat(password.length());
    }
}