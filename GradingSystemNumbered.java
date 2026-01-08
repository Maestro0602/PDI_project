import java.sql.*;
import java.util.*;

public class GradingSystemNumbered {

    static final String URL = "jdbc:mysql://localhost:3306/student_management";
    static final String USER = "root"; // your MySQL username
    static final String PASSWORD = "P@ssw0rd"; // your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Connected to MySQL!");

            System.out.print("Enter Student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // Load student info
            String studentQuery = "SELECT * FROM students WHERE student_id = ?";
            PreparedStatement psStudent = conn.prepareStatement(studentQuery);
            psStudent.setInt(1, studentId);
            ResultSet rsStudent = psStudent.executeQuery();

            if (!rsStudent.next()) {
                System.out.println("❌ Student not found!");
                return;
            }

            String studentName = rsStudent.getString("name");
            String department = rsStudent.getString("department");
            String major = rsStudent.getString("major");

            System.out.println("\nStudent: " + studentName);
            System.out.println("Department: " + department);
            System.out.println("Major: " + major);

            // Load courses for this student
            String coursesQuery = "SELECT course_name FROM courses WHERE department = ? AND major = ?";
            PreparedStatement psCourses = conn.prepareStatement(coursesQuery);
            psCourses.setString(1, department);
            psCourses.setString(2, major);
            ResultSet rsCourses = psCourses.executeQuery();

            List<String> courseNames = new ArrayList<>();
            while (rsCourses.next()) {
                courseNames.add(rsCourses.getString("course_name"));
            }

            if (courseNames.isEmpty()) {
                System.out.println("No courses found!");
                return;
            }

            // Map number → course
            Map<Integer, String> courseMap = new HashMap<>();
            for (int i = 0; i < courseNames.size(); i++) {
                courseMap.put(i + 1, courseNames.get(i));
            }

            // Flexible score input by number
            while (true) {
                System.out.println("\nCourses for this student:");
                for (int i = 1; i <= courseNames.size(); i++) {
                    System.out.println(i + ". " + courseMap.get(i));
                }

                System.out.print("\nEnter course number to enter/update score (0 to finish): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (choice == 0) break;

                if (!courseMap.containsKey(choice)) {
                    System.out.println("❌ Invalid course number. Try again.");
                    continue;
                }

                String courseName = courseMap.get(choice);
                System.out.print("Enter score for " + courseName + " (0-100): ");
                double score = scanner.nextDouble();
                scanner.nextLine(); // consume newline

                // Calculate grade & grade point
                String grade;
                double gradePoint;
                if (score >= 85) { grade = "A"; gradePoint = 4.0; }
                else if (score >= 75) { grade = "B"; gradePoint = 3.0; }
                else if (score >= 65) { grade = "C"; gradePoint = 2.0; }
                else if (score >= 50) { grade = "D"; gradePoint = 1.0; }
                else { grade = "F"; gradePoint = 0.0; }

                // Insert or update grade
                String sql = "INSERT INTO grades (student_id, student_name, course_name, score, grade, grade_point) " +
                        "VALUES (?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE score = VALUES(score), grade = VALUES(grade), grade_point = VALUES(grade_point)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, studentId);
                ps.setString(2, studentName);
                ps.setString(3, courseName);
                ps.setDouble(4, score);
                ps.setString(5, grade);
                ps.setDouble(6, gradePoint);
                ps.executeUpdate();

                System.out.println(courseName + " → Score: " + score + ", Grade: " + grade + ", Grade Point: " + gradePoint);
            }

            // Calculate GPA
            String gpaQuery = "SELECT AVG(grade_point) AS gpa FROM grades WHERE student_id = ?";
            PreparedStatement psGpa = conn.prepareStatement(gpaQuery);
            psGpa.setInt(1, studentId);
            ResultSet rsGpa = psGpa.executeQuery();
            if (rsGpa.next()) {
                double gpa = rsGpa.getDouble("gpa");
                System.out.printf("\nCurrent GPA: %.2f\n", gpa);

                String status;
                if (gpa >= 3.5) status = "Excellent";
                else if (gpa >= 3.0) status = "Good";
                else if (gpa >= 2.0) status = "Pass";
                else status = "Probation";

                System.out.println("Academic Status: " + status);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
