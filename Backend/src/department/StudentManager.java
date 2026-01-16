package Backend.src.department;

import java.util.Scanner;
import java.util.ArrayList;

public class StudentManager {
    private ArrayList<Student> students = new ArrayList<>();
    private Scanner input = new Scanner(System.in);

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void displayAllDepartments() {
        System.out.println("\n📚 Available Departments:");
        System.out.println("1. " + Department.GIC.getDisplayName());
        System.out.println("2. " + Department.GIM.getDisplayName());
        System.out.println("3. " + Department.ELECTRIC.getDisplayName());
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
                selectedDepartment = Department.ELECTRIC.getDisplayName();
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        System.out.println("\n📋 Students in " + selectedDepartment + " Department:");
        boolean found = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getDepartment() != null &&
                    students.get(i).getDepartment().equals(selectedDepartment)) {
                System.out.println((i + 1) + ". " + students.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println(" No students in " + selectedDepartment + " department!");
        }
    }

    public void addStudentToDepartment() {
        System.out.println("\n--- Add Student to Department ---");

        if (students.isEmpty()) {
            System.out.println(" No students available!");
            return;
        }

        System.out.println("📋 Available Students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        System.out.print("Enter Student ID to assign to department: ");
        String studentId = input.nextLine();

        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
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
                        department = Department.ELECTRIC.getDisplayName();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        return;
                }

                student.setDepartment(department);
                System.out.println(" Student assigned to " + department + " department successfully!");
                return;
            }
        }
        System.out.println(" Student ID not found!");
    }

    public void removeStudentFromDepartment() {
        System.out.println("\n--- Remove Student from Department ---");

        if (students.isEmpty()) {
            System.out.println(" No students available!");
            return;
        }

        System.out.println("📋 Available Students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        System.out.print("Enter Student ID to remove from department: ");
        String studentId = input.nextLine();

        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                if (student.getDepartment() == null || student.getDepartment().isEmpty()) {
                    System.out.println(" Student is not assigned to any department!");
                } else {
                    String prevDept = student.getDepartment();
                    student.setDepartment(null);
                    System.out.println(" Student removed from " + prevDept + " department successfully!");
                }
                return;
            }
        }
        System.out.println(" Student ID not found!");
    }

    public void updateStudentDepartment() {
        System.out.println("\n--- Update Student Department ---");

        if (students.isEmpty()) {
            System.out.println(" No students available!");
            return;
        }

        System.out.println("📋 Available Students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        System.out.print("Enter Student ID to update department: ");
        String studentId = input.nextLine();

        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
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
                        department = Department.ELECTRIC.getDisplayName();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        return;
                }

                student.setDepartment(department);
                System.out.println(" Student department updated to " + department + " successfully!");
                return;
            }
        }
        System.out.println(" Student ID not found!");
    }
}
