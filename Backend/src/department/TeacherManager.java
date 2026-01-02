package Backend.src.department;

import java.util.Scanner;
import java.util.ArrayList;

public class TeacherManager {
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private Scanner input = new Scanner(System.in);

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public void displayAllDepartments() {
        System.out.println("\n Available Departments:");
        System.out.println("1. " + Department.GIC.getDisplayName());
        System.out.println("2. " + Department.GIM.getDisplayName());
        System.out.println("3. " + Department.GEE.getDisplayName());
    }

    public void viewTeachersByDepartment(Scanner input) {
        System.out.println("\n--- View Teachers by Department ---");
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

        System.out.println("\n Teachers in " + selectedDepartment + " Department:");
        boolean found = false;
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getDepartment() != null &&
                    teachers.get(i).getDepartment().equals(selectedDepartment)) {
                System.out.println((i + 1) + ". " + teachers.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println(" No teachers in " + selectedDepartment + " department!");
        }
    }

    public void addTeacherToDepartment() {
        System.out.println("\n--- Add Teacher to Department ---");

        if (teachers.isEmpty()) {
            System.out.println(" No teachers available!");
            return;
        }

        System.out.println(" Available Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        String teacherId = "";
        Teacher targetTeacher = null;
        while (targetTeacher == null) {
            System.out.print("Enter Teacher ID to assign to department (or 'q' to cancel): ");
            teacherId = input.nextLine();
            if (teacherId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }
            for (Teacher teacher : teachers) {
                if (teacher.getTeacherId().equals(teacherId)) {
                    targetTeacher = teacher;
                    break;
                }
            }
            if (targetTeacher == null) {
                System.out.println(" Teacher ID not found. Please try again.");
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

        targetTeacher.setDepartment(department);
        System.out.println(" Teacher assigned to " + department + " department successfully!");
        return;
    }

    public void removeTeacherFromDepartment() {
        System.out.println("\n--- Remove Teacher from Department ---");

        if (teachers.isEmpty()) {
            System.out.println(" No teachers available!");
            return;
        }

        System.out.println(" Available Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        String teacherId = "";
        Teacher targetTeacher = null;
        while (targetTeacher == null) {
            System.out.print("Enter Teacher ID to remove from department (or 'q' to cancel): ");
            teacherId = input.nextLine();
            if (teacherId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }
            for (Teacher teacher : teachers) {
                if (teacher.getTeacherId().equals(teacherId)) {
                    targetTeacher = teacher;
                    break;
                }
            }
            if (targetTeacher == null) {
                System.out.println(" Teacher ID not found. Please try again.");
            }
        }

        if (targetTeacher.getDepartment() == null || targetTeacher.getDepartment().isEmpty()) {
            System.out.println(" Teacher is not assigned to any department!");
        } else {
            String prevDept = targetTeacher.getDepartment();
            targetTeacher.setDepartment(null);
            System.out.println(" Teacher removed from " + prevDept + " department successfully!");
        }
        return;
    }

    public void updateTeacherDepartment() {
        System.out.println("\n--- Update Teacher Department ---");

        if (teachers.isEmpty()) {
            System.out.println(" No teachers available!");
            return;
        }

        System.out.println("Available Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        String teacherId = "";
        Teacher targetTeacher = null;
        while (targetTeacher == null) {
            System.out.print("Enter Teacher ID to update department (or 'q' to cancel): ");
            teacherId = input.nextLine();
            if (teacherId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }
            for (Teacher teacher : teachers) {
                if (teacher.getTeacherId().equals(teacherId)) {
                    targetTeacher = teacher;
                    break;
                }
            }
            if (targetTeacher == null) {
                System.out.println(" Teacher ID not found. Please try again.");
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

        targetTeacher.setDepartment(department);
        System.out.println(" Teacher department updated to " + department + " successfully!");
        return;
    }
}
