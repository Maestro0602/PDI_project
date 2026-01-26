package Backend.src.attendance;

import Backend.main.MainPageTeacher;
import Backend.src.database.Attendance;
import Backend.src.department.Student;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
public class AttendanceUI {
    private Scanner scanner;
    private Attendance database;
    
    public AttendanceUI() {
        this.scanner = new Scanner(System.in);
        try {
            this.database = new Attendance();
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        AttendanceUI ui = new AttendanceUI();
        ui.run();
    }
    
    public void run() {
        boolean running = true;
        
        while (running) {
            int choice = displayMainMenu();
            
            switch (choice) {
                case 1:
                    takeAttendance();
                    break;
                case 2:
                    displayAllAttendance();
                    break;
                case 3:
                    running = false;
                    displayExitMessage();
                    MainPageTeacher.main(null);
                    break;
            }
        }
        
        close();
    }
    
    private void takeAttendance() {
        try {
            List<Student> students = database.getAllStudents();
            
            if (students.isEmpty()) {
                System.out.println("\nNo students found in the database!");
                return;
            }
            
            displayHeader();
            
            int studentNumber = 1;
            for (Student student : students) {
                // Check if attendance already exists for this student today
                AttendanceRecord existingRecord = database.getExistingAttendance(
                    student.getStudentId(), 
                    LocalDate.now()
                );
                
                if (existingRecord != null) {
                    // Attendance already exists, ask if user wants to update
                    System.out.println("\nStudent #" + studentNumber + ": " + student.getStudentName());
                    System.out.println("Attendance already recorded: " + 
                        AttendanceStatus.getStautsText(existingRecord.getStatus()) + 
                        " (Score: " + existingRecord.getScore() + ")");
                    System.out.print("Do you want to update it? (Y/N): ");
                    
                    String response = scanner.next().trim().toUpperCase();
                    scanner.nextLine(); // consume newline
                    
                    if (response.equals("Y") || response.equals("YES")) {
                        System.out.print("Enter new attendance (1-4): ");
                        int status = getAttendanceInput();
                        double score = AttendanceStatus.getScore(status);
                        
                        AttendanceRecord updatedRecord = new AttendanceRecord(
                            student.getStudentId(),
                            LocalDate.now(),
                            status,
                            score
                        );
                        
                        database.updateAttendance(updatedRecord);
                        System.out.println(" Updated: " + AttendanceStatus.getStautsText(status) + " (Score: " + score + ")");
                    } else {
                        System.out.println(" Skipped - keeping existing record");
                    }
                } else {
                    // No existing attendance, create new one
                    displayStudent(studentNumber, student.getStudentName());
                    int status = getAttendanceInput();
                    double score = AttendanceStatus.getScore(status);
                    
                    AttendanceRecord record = new AttendanceRecord(
                        student.getStudentId(),
                        LocalDate.now(),
                        status,
                        score
                    );
                    
                    database.saveAttendance(record);
                    displayRecorded(AttendanceStatus.getStautsText(status), score);
                }
                
                studentNumber++;
            }
            
            displaySummary(students.size());
            
        } catch (Exception e) {
            displayError("Failed to take attendance: " + e.getMessage());
            e.printStackTrace();
        }
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
        System.out.println(" Recorded: " + statusText + " (Score: " + score + ")");
    }
    
    public void displaySummary(int studentCount) {
        System.out.println("\n===========================================");
        System.out.println("Attendance completed for " + studentCount + " students!");
        System.out.println("===========================================");
    }
    
    public void displayAllAttendance() {
        try {
            List<AttendanceRecord> records = database.getAllAttendance();
            
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
                    System.out.println(String.format("%-15s %-25s %-15s %-15s %-10.2f",
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
            
        } catch (Exception e) {
            displayError("Failed to retrieve attendance records: " + e.getMessage());
            e.printStackTrace();
        }
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
        try {
            if (database != null) {
                database.close();
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}