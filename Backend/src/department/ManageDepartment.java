package Backend.src.department;

import java.util.Scanner;
import Backend.src.database.StudentInfoManager;
import Backend.src.database.MajorManager;
import Backend.src.database.TeacherInfoManager;
import Backend.src.major.major;
import Backend.src.course.Course;

public class ManageDepartment {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StudentManager studentManager = new StudentManager();
        TeacherManager teacherManager = new TeacherManager();
        boolean exit = false;

        // Initialize database tables
        StudentInfoManager.createStudentInfoTable();
        System.out.println(" Student Info database initialized!");

        MajorManager.createDepartmentMajorTable();
        System.out.println(" Department/Major database initialized!");

        TeacherInfoManager.createTeacherInfoTable();
        System.out.println(" Teacher Info database initialized!");

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

    public static void manageStudents(Scanner input, StudentManager studentManager) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\n========================================");
            System.out.println("     STUDENT MANAGEMENT");
            System.out.println("========================================");
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
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    break;
                case 2:
                    studentManager.removeStudentFromDepartment();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    studentManager.updateStudentDepartment();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
            System.out.println("\n--- Operations ---");
            System.out.println("1. View All Teachers");
            System.out.println("2. View Teachers by Department");
            System.out.println("3. View Teachers by Course");
            System.out.println("4. Add New Teacher");
            System.out.println("5. Update Teacher Information");
            System.out.println("6. Delete Teacher");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    TeacherInfoManager.displayAllTeachers();
                    break;
                case 2:
                    viewTeachersByDepartment(input);
                    break;
                case 3:
                    viewTeachersByCourse(input);
                    break;
                case 4:
                    addNewTeacher(input);
                    break;
                case 5:
                    updateTeacher(input);
                    break;
                case 6:
                    deleteTeacher(input);
                    break;
                case 7:
                    backToMain = true;
                    break;
                default:
                    System.out.println(" Invalid choice. Please try again.");
            }
        }
    }

    private static void viewTeachersByDepartment(Scanner input) {
        System.out.println("\n========================================");
        System.out.println("  VIEW TEACHERS BY DEPARTMENT");
        System.out.println("========================================");
        System.out.println("1. GIC (General IT & Computing)");
        System.out.println("2. GIM (General IT & Management)");
        System.out.println("3. GEE (General Electrical & Engineering)");
        System.out.print("Enter department choice (1-3): ");

        int choice = input.nextInt();
        input.nextLine();

        String department = "";
        switch (choice) {
            case 1:
                department = "GIC";
                break;
            case 2:
                department = "GIM";
                break;
            case 3:
                department = "GEE";
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        TeacherInfoManager.displayTeachersByDepartment(department);
    }

    private static void viewTeachersByCourse(Scanner input) {
        System.out.println("\n========================================");
        System.out.println("   VIEW TEACHERS BY COURSE");
        System.out.println("========================================");
        System.out.print("Enter course name: ");
        String course = input.nextLine();

        TeacherInfoManager.displayTeachersByCourse(course);
    }

    private static void addNewTeacher(Scanner input) {
        System.out.println("\n========================================");
        System.out.println("        ADD NEW TEACHER");
        System.out.println("========================================");

        String teacherID = "";
        boolean validID = false;

        // Loop until user enters a valid teacher ID
        while (!validID) {
            System.out.print("Enter Teacher ID: ");
            teacherID = input.nextLine();

            if (teacherID.trim().isEmpty()) {
                System.out.println(" Teacher ID cannot be empty! Try again.");
                continue;
            }
            if (teacherID.length() < 8) {
                System.out.println("Teacher ID must be at least 8 characters long! Try again.");
                continue;
            }
            if (!teacherID.startsWith("T2024")) {
                System.out.println("Teacher ID must start with 'T2024'! Try again.");
                continue;
            }

            if (TeacherInfoManager.teacherExists(teacherID)) {
                System.out.println(" Teacher with ID " + teacherID + " already exists! Try again.");
                continue;
            }

            validID = true;
            System.out.println("1. GIC (General IT & Computing)");
            System.out.println("2. GIM (General IT & Management)");
            System.out.println("3. GEE (General Electrical & Engineering)");
            System.out.print("Enter department choice (1-3): ");

            String department = "";
            int deptChoice = input.nextInt();
            input.nextLine();

            switch (deptChoice) {
                case 1:
                    department = "GIC";
                    break;
                case 2:
                    department = "GIM";
                    break;
                case 3:
                    department = "GEE";
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            String selectedMajor = "";
            switch (deptChoice) {
                case 1:
                    selectedMajor = major.getGICMajor();
                    break;
                case 2:
                    selectedMajor = major.getGIMMajor();
                    break;
                case 3:
                    selectedMajor = major.getGEEMajor();
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            if (selectedMajor == null) {
                System.out.println("Major selection cancelled.");
                return;
            }

            String major = selectedMajor;

            String course = "";

            // Get course based on selected major
            if (major.equals("Software Engineering")) {
                course = Course.getSECourse();
            } else if (major.equals("Cyber Security")) {
                course = Course.getCyberCourse();
            } else if (major.equals("Artificial Intelligence")) {
                course = Course.getAICourse();
            } else if (major.equals("Mechanical Engineering")) {
                course = Course.getMechanicCourse();
            } else if (major.equals("Manufacturing Engineering")) {
                course = Course.getManufactCourse();
            } else if (major.equals("Industrial Engineering")) {
                course = Course.getIndustCourse();
            } else if (major.equals("Automation Engineering")) {
                course = Course.getAutomationCourse();
            } else if (major.equals("Electrical Engineering")) {
                course = Course.getElectricCourse();
            } else if (major.equals("Electronics Engineering")) {
                course = Course.getElectronicCourse();
            }

            if (course == null) {
                System.out.println("Course selection cancelled.");
                return;
            }

            TeacherInfoManager.saveTeacherInfo(teacherID, department, major, course);
            System.out.println("✓ Teacher added successfully!");
        }
    }

    private static void updateTeacher(Scanner input) {
        System.out.println("\n========================================");
        System.out.println("     UPDATE TEACHER INFORMATION");
        System.out.println("========================================");

        String teacherID = "";
        boolean validID = false;

        // Loop until user enters a valid teacher ID
        while (!validID) {
            System.out.print("Enter Teacher ID to update: ");
            teacherID = input.nextLine();

            if (teacherID.trim().isEmpty()) {
                System.out.println("✗ Teacher ID cannot be empty! Try again.");
                continue;
            }

            if (!TeacherInfoManager.teacherExists(teacherID)) {
                System.out.println("✗ Teacher with ID " + teacherID + " not found! Try again.");
                continue;
            }

            validID = true;
        }

        System.out.print("Enter New Department (GIC/GIM/GEE): ");
        String department = input.nextLine();

        System.out.print("Enter New Major: ");
        String major = input.nextLine();

        System.out.print("Enter New Course: ");
        String course = input.nextLine();

        TeacherInfoManager.updateTeacherInfo(teacherID, department, major, course);
    }

    private static void deleteTeacher(Scanner input) {
        System.out.println("\n========================================");
        System.out.println("        DELETE TEACHER");
        System.out.println("========================================");

        System.out.print("Enter Teacher ID to delete: ");
        String teacherID = input.nextLine();

        if (!TeacherInfoManager.teacherExists(teacherID)) {
            System.out.println("✗ Teacher with ID " + teacherID + " not found!");
            return;
        }

        System.out.print("Are you sure you want to delete this teacher? (yes/no): ");
        String confirm = input.nextLine();

        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            TeacherInfoManager.deleteTeacher(teacherID);
        } else {
            System.out.println("Delete cancelled.");
        }
    }
}
