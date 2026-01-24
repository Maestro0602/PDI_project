package Backend.src.report;

import Backend.main.MainPageTeacher;
import Backend.src.database.Genreport;
import java.util.Scanner;
public class GenerateStudentReport {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("=====================================");
            System.out.println("        STUDENT REPORT SYSTEM         ");
            System.out.println("=====================================");
            System.out.print("Enter Student ID (0 to exit): ");

            String studentID = scanner.nextLine().trim();

            // ✅ Exit condition
            if (studentID.equals("0")) {
                System.out.println("\nExit to Teacher Main Page.");
                MainPageTeacher.main(null);
                break;
            }

            Report report = Genreport.getInformationByStudentID(studentID);

            if (report == null) {
                System.out.println("\nNo report found for Student ID: " + studentID);
                continue; // ask for another ID
            }

            // ===== STUDENT INFORMATION =====
            System.out.println("\n-------------------------------------");
            System.out.println("           STUDENT PROFILE            ");
            System.out.println("-------------------------------------");
            System.out.println("Student ID   : " + report.getStudentID());
            System.out.println("Name         : " + report.getStudentName());
            System.out.println("Gender       : " + report.getGender());
            System.out.println("Year         : " + report.getYear());
            System.out.println("Department   : " + report.getDepartment());
            System.out.println("Major        : " + report.getMajor());

            // ===== COURSE REPORT =====
            System.out.println("\n-------------------------------------");
            System.out.println("           COURSE RESULTS             ");
            System.out.println("-------------------------------------");

            System.out.printf("%-12s %-50s %-8s %-6s%n",
                    "Course ID", "Course Name", "Score", "Grade");
            System.out.println("----------------------------------------------------------------------");

            double totalScore = 0.0;

            for (int i = 0; i < report.getCourseIDs().size(); i++) {

                double score = report.getScores().get(i);
                totalScore += score;

                System.out.printf(
                        "%-12s %-50s %-8.2f %-6s%n",
                        report.getCourseIDs().get(i),
                        report.getCourseNames().get(i),
                        score,
                        report.getGrades().get(i)
                );
            }

            System.out.println("--------------------------------------------------------------");

            // ===== GPA CALCULATION =====
            double gpa = (totalScore / 400.0) * 4.0;

            // Cap GPA at 4.00
            if (gpa > 4.0) {
                gpa = 4.0;
            }

            System.out.printf("GPA : %.2f%n", gpa);
            System.out.println("=====================================\n");
        }

        scanner.close();
    }
}
