package Backend.src.owner;

import Backend.src.database.StudentInfoManager;
import Backend.src.database.MajorManager;

public class totalstudent {

    public static void main(String[] args) {
        // Create the studentInfo table if it doesn't exist
        StudentInfoManager.createStudentInfoTable();
        MajorManager.createDepartmentMajorTable();

        displayStudentStatistics();
    }

    private static void displayStudentStatistics() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("              STUDENT STATISTICS REPORT");
        System.out.println("=".repeat(70));

        // Get total student count
        int totalStudents = StudentInfoManager.getTotalStudentCount();
        if (totalStudents == 0) {
            System.out.println("\nNo students found in the system.");
            System.out.println("=".repeat(70));
            return;
        }

        System.out.println("\n" + "-".repeat(70));
        System.out.println("STUDENTS BY DEPARTMENT AND GENDER");
        System.out.println("-".repeat(70));
        System.out.printf("%-25s %-15s %-15s %-15s%n", "Department", "Total", "Male", "Female");
        System.out.println("-".repeat(70));

        // Departments to check
        String[] departments = { "GIC", "GIM", "GEE" };
        int grandTotalMale = 0;
        int grandTotalFemale = 0;

        for (String dept : departments) {
            int[] stats = StudentInfoManager.getStudentsByDepartmentAndGender(dept);
            if (stats != null && stats.length >= 3) {
                int total = stats[0];
                int males = stats[1];
                int females = stats[2];

                grandTotalMale += males;
                grandTotalFemale += females;

                String deptName = getDepartmentFullName(dept);
                System.out.printf("%-25s %-15d %-15d %-15d%n", deptName, total, males, females);
            }
        }

        System.out.println("-".repeat(70));
        System.out.printf("%-25s %-15d %-15d %-15d%n", "TOTAL", totalStudents, grandTotalMale, grandTotalFemale);
        System.out.println("=".repeat(70));

        // Display percentage breakdown
        System.out.println("\nGENDER PERCENTAGE BREAKDOWN:");
        System.out.println("-".repeat(70));
        if (totalStudents > 0) {
            double malePercentage = (grandTotalMale * 100.0) / totalStudents;
            double femalePercentage = (grandTotalFemale * 100.0) / totalStudents;

            System.out.printf("Male Students:   %d (%.2f%%)%n", grandTotalMale, malePercentage);
            System.out.printf("Female Students: %d (%.2f%%)%n", grandTotalFemale, femalePercentage);
        }
        System.out.println("=".repeat(70));
    }

    private static String getDepartmentFullName(String deptCode) {
        switch (deptCode) {
            case "GIC":
                return "GIC (General IT & Computing)";
            case "GIM":
                return "GIM (General IT & Management)";
            case "GEE":
                return "GEE (General Electrical & Engineering)";
            default:
                return deptCode;
        }
    }
}
