import java.time.LocalDate;
import java.util.Scanner;

public class AttendanceUI {
    private Scanner scanner;

    public AttendanceUI()
    {
        this.scanner = new Scanner(System.in);
    }

    public void displayHeader()
    {
        System.out.println("===========================================");
        System.out.println("    STUDENT ATTENDANCE SYSTEM");
        System.out.println("===========================================");
        System.out.println("Date: " + LocalDate.now());
        System.out.println("\nAttendance Options:");
        System.out.println("1 - Present (Score: 1.0)");
        System.out.println("2 - Late (Score: 0.8)");
        System.out.println("3 - Absent (Score: 0.0)");
        System.out.println("4 - Excused/Permission (Score: 0.5)");
        System.out.println("===========================================\n");
    }

    public void displayStudent(int number, String name)
    {
        System.out.println("\nStudent #" + number + ": " + name);
        System.out.print("Enter attendance (1-4): ");
    }

    public int getattendanceInput()
    {
        int status = 0;
        boolean validInput = false;

        while (!validInput)
        {
            try
            {
                status = scanner.nextInt();
                if(AttendanceStatus.isValid(status))
                {
                    validInput = true;
                } else
                {
                    System.out.print("Invalid input! Please enter 1-4: ");
                }
            } catch (Exception e)
            {
                System.out.print("Invalid input! Please enter 1-4: ");
                scanner.next();
            }
        }

        return status;
    }

    public void displaySummary(int studentCount)
    {
        System.out.println("\n===========================================");
        System.out.println("Attendance completed for " + studentCount + " students!");
        System.out.println("===========================================");
    }

    public void displayError(String message)
    {
        System.err.println("Error: " + message);
    }

    public void close()
    {
        scanner.close();
    }
}
