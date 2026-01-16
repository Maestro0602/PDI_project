package Backend.main;

import java.util.Scanner;

import Backend.src.searching.Searching;

public class MainPageStudent {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        try {
            System.out.println("=".repeat(30));
            System.out.println("Welcome To Student main page");
            System.out.println("=".repeat(30));
            System.out.println("1. Search Student Information");
            System.out.println("2. See Grades and GPA");
            System.out.println("3. Add Comments to Teachers");
            System.out.println("4. Logout");
            System.out.println();

            while (!exit) {
                System.out.print("Choose an option to proceed: ");
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Managing Students information...");
                        // Add your student management logic here
                        Searching.main(new String[] {});
                        break;
                    case 2:
                        System.out.println("See Grades and GPA...");
                        // Add your grades management logic here
                        break;
                    case 3:
                        System.out.println("Add Comments to Teachers...");
                        // Add your comments logic here
                        break;
                    case 4:
                        System.out.println("Logging out...");
                         exit = true;
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
