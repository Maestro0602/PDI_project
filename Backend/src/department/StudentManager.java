package Backend.src.department;

import Backend.src.database.DatabaseManager;
import Backend.src.database.MajorManager;
import Backend.src.database.StudentInfoManager;
import Backend.src.major.major;
import java.util.Scanner;

public class StudentManager {
    private Scanner input = new Scanner(System.in);

    public void displayAllDepartments() {
        System.out.println("\n Available Departments:");
        System.out.println("1. " + Department.GIC.getDisplayName());
        System.out.println("2. " + Department.GIM.getDisplayName());
        System.out.println("3. " + Department.GEE.getDisplayName());
    }

    public void viewStudentsByDepartment(Scanner input) {
        System.out.println("\n--- View Students by Department ---");
        displayAllDepartments();
        System.out.print("Enter department choice (1-3): ");
        int deptChoice = input.nextInt();
        input.nextLine();

        String selectedDepartment = "";
        switch (deptChoice) {
            case 1:
                selectedDepartment = Department.GIC.getDisplayName();
                break;
            case 2:
                selectedDepartment = Department.GIM.getDisplayName();
                break;
            case 3:
                selectedDepartment = Department.GEE.getDisplayName();
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

       // StudentInfoManager.displayStudentsByDepartment(selectedDepartment);
    }

    public void addStudentToDepartment() {
        System.out.println("\n--- Add Student to Department ---");

        System.out.println(" Available Students:");
        StudentInfoManager.displayAllStudents();

        String studentId = "";
        String[] studentInfo = null;
        while (studentInfo == null) {
            System.out.print("Enter Student ID to assign to department (or 'q' to cancel): ");
            studentId = input.nextLine();
            if (studentId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }

            // Check if UserID exists in users table
            boolean userIDExists = DatabaseManager.ConditionChecker.checkUserIDExists(studentId);
            if (!userIDExists) {
                System.out.println(" UserID not found in users table. Please try again.");
                continue;
            }

            studentInfo = StudentInfoManager.getStudentInfo(studentId);
            if (studentInfo == null) {
                System.out.println(" Student ID not found. Please try again.");
            } else {
                // Check if student already has a department assigned
                String[] existingDeptMajor = MajorManager.getDepartmentMajor(studentId);
                if (existingDeptMajor != null) {
                    System.out.println(" This id already have department! Try again.");
                    studentInfo = null; // Reset to loop again
                }
            }
        }

        displayAllDepartments();
        System.out.print("Choose department (1-3): ");
        int deptChoice = input.nextInt();
        input.nextLine();

        String department = "";
        switch (deptChoice) {
            case 1:
                department = Department.GIC.getDisplayName();
                break;
            case 2:
                department = Department.GIM.getDisplayName();
                break;
            case 3:
                department = Department.GEE.getDisplayName();
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        // Get major selection without saving yet
        String major = getMajorForDepartment(department);
        if (major == null) {
            System.out.println("Major selection cancelled.");
            return;
        }

        // Now save both department and major together
        if (MajorManager.saveDepartmentMajor(studentId, department, major)) {
            System.out.println(" Student assigned to " + department + " with major " + major + " successfully!");
        } else {
            System.out.println(" Failed to assign student.");
        }
    }

    private String getMajorForDepartment(String department) {
        if (department.equals(Department.GIC.getDisplayName())) {
            return major.getGICMajor();
        } else if (department.equals(Department.GIM.getDisplayName())) {
            return major.getGIMMajor();
        } else if (department.equals(Department.GEE.getDisplayName())) {
            return major.getGEEMajor();
        }
        return null;
    }

    public void removeStudentFromDepartment() {
        System.out.println("\n--- Remove Student from Department ---");

        System.out.println(" Available Students:");
        StudentInfoManager.displayAllStudents();

        String studentId = "";
        String[] studentInfo = null;
        while (studentInfo == null) {
            System.out.print("Enter Student ID to remove from department (or 'q' to cancel): ");
            studentId = input.nextLine();
            if (studentId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }
            studentInfo = StudentInfoManager.getStudentInfo(studentId);
            if (studentInfo == null) {
                System.out.println(" Student ID not found. Please try again.");
            }
        }

        String[] deptMajor = MajorManager.getDepartmentMajor(studentId);
        if (deptMajor == null) {
            System.out.println(" Student is not assigned to any department!");
        } else {
            if (MajorManager.updateDepartmentMajor(studentId, null, null)) {
                System.out.println(" Student removed from " + deptMajor[0] + " department successfully!");
            } else {
                System.out.println(" Failed to remove student from department.");
            }
        }
    }

    public void updateStudentDepartment() {
        System.out.println("\n--- Update Student Department ---");

        System.out.println(" Available Students:");
        StudentInfoManager.displayAllStudents();

        String studentId = "";
        String[] studentInfo = null;
        while (studentInfo == null) {
            System.out.print("Enter Student ID to update department (or 'q' to cancel): ");
            studentId = input.nextLine();
            if (studentId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                ManageDepartment.manageStudents(input, null);
                return;
            }
            studentInfo = StudentInfoManager.getStudentInfo(studentId);
            if (studentInfo == null) {
                System.out.println(" Student ID not found. Please try again.");
            }
        }

        displayAllDepartments();
        System.out.print("Choose new department (1-3): ");
        int deptChoice = input.nextInt();
        input.nextLine();

        String department = "";
        switch (deptChoice) {
            case 1:
                department = Department.GIC.getDisplayName();
                break;
            case 2:
                department = Department.GIM.getDisplayName();
                break;
            case 3:
                department = Department.GEE.getDisplayName();
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        // Get major selection without saving yet
        String major = getMajorForDepartment(department);
        if (major == null) {
            System.out.println("Major selection cancelled.");
            return;
        }

        // Now update both department and major together
        if (MajorManager.updateDepartmentMajor(studentId, department, major)) {
            System.out.println(
                    " Student department updated to " + department + " with major " + major + " successfully!");
        } else {
            System.out.println(" Failed to update student department.");
        }
    }
}
