package Backend.src.department;

import java.util.Scanner;
import Backend.src.database.StudentInfoManager;
import Backend.src.database.MajorManager;
import Backend.src.database.TeacherInfoManager;
import Backend.src.database.CourseManager;
import Backend.src.database.TeacherCourseManager;
import Backend.src.major.major;
import Backend.main.MainPageTeacher;
import Backend.src.course.Course;
import Backend.src.database.DatabaseManager;

public class ManageDepartment {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StudentManager studentManager = new StudentManager();
        TeacheinforManager teacherManager = new TeacheinforManager();
        boolean exit = false;

        // Initialize database tables
        StudentInfoManager.createStudentInfoTable();
        // System.out.println(" Student Info database initialized!");

        MajorManager.createDepartmentMajorTable();
        // System.out.println(" Department/Major database initialized!");

        CourseManager.createCourseTable();
        TeacherCourseManager.createTeacherCourseTable();

        TeacherInfoManager.createTeacherInfoTable();
        // System.out.println(" Teacher Info database initialized!");

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
                    System.out.println(" Thank you! Goodbye!");
                    MainPageTeacher.main(null);
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
            System.out.println("5. Back to Menu");
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

    private static void manageTeachers(Scanner input, TeacheinforManager teacherManager) {
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
            System.out.println("7. Back to Menu");
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
                    DatabaseManager.displayTemporaryTeacherUsers();
                    addNewTeacher(input);
                    break;
                case 5:
                    DatabaseManager.displayTemporaryTeacherUsers();
                    updateTeacher(input);
                    break;
                case 6:
                    DatabaseManager.displayTemporaryTeacherUsers();
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
        System.out.println("AI00(1-4)");
        System.out.println("AU10(1-4)");
        System.out.println("AI00(1-4)");
        System.out.println("CS00(1-4)");
        System.out.println("EE10(1-4)");
        System.out.println("EL10(1-4)");
        System.out.println("IE10(1-4)");
        System.out.println("ME10(1-4)");
        System.out.println("MF10(1-4)");
        System.out.println("SE10(1-4)");
        System.out.print("Enter course ID: ");
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
                System.out.println("Teacher ID cannot be empty! Try again.");
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

            // Check if teacher already exists
            if (TeacherInfoManager.teacherExists(teacherID)) {
                System.out.println("\nTeacher with ID " + teacherID + " already exists!");

                // Get existing teacher info
                String[] teacherInfo = TeacherInfoManager.getTeacherInfo(teacherID);
                if (teacherInfo != null) {
                    System.out.println("Current Department: " + teacherInfo[1]);
                    System.out.println("Current Major: " + teacherInfo[2]);

                    String majorName = teacherInfo[2];

                    // Show current courses
                    System.out.println("\n--- Current Courses ---");
                    TeacherCourseManager.getTeacherCourses(teacherID);

                    // Ask if they want to add more courses
                    System.out.print("\nDo you want to assign additional courses to this teacher? (yes/no): ");
                    String response = input.nextLine();

                    if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                        assignMultipleCoursesToTeacher(input, teacherID, majorName);
                    } else {
                        System.out.println("Operation cancelled.");
                    }
                }
                return; // Exit the method after handling existing teacher
            }

            // If teacher doesn't exist, proceed with normal registration
            validID = true;
            System.out.println("\n Teacher ID is available!");

            System.out.println("\n--- Select Department ---");
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

            String majorName = selectedMajor;

            // Save teacher info
            TeacherInfoManager.saveTeacherInfo(teacherID, department, majorName);

