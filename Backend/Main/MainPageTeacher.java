package Backend.main;

import java.util.Scanner;
import Backend.src.department.ManageDepartment;
import Backend.src.studentassign.studentassign;;

public class MainPageTeacher {

    // New method that accepts Scanner parameter
   public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        try {
            System.out.println("=".repeat(30));
            System.out.println("Welcome To Teacher main page");
            System.out.println("=".repeat(30));
            System.out.println("1. Manage Students information");
            System.out.println("2. Manage grades and GPA");
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
                        // Add your student management logic here
                        break;
                    case 2:
                        System.out.println("Managing grades and GPA...");
                        // Add your grades management logic here
                        break;
                    case 3:
                        System.out.println("Managing Attendance...");
                        // Add your attendance management logic here
                        break;
                    case 4:
                        System.out.println("Generating Reports...");
                        // Add your reports generation logic here
                        break;
                    case 5:
                        System.out.println("Managing Department...");
                        ManageDepartment.main(null);
                        break;
                    case 6:
                        System.out.println("Searching Students...");
                        break;
                    case 7:
                        System.out.println("Logging out...");
                        Main.main(null);
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