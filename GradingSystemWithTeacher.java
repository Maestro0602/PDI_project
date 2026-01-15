import java.sql.*;
import java.util.*;

public class GradingSystemWithTeacher {

    static final String URL = "jdbc:mysql://localhost:3306/student_management";
    static final String USER = "root";
    static final String PASSWORD = "P@ssw0rd";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Connected to MySQL!");

            // ===== TEACHER =====
            System.out.print("Enter Teacher ID: ");
            String teacherId = scanner.nextLine();

            String teacherSql = "SELECT * FROM teachers WHERE teacher_id = ?";
            PreparedStatement psTeacher = conn.prepareStatement(teacherSql);
            psTeacher.setString(1, teacherId);
            ResultSet rsTeacher = psTeacher.executeQuery();

            if (!rsTeacher.next()) {
                System.out.println("❌ Teacher not found!");
                return;
            }

            String teacherName = rsTeacher.getString("teacher_name");
            String teacherDept = rsTeacher.getString("department");
            String teacherMajor = rsTeacher.getString("major");

            System.out.println("\nTeacher: " + teacherName);
            System.out.println("Department: " + teacherDept);
            System.out.println("Major: " + teacherMajor);

            // ===== LOAD TEACHER COURSES (JOIN) =====
            String courseSql =
                "SELECT c.course_id, c.course_name " +
                "FROM teacher_courses tc " +
                "JOIN courses c ON tc.course_id = c.course_id " +
                "WHERE tc.teacher_id = ?";

            PreparedStatement psCourses = conn.prepareStatement(courseSql);
            psCourses.setString(1, teacherId);
            ResultSet rsCourses = psCourses.executeQuery();

            Map<Integer, Integer> numberToCourseId = new HashMap<>();
            Map<Integer, String> numberToCourseName = new HashMap<>();

            int index = 1;
            while (rsCourses.next()) {
                numberToCourseId.put(index, rsCourses.getInt("course_id"));
                numberToCourseName.put(index, rsCourses.getString("course_name"));
                index++;
            }

            if (numberToCourseId.isEmpty()) {
                System.out.println("❌ No courses assigned to this teacher.");
                return;
            }

            // ===== STUDENT =====
            System.out.print("\nEnter Student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();

            String studentSql = "SELECT * FROM students WHERE student_id = ?";
            PreparedStatement psStudent = conn.prepareStatement(studentSql);
            psStudent.setInt(1, studentId);
            ResultSet rsStudent = psStudent.executeQuery();

            if (!rsStudent.next()) {
                System.out.println("❌ Student not found!");
                return;
            }

            String studentName = rsStudent.getString("name");
            String studentDept = rsStudent.getString("department");
            String studentMajor = rsStudent.getString("major");

            System.out.println("\nStudent: " + studentName);
            System.out.println("Department: " + studentDept);
            System.out.println("Major: " + studentMajor);

            // ===== PERMISSION CHECK =====
            if (!teacherDept.equals(studentDept) || !teacherMajor.equals(studentMajor)) {
                System.out.println("⛔ You are NOT allowed to grade this student.");
                return;
            }

            // ===== GRADING LOOP =====
            while (true) {
                System.out.println("\nCourses you can grade:");
                for (int i : numberToCourseName.keySet()) {
                    System.out.println(i + ". " + numberToCourseName.get(i));
                }

                System.out.print("Choose course number (0 to finish): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) break;
                if (!numberToCourseId.containsKey(choice)) continue;

                int courseId = numberToCourseId.get(choice);
                String courseName = numberToCourseName.get(choice);

                System.out.print("Enter score for " + courseName + " (0–100): ");
                double score = scanner.nextDouble();
                scanner.nextLine();

                String grade;
                double gradePoint;
                if (score >= 85) { grade = "A"; gradePoint = 4.0; }
                else if (score >= 75) { grade = "B"; gradePoint = 3.0; }
                else if (score >= 65) { grade = "C"; gradePoint = 2.0; }
                else if (score >= 50) { grade = "D"; gradePoint = 1.0; }
                else { grade = "F"; gradePoint = 0.0; }

                String insertSql =
                    "INSERT INTO grades (student_id, student_name, course_name, score, grade, grade_point) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE score=?, grade=?, grade_point=?";

                PreparedStatement ps = conn.prepareStatement(insertSql);
                ps.setInt(1, studentId);
                ps.setString(2, studentName);
                ps.setString(3, courseName);
                ps.setDouble(4, score);
                ps.setString(5, grade);
                ps.setDouble(6, gradePoint);
                ps.setDouble(7, score);
                ps.setString(8, grade);
                ps.setDouble(9, gradePoint);

                ps.executeUpdate();
                System.out.println("✅ Saved: " + courseName + " → " + grade);
            }

            // ===== GPA =====
            String gpaSql = "SELECT AVG(grade_point) AS gpa FROM grades WHERE student_id = ?";
            PreparedStatement psGpa = conn.prepareStatement(gpaSql);
            psGpa.setInt(1, studentId);
            ResultSet rsGpa = psGpa.executeQuery();

            if (rsGpa.next()) {
                double gpa = rsGpa.getDouble("gpa");
                System.out.printf("\nCurrent GPA: %.2f\n", gpa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

