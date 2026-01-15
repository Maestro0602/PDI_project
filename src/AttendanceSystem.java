import java.time.LocalDate;
import java.util.List;

public class AttendanceSystem {
    private DatabaseManager dbManager;
    private AttendanceUI ui;

    public AttendanceSystem()
    {
        this.ui = new AttendanceUI();
    }

    public void takeAttendance()
    {
        try
        {
            dbManager = new DatabaseManager();
            System.out.println("Connected to database successfully!");

            ui.displayHeader();

            List<Student> students = dbManager.getAllStudents();

            if(students.isEmpty())
            {
                System.out.println("No students found in the database.");
                return;
            }

            int studentCount = 0;
            LocalDate today = LocalDate.now();

            for(Student student : students)
            {
                studentCount++;

                ui.displayStudent(studentCount, student.getStudentName());
                int status = ui.getattendanceInput();

                double score = AttendanceStatus.getScore(status);
                String statusText = AttendanceStatus.getStautsText(status);

                AttendanceRecord record = new AttendanceRecord(
                    student.getStudentId(),
                    today,
                    status,
                    score
                );

                ui.displaySummary(studentCount);
            }
        } catch (Exception e)
        {
            ui.displayError(e.getMessage());
            e.printStackTrace();
        } finally 
        {
            try
            {
                if (dbManager != null)
                {
                    dbManager.close();
                    System.out.println("Database connection closed.");
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            ui.close();
        }
    }

    public static void main(String[] args)
    {
        AttendanceSystem system = new AttendanceSystem();
        system.takeAttendance();
    }
}
