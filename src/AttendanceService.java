import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AttendanceService {
    private AttendanceDAO attendanceDAO;
    private StudentDAO studentDAO;

    public AttendanceService() throws SQLException {
        this.attendanceDAO = new AttendanceDAO();
        this.studentDAO = new StudentDAO();
    }

    public void takeAttendance() {
        Scanner scanner = new Scanner(System.in);
        try {
            displayHeader();
            LocalDate today = LocalDate.now();
            System.out.println("Date: " + today + "\n");
            
            List<Student> students = studentDAO.getAllStudents();
            int count = 0;
            
            for (Student student : students) {
                System.out.print(++count + ". " + student.getStudentName() + " - Enter status (1-4): ");
                int status = getValidStatus(scanner);
                double score = AttendanceStatus.calculateScore(status);
                
                AttendanceRecord record = new AttendanceRecord(
                    student.getStudentID(),
                    today.toString(),
                    status,
                    (int) score
                );
                
                attendanceDAO.recordAttendance(record);
                
                System.out.println("   → Recorded: " + AttendanceStatus.getStatusText(status) + 
                                   " (Score: " + score + ")\n");
            }
            
            displayFooter(count);
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

        public void viewAttendanceSummary(String date) {
        try {
            List<AttendanceRecord> records = attendanceDAO.getAttendanceByDate(date);
            
            System.out.println("\n========================================");
            System.out.println("Attendance Summary for " + date);
            System.out.println("========================================");
            
            double totalScore = 0;
            int studentCount = 0;
            
            for (AttendanceRecord record : records) {
                Student student = studentDAO.getStudentById(record.getStudentID());
                System.out.printf("%-30s %s (%.1f)\n", 
                    student.getStudentName(), 
                    AttendanceStatus.getStatusText(record.getStatus()), 
                    record.getScore());
                
                totalScore += record.getScore();
                studentCount++;
            }
            
            if (studentCount > 0) {
                double averageScore = totalScore / studentCount;
                System.out.println("========================================");
                System.out.printf("Average Attendance Score: %.2f%%\n", averageScore * 100);
                System.out.println("========================================");
            } else {
                System.out.println("No attendance records found for this date.");
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private void displayHeader() {
        System.out.println("========================================");
        System.out.println("     STUDENT ATTENDANCE SYSTEM");
        System.out.println("========================================");
        System.out.println("1 = Present (Score: 1.0)");
        System.out.println("2 = Late (Score: 0.8)");
        System.out.println("3 = Absent (Score: 0.0)");
        System.out.println("4 = Excused/Permission (Score: 0.5)");
        System.out.println("========================================\n");
    }
    private void displayFooter(int count) {
        System.out.println("========================================");
        System.out.println("Attendance recorded for " + count + " students!");
        System.out.println("========================================");
    }
    
    private int getValidStatus(Scanner scanner) {
        while (true) {
            try {
                int status = scanner.nextInt();
                if (AttendanceStatus.isValidStatus(status)) {
                    return status;
                }
                System.out.print("Invalid input! Enter 1-4: ");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.print("Invalid input! Enter 1-4: ");
            }
        }
    }
}