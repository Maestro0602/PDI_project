import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AttendanceUI {
    private Scanner scanner;
    
    public AttendanceUI() {
        this.scanner = new Scanner(System.in);
    }
    
    public int displayMainMenu() {
        System.out.println("\n===========================================");
        System.out.println("    STUDENT ATTENDANCE SYSTEM");
        System.out.println("===========================================");
        System.out.println("Date: " + LocalDate.now());
        System.out.println("\nMain Menu:");
        System.out.println("1 - Take Attendance");
        System.out.println("2 - Display All Attendance");
        System.out.println("3 - Exit");
        System.out.println("===========================================");
        System.out.print("Enter your choice (1-3): ");
        
        int choice = 0;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 3) {
                    validInput = true;
                } else {
                    System.out.print("Invalid choice! Please enter 1-3: ");
                }
            } catch (Exception e) {
                System.out.print("Invalid input! Please enter a number (1-3): ");
                scanner.next();
            }
        }
        
        return choice;
    }
    
    public void displayHeader() {
        System.out.println("\n===========================================");
        System.out.println("    TAKE ATTENDANCE");
        System.out.println("===========================================");
        System.out.println("Date: " + LocalDate.now());
        System.out.println("\nAttendance Options:");
        System.out.println("1 - Present (Score: 1.0)");
        System.out.println("2 - Late (Score: 0.8)");
        System.out.println("3 - Absent (Score: 0.0)");
        System.out.println("4 - Excused/Permission (Score: 0.5)");
        System.out.println("===========================================\n");
    }
    
    public void displayStudent(int number, String name) {
        System.out.println("\nStudent #" + number + ": " + name);
        System.out.print("Enter attendance (1-4): ");
    }
    
    public int getAttendanceInput() {
        int status = 0;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                status = scanner.nextInt();
                if (AttendanceStatus.isValid(status)) {
                    validInput = true;
                } else {
                    System.out.print("Invalid input! Please enter 1-4: ");
                }
            } catch (Exception e) {
                System.out.print("Invalid input! Please enter a number (1-4): ");
                scanner.next();
            }
        }
        
        return status;
    }
    
    public void displayRecorded(String statusText, double score) {
        System.out.println("✓ Recorded: " + statusText + " (Score: " + score + ")");
    }
    
    public void displaySummary(int studentCount) {
        System.out.println("\n===========================================");
        System.out.println("Attendance completed for " + studentCount + " students!");
        System.out.println("===========================================");
    }
    
    public void displayAllAttendance(List<AttendanceRecord> records) {
        System.out.println("\n===========================================");
        System.out.println("    ALL ATTENDANCE RECORDS");
        System.out.println("===========================================");
        
        if (records.isEmpty()) {
            System.out.println("\nNo attendance records found.");
        } else {
            System.out.println(String.format("%-15s %-25s %-15s %-15s %-10s", 
                "Student ID", "Student Name", "Date", "Status", "Score"));
            System.out.println("-----------------------------------------------------------------------------------");
            
            for (AttendanceRecord record : records) {
                System.out.println(String.format("%-15d %-25s %-15s %-15s %-10.2f",
                    record.getStudentId(),
                    record.getStudentName(),
                    record.getAttendanceDate(),
                    AttendanceStatus.getStautsText(record.getStatus()),
                    record.getScore()));
            }
            
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("Total records: " + records.size());
        }
        
        System.out.println("===========================================");
    }
    
    public void displayError(String message) {
        System.err.println("ERROR: " + message);
    }
    
    public void displayExitMessage() {
        System.out.println("\n===========================================");
        System.out.println("Thank you for using the Attendance System!");
        System.out.println("===========================================");
    }
    
    public void close() {
        scanner.close();
    }
}
