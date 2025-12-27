import java.util.Scanner;
import Backend.Database.DatabaseManager;

public class main_page {

    protected static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        DatabaseManager.createUserTable();

        System.out.println("========================================");
        System.out.println("        WELCOME TO THE SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("\nChoose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        switch (choice) {
            case 1:
                LoginApplication.startLogin(scanner);
                break;
            case 2:
                Registration.startRegistration(scanner);
                break;
            default:
                System.out.println("✗ Invalid option");
        }

        scanner.close();
    }
}
