import Backend.src.login.LoginApplication;
import Backend.src.register.RegistrationManager;
import java.util.Scanner;

public class Main {

    protected static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean exit = false;

        while (!exit) {
            System.out.println("========================================");
            System.out.println("        WELCOME TO THE SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // LOGIN and redirect based on role
                    LoginApplication.startLogin(scanner);
                    break;
                case 2:
                    RegistrationManager.startRegistration(scanner);
                    break;
                case 3:
                    System.out.println("\nThank you for using the system. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
