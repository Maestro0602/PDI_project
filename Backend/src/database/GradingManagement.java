package database;

import java.sql.*;
import java.util.*;

public class GradingManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/login_system";
    private static final String USER = "root";
    private static final String PASSWORD = "MRHENGXD123";

    // Get database connection
    // ===== CONNECT DATABASE =====
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===== CREATE TABLE =====
    public static void createUserTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = connectDB();
             String sql = "CREATE TABLE IF NOT EXISTS Grading (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "studentID Char(100) NOT NULL, " +
                "courseID Char(100) NOT NULL, " +
                "Score DOUBLE, " +
                "grade VARCHAR(5))";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    // Get teacher info from teacherinfo table
    public Map<String, String> getTeacherInfo(Connection conn, String teacherId) throws SQLException {
        String sql = "SELECT * FROM teacherinfo WHERE teacherID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, teacherId);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return null;
        }

        Map<String, String> teacher = new HashMap<>();
        teacher.put("teacherID", rs.getString("teacherID"));
        teacher.put("department", rs.getString("department"));
        teacher.put("major", rs.getString("major"));
        return teacher;
    }

    // Get teacher's courses from teacher_course table with course names (JOIN)
    public Map<Integer, Map<String, Object>> getTeacherCourses(Connection conn, String teacherId) throws SQLException {
        String sql = "SELECT tc.course_id, c.course_name " +
                "FROM teacher_course tc " +
                "JOIN course c ON tc.course_id = c.course_id " +
                "WHERE tc.teacherID = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, teacherId);
        ResultSet rs = ps.executeQuery();

        Map<Integer, Map<String, Object>> courses = new HashMap<>();
        int index = 1;
        while (rs.next()) {
            Map<String, Object> course = new HashMap<>();
            course.put("course_Id", rs.getString("course_id"));
            course.put("course_name", rs.getString("course_name"));
            courses.put(index, course);
            index++;
        }

        return courses;
    }

    // Get student info from departmentmajor table
    public Map<String, String> getStudentInfo(Connection conn, String studentId) throws SQLException {
        String sql = "SELECT stuId, department, major FROM departmentmajor WHERE stuId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return null;
        }

        Map<String, String> student = new HashMap<>();
        student.put("department", rs.getString("department"));
        student.put("major", rs.getString("major"));
        return student;
    }

    // Check if teacher can grade student based on department and major
    public boolean canTeacherGradeStudent(Map<String, String> teacher, Map<String, String> student) {
        return teacher.get("department").equals(student.get("department")) &&
                teacher.get("major").equals(student.get("major"));
    }

    // Calculate grade and grade point based on score
    public Map<String, Object> calculateGrade(double score) {
        Map<String, Object> result = new HashMap<>();
        String grade;
        double gradePoint;

        if (score >= 85) {
            grade = "A";
            gradePoint = 4.0;
        } else if (score >= 75) {
            grade = "B";
            gradePoint = 3.0;
        } else if (score >= 65) {
            grade = "C";
            gradePoint = 2.0;
        } else if (score >= 50) {
            grade = "D";
            gradePoint = 1.0;
        } else {
            grade = "F";
            gradePoint = 0.0;
        }

        result.put("grade", grade);
        result.put("gradePoint", gradePoint);
        return result;
    }

    // Save grade to database (updated to accept String courseId)
    public void saveGrade(Connection conn, String studentId, String courseId, double score, String grade)
            throws SQLException {
        String sql = "INSERT INTO Grading (studentID, courseID, Score, grade) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE Score=?, grade=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, studentId);
        ps.setString(2, courseId);
        ps.setDouble(3, score);
        ps.setString(4, grade);
        ps.setDouble(5, score);
        ps.setString(6, grade);

        ps.executeUpdate();
    }

    // Calculate GPA for a student
    public double calculateGPA(Connection conn, String studentId) throws SQLException {
        String sql = "SELECT AVG(grade_point) AS gpa FROM Grading WHERE studentID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble("gpa");
        }
        return 0.0;
    }
    // Close database resources
    private static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
    try {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}