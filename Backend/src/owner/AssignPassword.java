package Backend.src.owner;

import Backend.src.database.HeadTeacherpw;
import java.util.Scanner;

import Backend.main.MainPageOwner;
public class AssignPassword {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        assignPasswordFromUser(input);
        MainPageOwner.main(null);
        input.close();
    }

    /**
     * Assign password to a HeadTeacher with user input
     */
    public static void assignPasswordFromUser(Scanner input) {

        // Initialize database & table
        HeadTeacherpw.createDatabase();
        HeadTeacherpw.createUserTable();

        System.out.println("\n========================================");
        System.out.println("   ASSIGN PASSWORD TO HEAD TEACHER");
        System.out.println("========================================");

        System.out.print("Enter HeadTeacher ID: ");
        String headTeacherID = input.nextLine().trim();

        if (headTeacherID.isEmpty()) {
            System.out.println(" HeadTeacher ID cannot be empty!");
            return;
        }

        System.out.print("Enter Password: ");
        String password = input.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println(" Password cannot be empty!");
            return;
        }

        boolean success = HeadTeacherpw.saveHeadTeacherInfo(headTeacherID, password);

        if (success) {
            System.out.println(" Password assigned successfully!");
        } else {
            System.out.println(" Failed to assign password!");
        }
    }
}
