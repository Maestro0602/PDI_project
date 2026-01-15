package Backend.Main;

import java.util.Scanner;
import Backend.src.accountassign.EmailAccountCreationUI;

public class MainPageOwner {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        try {
            System.out.println("=".repeat(30));
            System.out.println("Welcome To Owner main page");
            System.out.println("=".repeat(30));
            System.out.println("1. Manage Accounts");
            System.out.println("3. See Overall Students Performance");
            System.out.println("4. Logout");
            System.out.println();

            while (!exit) {
                System.out.print("Choose an option to proceed: ");
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Managing Accounts...");
                        EmailAccountCreationUI.main(new String[] {});
                        // Add your account management logic here
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