            // Allow teacher to select multiple courses
            assignMultipleCoursesToTeacher(input, teacherID, majorName);
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
                System.out.println("Teacher ID cannot be empty! Try again.");
                continue;
            }

            if (!TeacherInfoManager.teacherExists(teacherID)) {
                System.out.println(" Teacher with ID " + teacherID + " not found! Try again.");
                continue;
            }

            validID = true;
        }

        // Select Department
        System.out.println("\n--- Select Department ---");
        System.out.println("1. GIC (General IT & Computing)");
        System.out.println("2. GIM (General IT & Management)");
        System.out.println("3. GEE (General Electrical & Engineering)");
        System.out.print("Choose department (1-3): ");
        int deptChoice = input.nextInt();
        input.nextLine();

        String department = "";
        String selectedMajor = null;

        switch (deptChoice) {
            case 1:
                department = "GIC";
                selectedMajor = major.getGICMajor();
                break;
            case 2:
                department = "GIM";
                selectedMajor = major.getGIMMajor();
                break;
            case 3:
                department = "GEE";
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

        // Save to database
        TeacherInfoManager.updateTeacherInfo(teacherID, department, selectedMajor);

        // Remove old courses from teacher_course table
        TeacherCourseManager.deleteTeacherAllCourses(teacherID);

        // Allow teacher to select multiple courses
        assignMultipleCoursesToTeacher(input, teacherID, selectedMajor);
    }

    /**
     * Allow a teacher to assign multiple courses within their major
     */
    private static void assignMultipleCoursesToTeacher(Scanner input, String teacherID, String majorName) {
        boolean addMore = true;

        while (addMore) {
            System.out.println("\n--- Select Course ---");
            String selectedCourse = displayAndSelectCourse(input, majorName);

            if (selectedCourse == null) {
                System.out.println("Course selection cancelled.");
                break;
            }

            // Get course ID from course name
            String courseId = CourseManager.getCourseIdByName(selectedCourse);
            if (courseId == null) {
                System.out.println(" Course ID not found for: " + selectedCourse);
                continue;
            }

            // Assign course to teacher in teacher_course table
            boolean courseAssigned = TeacherCourseManager.addTeacherCourse(teacherID, courseId);
            if (courseAssigned) {
                System.out.println(" Course assigned: " + selectedCourse + " (" + courseId + ")");
            } else {
                // System.out.println(" Failed to assign course.");
            }

            // Ask if teacher wants to teach another course
            System.out.print("\nDo you want to assign another course? (yes/no): ");
            String response = input.nextLine();
            if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("y")) {
                addMore = false;
                System.out.println(" Teacher courses assignment completed!");
            }
        }
    }

    // private static String selectCourseForMajor(String major) {
    // switch (major) {
    // case "Software Engineering":
    // return Course.getSECourse();
    // case "Cyber Security":
    // return Course.getCyberCourse();
    // case "Artificial Intelligence":
    // return Course.getAICourse();
    // case "Mechanical Engineering":
    // return Course.getMechanicCourse();
    // case "Manufacturing Engineering":
    // return Course.getManufactCourse();
    // case "Industrial Engineering":
    // return Course.getIndustCourse();
    // case "Electrical Engineering":
    // return Course.getElectricCourse();
    // case "Electronics Engineering":
    // return Course.getElectronicCourse();
    // case "Automation Engineering":
    // return Course.getAutomationCourse();
    // default:
    // return null;
    // }
    // }

    /**
     * Display available courses for a major and let teacher select one
     */
    private static String displayAndSelectCourse(Scanner input, String major) {
        String[] courses = Course.getCoursesForMajor(major);

        if (courses == null || courses.length == 0) {
            System.out.println(" No courses found for this major.");
            return null;
        }

        System.out.println("\n========================================");
        System.out.println("     SELECT COURSE FOR " + major.toUpperCase());
        System.out.println("========================================");

        for (int i = 0; i < courses.length; i++) {
            System.out.println((i + 1) + ". " + courses[i]);
        }

        System.out.print("Enter your choice (1-" + courses.length + "): ");
        int choice = input.nextInt();
        input.nextLine();

        if (choice < 1 || choice > courses.length) {
            System.out.println(" Invalid choice!");
            return null;
        }

        String selectedCourse = courses[choice - 1];
        System.out.println(" You selected: " + selectedCourse);
        return selectedCourse;
    }

    private static void deleteTeacher(Scanner input) {
        System.out.println("\n========================================");
        System.out.println("        DELETE TEACHER");
        System.out.println("========================================");

        System.out.print("Enter Teacher ID to delete: ");
        String teacherID = input.nextLine();

        if (!TeacherInfoManager.teacherExists(teacherID)) {
            System.out.println(" Teacher with ID " + teacherID + " not found!");
            return;
        }

        System.out.print("Are you sure you want to delete this teacher? (yes/no): ");
        String confirm = input.nextLine();

        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            TeacherInfoManager.deleteTeacher(teacherID);
            TeacherCourseManager.deleteTeacherAllCourses(teacherID);
        } else {
            System.out.println("Delete cancelled.");
        }
    }
}
