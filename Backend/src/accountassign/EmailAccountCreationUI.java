package Backend.src.accountassign;

import Backend.src.database.EmailManager;
import Backend.src.database.EmailManager.AccountType;
import Backend.main.MainPageOwner;
import java.util.Scanner;

/**
 * Terminal-based application for creating email accounts for students,
 * teachers, and owners
 */
public class EmailAccountCreationUI {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initialize email tables if they don't exist
        EmailManager.AccountDatabaseConnection.createAccountDatabase();
        EmailManager.AccountDatabaseConnection.initializeEmailTables();

        System.out.println("\n========================================");
        System.out.println("   EMAIL ACCOUNT CREATION SYSTEM");
        System.out.println("========================================\n");

        boolean continueApp = true;

        while (continueApp) {
            createEmailAccount();

            System.out.println("\nDo you want to create another account?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Choose an option (1-2): ");

            int choice = getUserChoice();
            if (choice != 1) {
                System.out.println("\n Exiting application...");
                MainPageOwner.main(new String[] {});
                continueApp = false;
            }
        }

        scanner.close();
    }

    /**
     * Display main menu
     */

    /**
     * Get user's menu choice
     */
    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Create email account process
     */
    private static void createEmailAccount() {
        System.out.println("\n========================================");
        System.out.println("      SELECT ACCOUNT TYPE");
        System.out.println("========================================");
        System.out.println("1. Student (ID starts with P2024)");
        System.out.println("2. Teacher (ID starts with T2024)");
        System.out.println("3. Owner (ID starts with O2024)");
        System.out.println("4. Exit");
        System.out.print("Select account type (1-4): ");

        int typeChoice = getUserChoice();
        AccountType type = null;

        switch (typeChoice) {
            case 1:
                type = AccountType.STUDENT;
                break;
            case 2:
                type = AccountType.TEACHER;
                break;
            case 3:
                type = AccountType.OWNER;
                break;
            case 4:
                System.out.println("\n Exiting application...");
                MainPageOwner.main(new String[] {});
                System.exit(0);
                return;
            default:
                System.out.println(" Invalid choice. Please try again.");
                return;
        }

        // Get ID from user
        System.out.print("\nEnter ID (must start with " + type.getPrefix() + "): ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println(" ID cannot be empty");
            return;
        }

        // Get email from user
        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();

        if (email.isEmpty()) {
            System.out.println(" Email cannot be empty");
            return;
        }

        // Validate inputs
        if (!EmailManager.validateIdFormat(id, type)) {
            System.out.println(" Invalid ID format. Must start with " + type.getPrefix());
            return;
        }

        if (!EmailManager.validateEmailFormat(email)) {
            System.out.println(" Invalid email format. Use format: example@domain.com");
            return;
        }

        if (EmailManager.idExists(id, type)) {
            System.out.println(" Error: This ID already has an email account");
            return;
        }

        if (EmailManager.emailExistsInSystem(email)) {
            System.out.println(" Error: This email is already in use");
            return;
        }

        // Create email account
        System.out.println("\nProcessing...");
        boolean success = EmailManager.createEmailAccount(type, id, email);

        if (success) {
            System.out.println("\n========================================");
            System.out.println(" EMAIL ACCOUNT CREATED SUCCESSFULLY!");
            System.out.println("========================================");
            System.out.println("Account Type: " + getAccountTypeLabel(type));
            System.out.println("ID: " + id);
            System.out.println("Email: " + email);
            System.out.println("========================================\n");
        } else {
            System.out.println("\n Failed to create email account. Please try again.");
        }
    }

    /**
     * Get readable account type label
     */
    private static String getAccountTypeLabel(AccountType type) {
        switch (type) {
            case STUDENT:
                return "Student";
            case TEACHER:
                return "Teacher";
            case OWNER:
                return "Owner";
            default:
                return "Unknown";
        }
    }
}
