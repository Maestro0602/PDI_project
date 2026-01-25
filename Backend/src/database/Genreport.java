package Backend.src.database;

import Backend.src.report.Report;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Genreport {
     private static final String URL = "jdbc:mysql://localhost:3306/login_system";
    private static final String USER = "root";
    private static final String PASSWORD = "MRHENGXD123";

public static void createDatabase() {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS login_system");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public static Report getInformationByStudentID(String inputStudentID) {

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    String studentID = null;
    String studentName = null;
    String gender = null;
    String year = null;
    String department = null;
    String major = null;

    List<String> courseIDs = new ArrayList<>();
    List<String> courseNames = new ArrayList<>();
    List<Double> scores = new ArrayList<>();
    List<String> grades = new ArrayList<>();

    try {
        conn = connectDB();

        String sql = """
            SELECT 
                s.studentID,
                s.studentname,
                s.gender,
                s.year,
                d.department,
                d.major,
                c.course_id AS courseID,
                c.course_name,
                g.Score,
                g.grade
            FROM studentinfo s
            JOIN departmentmajor d ON s.studentID = d.stuId
            JOIN grading g ON s.studentID = g.studentID
            JOIN course c ON c.course_id = g.courseID
            WHERE s.studentID = ?
        """;

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, inputStudentID);

        rs = pstmt.executeQuery();

        while (rs.next()) {
            studentID = rs.getString("studentID");
            studentName = rs.getString("studentname");
            gender = rs.getString("gender");
            year = rs.getString("year");
            department = rs.getString("department");
            major = rs.getString("major");

            courseIDs.add(rs.getString("courseID"));
            courseNames.add(rs.getString("course_name"));
            scores.add(rs.getDouble("Score"));
            grades.add(rs.getString("grade"));
        }

        if (studentID == null) {
            return null; // No data found
        }

        return new Report(
                studentID, studentName, gender, year,
                department, major,
                courseIDs, courseNames, scores, grades
        );

    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    } finally {
        closeResources(conn, pstmt, rs);
    }
}
    
      public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
