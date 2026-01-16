import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;
    
    public DatabaseManager() throws ClassNotFoundException, SQLException {
        Class.forName(DatabaseConfig.JDBC_DRIVER);
        this.connection = DriverManager.getConnection(
            DatabaseConfig.DB_URL,
            DatabaseConfig.DB_USER,
            DatabaseConfig.DB_PASSWORD
        );
        // Disable auto-commit to have better control
        connection.setAutoCommit(true);
    }
    
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT student_id, student_name FROM students ORDER BY student_name";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            int id = rs.getInt("student_id");
            String name = rs.getString("student_name");
            students.add(new Student(id, name));
        }
        
        rs.close();
        stmt.close();
        
        return students;
    }
    
    public List<AttendanceRecord> getAllAttendance() throws SQLException {
        List<AttendanceRecord> records = new ArrayList<>();
        String query = "SELECT a.attendance_id, a.student_id, s.student_name, a.attendance_date, a.status, a.score " +
                       "FROM attendance a " +
                       "JOIN students s ON a.student_id = s.student_id " +
                       "ORDER BY a.attendance_date DESC, s.student_name";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            int attendanceId = rs.getInt("attendance_id");
            int studentId = rs.getInt("student_id");
            String studentName = rs.getString("student_name");
            LocalDate date = rs.getDate("attendance_date").toLocalDate();
            int status = rs.getInt("status");
            double score = rs.getDouble("score");
            
            records.add(new AttendanceRecord(attendanceId, studentId, studentName, date, status, score));
        }
        
        rs.close();
        stmt.close();
        
        return records;
    }
    
    public void saveAttendance(AttendanceRecord record) throws SQLException {
        String query = "INSERT INTO attendance (student_id, attendance_date, status, score) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            pstmt = connection.prepareStatement(query);
            
            pstmt.setInt(1, record.getStudentId());
            pstmt.setDate(2, Date.valueOf(record.getAttendanceDate()));
            pstmt.setInt(3, record.getStatus());
            pstmt.setDouble(4, record.getScore());
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Debug output
            System.out.println("  [DEBUG] Rows inserted: " + rowsAffected);
            
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert attendance record!");
            }
            
        } catch (SQLException e) {
            System.err.println("  [ERROR] Failed to save attendance: " + e.getMessage());
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
