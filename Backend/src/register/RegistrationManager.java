package Backend.src.register;

import java.util.Scanner;
import Backend.src.utils.PasswordUtils;
import Backend.src.database.DatabaseManager;
import Backend.src.database.EmailManager;

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

            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("\n Name cannot be empty!");
                continue;
            }

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("\n Email cannot be empty!");
                continue;
            }

            // Check if email exists in any of the three tables (owner, student, teacher)
            String userType = getEmailType(email);
            if (userType == null) {
                System.out.println("\n Email not found in our system!");
                System.out.println("Your email must be registered as an Owner, Student, or Teacher.");
                System.out.print("Would you like to try with a different email? (yes/no): ");
                String tryAgain = scanner.nextLine().toLowerCase();
                if (!tryAgain.equals("yes") && !tryAgain.equals("y")) {
                    registrationSuccess = true; // Exit loop
                }
                continue;
            }

            // Get role_id for the user
            int roleId = getRoleId(email, userType);
            if (roleId == -1) {
                System.out.println("\n Error: Could not retrieve user ID!");
                continue;
            }

            // Generate username based on email type and name
            String generatedUsername = generateUsername(userType, name);

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

            // Register user in database with role and roleId
            boolean success = DatabaseManager.registerUser(generatedUsername, email, newPassword, userType, roleId);

            if (success) {
                System.out.println("\n Registration successful!");
                System.out.println("Data saved to MySQL database:");
                System.out.println("Username: " + generatedUsername);
                System.out.println("Email: " + email);
                System.out.println("Account Type: " + userType);
                System.out.println("\nYou can now login with your credentials.");
                registrationSuccess = true;
            } else {
                System.out.println("\n Registration failed! Account already exists.");
                System.out.print("\nWould you like to try again with different details? (yes/no): ");
                String tryAgain = scanner.nextLine().toLowerCase();

                if (!tryAgain.equals("yes") && !tryAgain.equals("y")) {
                    registrationSuccess = true; // Exit loop
                }
            }
        }
    }

    /**
     * Check which type of user the email belongs to
     * Returns "STUDENT", "TEACHER", "OWNER", or null if not found
     */
    private static String getEmailType(String email) {
        try {
            // Get emails from database
            java.util.List<String> studentEmails = EmailManager.getStudentEmails();
            java.util.List<String> teacherEmails = EmailManager.getTeacherEmails();
            java.util.List<String> ownerEmails = EmailManager.getOwnerEmails();

            // Check with trimmed comparison (case-sensitive)
            for (String studentEmail : studentEmails) {
                if (studentEmail != null && studentEmail.trim().equals(email)) {
                    return "STUDENT";
                }
            }

            for (String teacherEmail : teacherEmails) {
                if (teacherEmail != null && teacherEmail.trim().equals(email)) {
                    return "TEACHER";
                }
            }

            for (String ownerEmail : ownerEmails) {
                if (ownerEmail != null && ownerEmail.trim().equals(email)) {
                    return "OWNER";
                }
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Exception in getEmailType: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the role_id for the user based on email and type
     */
    private static int getRoleId(String email, String userType) {
        try {
            if (userType.equals("STUDENT")) {
                return EmailManager.getStudentIdByEmail(email);
            } else if (userType.equals("TEACHER")) {
                return EmailManager.getTeacherIdByEmail(email);
            } else if (userType.equals("OWNER")) {
                return EmailManager.getOwnerIdByEmail(email);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error getting role ID: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Generate username based on user type and name
     * Format: TYPE_name_2026
     */
    private static String generateUsername(String userType, String name) {
        // Remove spaces and special characters from name
        String cleanName = name.replaceAll("\\s+", "").replaceAll("[^a-zA-Z0-9]", "");
        return userType + "_" + cleanName + "_2026";
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}