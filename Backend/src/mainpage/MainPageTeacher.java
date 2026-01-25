package Backend.src.mainpage;

import java.util.Scanner;
import Backend.src.department.ManageDepartment;
import Backend.src.studentassign.studentassign;
import Backend.src.department.LogHeadTeacher;
import Backend.src.searching.SearchingTeacher;
import Backend.src.grading.Grading;
import Backend.src.report.GenerateStudentReport;
import Backend.src.attendance.AttendanceUI;

public class MainPageTeacher {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        try {
            System.out.println("========================================");
            System.out.println("Welcome To Teacher main page");
            System.out.println("========================================");
            System.out.println("1. Manage Students information");
            System.out.println("2. Manage Score and Grades");
            System.out.println("3. Manage Attendance");
            System.out.println("4. Generate Reports");
            System.out.println("5. Manage Department (Head of Department only)");
            System.out.println("6. Searching Students");
            System.out.println("7. Logout");
            System.out.println();

            while (!exit) {
                System.out.print("Choose an option to proceed: ");
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Managing Students information...");
                        studentassign.main(null);
                        break;
                    case 2:
                        System.out.println("Managing grades and GPA...");
                        Grading.main(null);
                        break;
                    case 3:
                        System.out.println("Managing Attendance...");
                        AttendanceUI.main(null);
                        break;
                    case 4:
                        System.out.println("Generating Reports...");
                        GenerateStudentReport.main(null);
                        break;
                    case 5:
                        System.out.println("Managing Department...");
                        LogHeadTeacher.main(null);
                        break;
                    case 6:
                        System.out.println("Searching Students...");
                        SearchingTeacher.main(args);
                        break;
                    case 7:
                        System.out.println("Logging out...");
                        Main.main(null);
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } finally {
            input.close();
        }
    }
}