package Backend.src.major;

import Backend.src.database.MajorManager;
import java.util.Scanner;

public class major {

    /**
     * Get major selection for GIC department - returns the major string without
     * saving
     */
    public static String getGICMajor() {
        System.out.println("\nSelect your major in GIC:");
        System.out.println("1. Software Engineering");
        System.out.println("2. Cyber Security");
        System.out.println("3. Artificial Intelligence");
        System.out.print("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Software Engineering";
                System.out.println("You selected Software Engineering.");
                break;
            case 2:
                major = "Cyber Security";
                System.out.println("You selected Cyber Security.");
                break;
            case 3:
                major = "Artificial Intelligence";
                System.out.println("You selected AI and Data Science.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;

    }

    /**
     * Get major selection for GIM department - returns the major string without
     * saving
     */
    public static String getGIMMajor() {
        System.out.println("\nSelect your major in GIM:");
        System.out.println("1. Mechanical Engineering");
        System.out.println("2. Manufacturing Engineering");
        System.out.println("3. Industrial Engineering");
        System.out.print("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Mechanical Engineering";
                System.out.println("You selected Mechanical Engineering.");
                break;
            case 2:
                major = "Manufacturing Engineering";
                System.out.println("You selected Electrical Engineering.");
                break;
            case 3:
                major = "Industrial Engineering";
                System.out.println("You selected Industrial Engineering.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    /**
     * Get major selection for GEE department - returns the major string without
     * saving
     */
    public static String getGEEMajor() {
        System.out.println("\nSelect your major in GEE:");
        System.out.println("1. Electrical Engineering");
        System.out.println("2. Electronics Engineering");
        System.out.println("3. Automation Engineering");
        System.out.print("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Electrical Engineering";
                System.out.println("You selected Electrical Engineering.");
                break;
            case 2:
                major = "Electronics Engineering";
                System.out.println("You selected Electronics Engineering.");
                break;
            case 3:
                major = "Automation Engineering";
                System.out.println("You selected Automation Engineering.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    /**
     * Get major selection for GIC department and save to database
     */
    public static void selectAndSaveGICMajor(String studentId) {
        System.out.println("\nSelect your major in GIC:");
        System.out.println("1. Software Engineering");
        System.out.println("2. Cyber Security");
        System.out.println("3. Artificial Intelligence");
        System.out.print("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Software Engineering";
                System.out.println("You selected Software Engineering.");
                break;
            case 2:
                major = "Cyber Security";
                System.out.println("You selected Cyber Security.");
                break;
            case 3:
                major = "Artificial Intelligence";
                System.out.println("You selected AI and Data Science.");
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        if (MajorManager.updateDepartmentMajor(studentId, "GIC", major)) {
            System.out.println(" Major saved successfully!");
        } else {
            System.out.println(" Failed to save major.");
        }
    }

    /**
     * Get major selection for GIM department and save to database
     */
    public static void selectAndSaveGIMMajor(String studentId) {
        System.out.println("\nSelect your major in GIM:");
        System.out.println("1. Mechanical Engineering");
        System.out.println("2. Manufacturing Engineering");
        System.out.println("3. Industrial Engineering");
        System.out.print("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Mechanical Engineering";
                System.out.println("You selected Mechanical Engineering.");
                break;
            case 2:
                major = "Manufacturing Engineering";
                System.out.println("You selected Electrical Engineering.");
                break;
            case 3:
                major = "Industrial Engineering";
                System.out.println("You selected Industrial Engineering.");
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        if (MajorManager.updateDepartmentMajor(studentId, "GIM", major)) {
            System.out.println(" Major saved successfully!");
        } else {
            System.out.println(" Failed to save major.");
        }
    }

    /**
     * Get major selection for GEE department and save to database
     */
    public static void selectAndSaveGEEMajor(String studentId) {
        System.out.println("\nSelect your major in GEE:");
        System.out.println("1. Electrical Engineering");
        System.out.println("2. Electronics Engineering");
        System.out.println("3. Automation Engineering");
        System.out.print("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Electrical Engineering";
                System.out.println("You selected Electrical Engineering.");
                break;
            case 2:
                major = "Electronics Engineering";
                System.out.println("You selected Electronics Engineering.");
                break;
            case 3:
                major = "Automation Engineering";
                System.out.println("You selected Automation Engineering.");
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        if (MajorManager.updateDepartmentMajor(studentId, "GEE", major)) {
            System.out.println(" Major saved successfully!");
        } else {
            System.out.println(" Failed to save major.");
        }
    }

    public static void displayGICMajor() {
        System.out.println("Select your major in GIC:");
        System.out.println("1. Software Engineering");
        System.out.println("2. Cyber Security");
        System.out.println("3. Artificial Intelligence");
        System.out.println("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("You selected Software Engineering.");
                break;
            case 2:
                System.out.println("You selected Cyber Security.");
                break;
            case 3:
                System.out.println("You selected AI and Data Science.");
                break;
        }
    }

    public static void displayGIMMajor() {
        System.out.println("Select your major in GIM:");
        System.out.println("1. Mechanical Engineering");
        System.out.println("2. Manufacturing Engineering");
        System.out.println("3. Industrial Engineering");
        System.out.println("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("You selected Mechanical Engineering.");
                break;
            case 2:
                System.out.println("You selected Electrical Engineering.");
                break;
            case 3:
                System.out.println("You selected Industrial Engineering.");
                break;
        }
    }

    public static void displayGEEMajor() {
        System.out.println("Select your major in GEE:");
        System.out.println("1. Electrical Engineering");
        System.out.println("2. Electronics Engineering");
        System.out.println("3. Automation Engineering");
        System.out.println("Enter your choice (1-3): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("You selected Electrical Engineering.");
                break;
            case 2:
                System.out.println("You selected Electronics Engineering.");
                break;
            case 3:
                System.out.println("You selected Automation Engineering.");
                break;
        }
    }
}
