package Backend.src.grading;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;
import Backend.main.MainPageTeacher;
import Backend.src.database.GradingManagement;

public class Grading {

    public static void main(String[] args) {

    GradingManagement.createUserTable();
    GradingManagement dbManager = new GradingManagement();
    Scanner scanner = new Scanner(System.in);

    try (Connection conn = GradingManagement.connectDB()) {

        while (true) { // ===== MAIN LOOP =====

            System.out.println("=".repeat(30));
            System.out.println("  Grading Management System");
            System.out.println("=".repeat(30));
            System.out.print("Enter Teacher ID (0 to exit): ");
            String teacherId = scanner.nextLine().trim();

            if (teacherId.equals("0")) {
                System.out.println("👋 Exiting system...");
                MainPageTeacher.main(null);
                break;
            }

            Map<String, String> teacher = dbManager.getTeacherInfo(conn, teacherId);
            if (teacher == null) {
                System.out.println(" Teacher not found!\n");
                continue; // ask teacher again
            }

            System.out.println("\n=== TEACHER INFORMATION ===");
            System.out.println("Teacher ID: " + teacherId);
            System.out.println("Department: " + teacher.get("department"));
            System.out.println("Major: " + teacher.get("major"));
            System.out.println("============================\n");

            // ===== STUDENT LOOP =====
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine().trim();

            Map<String, String> student = dbManager.getStudentInfo(conn, studentId);
            if (student == null) {
                System.out.println(" Student not found!\n");
                continue; // restart from teacher input
            }

            System.out.println("\n=== STUDENT INFORMATION ===");
            System.out.println("Student ID: " + studentId);
            System.out.println("Department: " + student.get("department"));
            System.out.println("Major: " + student.get("major"));
            System.out.println("============================\n");

            // ===== PERMISSION CHECK =====
            if (!dbManager.canTeacherGradeStudent(teacher, student)) {
                System.out.println(" ACCESS DENIED!");
                System.out.println("Teacher's Department/Major does NOT match Student's.\n");
                continue; // restart from teacher input
            }

            System.out.println(" Permission granted.\n");

            // ===== COURSES =====
            Map<Integer, Map<String, Object>> courses =
                    dbManager.getTeacherCourses(conn, teacherId);

            if (courses.isEmpty()) {
                System.out.println(" No courses assigned!\n");
                continue;
            }

            System.out.println("=== COURSES AVAILABLE FOR GRADING ===");
            for (Map.Entry<Integer, Map<String, Object>> entry : courses.entrySet()) {
                System.out.println(
                        entry.getKey() + ". " +
                        entry.getValue().get("course_Id") + " - " +
                        entry.getValue().get("course_name")
                );
            }
            System.out.println("======================================\n");

            // ===== GRADING LOOP =====
            while (true) {
                System.out.print("Select course option (0 to finish): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) {
                    System.out.println("✅ Grading session completed!\n");
                    MainPageTeacher.main(null);
                    break;
                }

                if (!courses.containsKey(choice)) {
                    System.out.println(" Invalid option!\n");
                    continue;
                }

                String courseId = (String) courses.get(choice).get("course_Id");
                String courseName = (String) courses.get(choice).get("course_name");

                System.out.print("Enter score for " + courseName + " [0-100]: ");
                double score = scanner.nextDouble();
                scanner.nextLine();

                if (score < 0 || score > 100) {
                    System.out.println(" Invalid score!\n");
                    continue;
                }

                Map<String, Object> gradeInfo = dbManager.calculateGrade(score);
                String grade = (String) gradeInfo.get("grade");

                dbManager.saveGrade(conn, studentId, courseId, score, grade);

                System.out.println(" Grade saved!");
                System.out.println("   Course: " + courseId);
                System.out.println("   Score: " + score);
                System.out.println("   Grade: " + grade + "\n");
            }
        }

    } catch (SQLException e) {
        System.err.println(" Database error: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println(" Error: " + e.getMessage());
        e.printStackTrace();
    } finally {
        scanner.close();
    }
}

}
