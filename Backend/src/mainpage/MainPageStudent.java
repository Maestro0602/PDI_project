package Backend.src.mainpage;
import java.util.Scanner;
public class MainPageStudent {
    public static void main(String[] args) {
        boolean exit = false;
        System.out.println("=".repeat(30));
        System.out.println("Welcome To Student main page");
        System.out.println("=".repeat(30));
        System.out.println("1. Check Schedule");
        System.out.println("2. Check Grades");
        System.out.println("3. Check Assignments");
        System.out.println("4. Logout");
        System.out.println();

        Scanner input = new Scanner(System.in);

        while (!exit) {
            System.out.print("Choose an option to proceed: ");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Check Schedule");
                    exit = true;
                    break;
                case 2:
                    System.out.println("Check grades");
                    exit = true;
                    break;
                case 3:
                    System.out.println("Check Assignment");
                    exit = true;
                    break;
                case 4:
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
