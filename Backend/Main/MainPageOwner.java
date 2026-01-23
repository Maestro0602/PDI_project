package Backend.main;

import java.util.Scanner;
import Backend.src.accountassign.EmailAccountCreationUI;
import Backend.src.owner.AssignPassword;
import Backend.src.owner.totalstudent;
public class MainPageOwner {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        try {
            System.out.println("=".repeat(30));
            System.out.println("Welcome To Owner main page");
            System.out.println("=".repeat(30));
            System.out.println("1. Manage Accounts");
            System.out.println("2. See Overall Students");
            System.out.println("3. Assign Password for HeadTeacher");
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
                        break;
                    case 2:
                        System.out.println("See Overall Students ");
                        totalstudent.main(null);
                        // Add your reports generation logic here
                        break;
                    case 3:
                        System.out.println("Assign Password for HeadTeacher...");
                        // Add your password assignment logic here
                        AssignPassword.main(null);
                        break;
                    case 4:
                        System.out.println("Logout...");
                        Main.main(args);
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
