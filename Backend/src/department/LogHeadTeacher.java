package Backend.src.department;

import Backend.main.MainPageTeacher;
import Backend.src.database.HeadTeacherpw;
import java.util.Scanner;

public class LogHeadTeacher {
    public static void main(String[] args) {
        // Initialize database

        Scanner input = new Scanner(System.in);
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\n========================================");
            System.out.println("    HEAD TEACHER LOGIN");
            System.out.println("========================================");
            System.out.print("Enter Head Teacher ID: ");
            String headteacherID = input.nextLine();

            System.out.print("Enter Password: ");
            String password = input.nextLine();

            // Verify credentials from headteacher_password table
            if (HeadTeacherpw.verifyHeadTeacherPassword(headteacherID, password)) {
                System.out.println("\n Login successful! Welcome " + headteacherID);
                loggedIn = true;
                ManageDepartment.main(null);
            } else {
                System.out.println("\n Invalid ID or Password!");
                System.out.println("\n1. Try Again");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                String choice = input.nextLine();

                if (choice.equals("0")) {
                    System.out.println("\nGoodbye!");
                    MainPageTeacher.main(null);
                    break;
                }
            }
        }
    }
}
