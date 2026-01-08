package Backend.src.mainpage;
import java.util.Scanner;
public class MainPageTeacher {
    public static void main(String[] args) {
        boolean exit = false;
        System.out.println("=".repeat(30));
        System.out.println("Welcome To Teacher main page");
        System.out.println("=".repeat(30));
        System.out.println("1. Manage Students information");
        System.out.println("2. Manage grades and GPA");
        System.out.println("3. Manage Attendance");
        System.out.println("4. Generate Reports");
        System.out.println("5. Manage Department (Head of Department only)");
        System.out.println("6. Logout");
        System.out.println();

        Scanner input = new Scanner(System.in);

        while (!exit) {
            System.out.print("Choose an option to proceed: ");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Managing Students information...");
                    exit = true;
                    break;
                case 2:
                    System.out.println("Managing grades and GPA...");
                    exit = true;
                    break;
                case 3:
                    System.out.println("Managing Attendance...");
                    exit = true;
                    break;
                case 4:
                    System.out.println("Generating Reports...");
                    exit = true;
                    break;
                case 5:
                    System.out.println("Managing Department...");
                    exit = true;
                    break;
                case 6:
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            input.close();
        }
    }
}
