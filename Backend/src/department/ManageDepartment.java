package Backend.src.department;

import java.util.Scanner;

public class ManageDepartment {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StudentManager studentManager = new StudentManager();
        TeacherManager teacherManager = new TeacherManager();
        boolean exit = false;

        // Pre-load test students
        studentManager.getStudents().add(new Student("001", "Alice Johnson"));
        studentManager.getStudents().add(new Student("002", "Bob Smith", "GIC"));
        studentManager.getStudents().add(new Student("003", "Carol Williams", "GIM"));
        System.out.println("✓ Pre-loaded test students added!");

        // Pre-load test teachers
        teacherManager.getTeachers().add(new Teacher("T001", "Dr. Ahmed Hassan", "GIC"));
        teacherManager.getTeachers().add(new Teacher("T002", "Prof. Fatima Mohamed", "GIM"));
        teacherManager.getTeachers().add(new Teacher("T003", "Eng. Omar Ali"));
        System.out.println("✓ Pre-loaded test teachers added!");

        while (!exit) {
            System.out.println("\n========================================");
            System.out.println("     DEPARTMENT MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Student Management");
            System.out.println("2. Teacher Management");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int mainChoice = input.nextInt();
            input.nextLine();

            switch (mainChoice) {
                case 1:
                    manageStudents(input, studentManager);
                    break;
                case 2:
                    manageTeachers(input, teacherManager);
                    break;
                case 3:
                    exit = true;
                    System.out.println(" Thank you! Goodbye!");
                    break;
                default:
                    System.out.println(" Invalid choice. Please try again.");
            }
        }
        input.close();
    }

    private static void manageStudents(Scanner input, StudentManager studentManager) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\n========================================");
            System.out.println("     STUDENT MANAGEMENT");
            System.out.println("========================================");
            studentManager.displayAllDepartments();
            System.out.println("\n--- Operations ---");
            System.out.println("1. Add Student to Department");
            System.out.println("2. Remove Student from Department");
            System.out.println("3. Update Student Department");
            System.out.println("4. View All Students by Department");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    studentManager.addStudentToDepartment();
                    break;
                case 2:
                    studentManager.removeStudentFromDepartment();
                    break;
                case 3:
                    studentManager.updateStudentDepartment();
                    break;
                case 4:
                    studentManager.viewStudentsByDepartment(input);
                    break;
                case 5:
                    backToMain = true;
                    break;
                default:
                    System.out.println(" Invalid choice. Please try again.");
            }
        }
    }

    private static void manageTeachers(Scanner input, TeacherManager teacherManager) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\n========================================");
            System.out.println("     TEACHER MANAGEMENT");
            System.out.println("========================================");
            teacherManager.displayAllDepartments();
            System.out.println("\n--- Operations ---");
            System.out.println("1. Add Teacher to Department");
            System.out.println("2. Remove Teacher from Department");
            System.out.println("3. Update Teacher Department");
            System.out.println("4. View All Teachers by Department");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    teacherManager.addTeacherToDepartment();
                    break;
                case 2:
                    teacherManager.removeTeacherFromDepartment();
                    break;
                case 3:
                    teacherManager.updateTeacherDepartment();
                    break;
                case 4:
                    teacherManager.viewTeachersByDepartment(input);
                    break;
                case 5:
                    backToMain = true;
                    break;
                default:
                    System.out.println(" Invalid choice. Please try again.");
            }
        }
    }
}
