package Backend.src.searching;

import java.util.Scanner;
import Backend.src.database.StudentInfoManager;
import Backend.src.database.MajorManager;

public class Searching {

    public static void main(String[] args) {
        // Create the studentInfo table if it doesn't exist
        StudentInfoManager.createStudentInfoTable();

        Scanner scanner = new Scanner(System.in);
        boolean searching = true;

        while (searching) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("        STUDENT SEARCH SYSTEM");
            System.out.println("=".repeat(40));
            System.out.println("Search Options:");
            System.out.println("1. Search by Name");
            System.out.println("2. Search by ID");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    searchByName(scanner);
                    break;
                case "2":
                    searchByID(scanner);
                    break;
                case "3":
                    System.out.println("\n Exiting search system. Goodbye!");
                    searching = false;
                    break;
                default:
                    System.out.println("  Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void searchByName(Scanner scanner) {
        System.out.print("\nEnter student name to search: ");
        String searchName = scanner.nextLine().trim();

        if (searchName.isEmpty()) {
            System.out.println(" Name cannot be empty!");
            return;
        }

        // Search for matching names
        String[][] results = StudentInfoManager.searchStudentByName(searchName);

        if (results == null || results.length == 0) {
            System.out.println(" No students found with name containing: " + searchName);
            return;
        }

        // System.out.println("\n Found " + results.length + " student(s) matching: " +
        // searchName);
        System.out.println("-".repeat(40));

        for (int i = 0; i < results.length; i++) {
            System.out.println((i + 1) + ". Name: " + results[i][0] + " | ID: " + results[i][1]);
        }

        System.out.print("\nDo you want to see details? (yes/no): ");
        String viewDetails = scanner.nextLine().trim().toLowerCase();

        if (viewDetails.equals("yes") || viewDetails.equals("y")) {
            System.out.print("Enter the number to view details (1-" + results.length + "): ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim()) - 1;

                if (index >= 0 && index < results.length) {
                    // Get the student ID and fetch fresh details from database
                    String studentID = results[index][1];
                    String[] detailedInfo = StudentInfoManager.getStudentInfo(studentID);
                    if (detailedInfo != null) {
                        displayStudentDetails(detailedInfo);
                    } else {
                        System.out.println(" Failed to retrieve student details!");
                    }
                } else {
                    System.out.println("  Invalid selection!");
                }
            } catch (NumberFormatException e) {
                System.out.println(" ✗ Please enter a valid number!");
            }
        }
    }

    private static void searchByID(Scanner scanner) {
        System.out.print("\nEnter student ID to search: ");
        String searchID = scanner.nextLine().trim();

        if (searchID.isEmpty()) {
            System.out.println(" ✗ ID cannot be empty!");
            return;
        }

        // Search for exact ID match
        String[] result = StudentInfoManager.getStudentInfo(searchID);

        if (result == null) {
            System.out.println("  No student found with ID: " + searchID);
            return;
        }

        System.out.println("\n Student found!");
        System.out.println("-".repeat(40));
        System.out.println("Name: " + result[0] + " | ID: " + result[1]);

        System.out.print("\nDo you want to see details? (yes/no): ");
        String viewDetails = scanner.nextLine().trim().toLowerCase();

        if (viewDetails.equals("yes") || viewDetails.equals("y")) {
            displayStudentDetails(result);
        }
    }

    private static void displayStudentDetails(String[] studentInfo) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        STUDENT DETAILS");
        System.out.println("=".repeat(50));
        System.out.println("Name:     " + studentInfo[0]);
        System.out.println("ID:       " + studentInfo[1]);
        System.out.println("Gender:   " + studentInfo[2]);
        System.out.println("Year:     " + studentInfo[3]);
        
        // Get department, major, and courses from departmentMajor table
        String[] deptMajorCourses = MajorManager.getFullDepartmentMajorCourse(studentInfo[1]);
        
        if (deptMajorCourses != null && deptMajorCourses.length >= 6) {
            System.out.println("-".repeat(50));
            System.out.println("Department:  " + deptMajorCourses[0]);
            System.out.println("Major:       " + deptMajorCourses[1]);
            System.out.println("\nCourses:");
            System.out.println("  1. " + deptMajorCourses[2]);
            System.out.println("  2. " + deptMajorCourses[3]);
            System.out.println("  3. " + deptMajorCourses[4]);
            System.out.println("  4. " + deptMajorCourses[5]);
        } else {
            System.out.println("-".repeat(50));
            System.out.println("Department:  Not assigned");
            System.out.println("Major:       Not assigned");
        }
        
        System.out.println("=".repeat(50));
    }
}
