import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            AttendanceService service = new AttendanceService();
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                System.out.println("\n========== MENU ==========");
                System.out.println("1. Take Attendance");
                System.out.println("2. View Attendance Summary");
                System.out.println("3. Exit");
                System.out.print("Choose option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1:
                        service.takeAttendance();
                        break;
                    case 2:
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        service.viewAttendanceSummary(date);
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        DatabaseConnection.getInstance().closeConnection();
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
