package Backend.main;

import Backend.src.login.LoginApplication;
import Backend.src.register.RegistrationManager;
import Backend.src.database.DatabaseManager;
import Backend.src.database.EmailManager;
import java.util.Scanner;
import Backend.src.mainpage.MainPageStudent;
import Backend.src.mainpage.MainPageTeacher;

public class Main {

    protected static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Initialize databases - create database first, then tables
        DatabaseManager.DatabaseConnection.createDatabase();
        DatabaseManager.createUserTable();
        EmailManager.AccountDatabaseConnection.initializeEmailTables();

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
                    LoginApplication.startLogin(scanner);
                    break;
                case 2:
                    RegistrationManager.startRegistration(scanner);
                    break;
                case 3:
                    System.out.println("\n Thank you for using the system. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println(" Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
