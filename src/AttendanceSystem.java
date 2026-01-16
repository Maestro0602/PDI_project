import java.time.LocalDate;
import java.util.List;

public class AttendanceSystem {
    private DatabaseManager dbManager;
    private AttendanceUI ui;
    
    public AttendanceSystem() {
        this.ui = new AttendanceUI();
    }
    
    public void run() {
        try {
            // Initialize database connection
            dbManager = new DatabaseManager();
            System.out.println("Connected to database successfully!");
            
            boolean running = true;
            
            while (running) {
                int choice = ui.displayMainMenu();
                
                switch (choice) {
                    case 1:
                        takeAttendance();
                        break;
                    case 2:
                        displayAllAttendance();
                        break;
                    case 3:
                        running = false;
                        ui.displayExitMessage();
                        break;
                }
            }
            
        } catch (Exception e) {
            ui.displayError(e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            try {
                if (dbManager != null) {
                    dbManager.close();
                    System.out.println("Database connection closed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ui.close();
        }
    }
    
    private void takeAttendance() {
        try {
            // Display header
            ui.displayHeader();
            
            // Get all students
            List<Student> students = dbManager.getAllStudents();
            
            if (students.isEmpty()) {
                System.out.println("No students found in the database.");
                return;
            }
            
            // Process each student
            int studentCount = 0;
            LocalDate today = LocalDate.now();
            
            for (Student student : students) {
                studentCount++;
                
                // Display student and get attendance
                ui.displayStudent(studentCount, student.getStudentName());
                int status = ui.getAttendanceInput();
                
                // Calculate score
                double score = AttendanceStatus.getScore(status);
                String statusText = AttendanceStatus.getStautsText(status);
                
                // Create and save attendance record
                AttendanceRecord record = new AttendanceRecord(
                    student.getStudentId(),
                    today,
                    status,
                    score
                );
                
                dbManager.saveAttendance(record);
                
                // Display confirmation
                ui.displayRecorded(statusText, score);
            }
            
            // Display summary
            ui.displaySummary(studentCount);
            
        } catch (Exception e) {
            ui.displayError("Failed to take attendance: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void displayAllAttendance() {
        try {
            List<AttendanceRecord> records = dbManager.getAllAttendance();
            ui.displayAllAttendance(records);
        } catch (Exception e) {
            ui.displayError("Failed to display attendance: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        AttendanceSystem system = new AttendanceSystem();
        system.run();
    }
}