package database;

import Backend.src.attendance.AttendanceRecord;
import Backend.src.department.Student;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Attendance {
    private Connection connection;
    
    public static class AccountDatabaseConnection {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/login_system";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "MRHENGXD123";
        private static final String DB_ROOT_URL = "jdbc:mysql://localhost:3306";

        /**
         * Create account database if it doesn't exist
         */
        public static void createAccountDatabase() {
            Connection conn = null;
            Statement stmt = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_ROOT_URL, DB_USER, DB_PASSWORD);
                if (conn != null) {
                    String sql = "CREATE DATABASE IF NOT EXISTS account";
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    // System.out.println(" Account database initialized successfully");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Error creating account database: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        }

        /**
         * Connect to account database
         */
        public static Connection connectDB() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                return conn;
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
                return null;
            } catch (SQLException e) {
                System.out.println("Account database connection error: " + e.getMessage());
                return null;
            }
        }

        public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    /**
     * Constructor - initializes connection and creates attendance table if needed
     */
    public Attendance() throws SQLException {
        this.connection = AccountDatabaseConnection.connectDB();
        if (this.connection != null) {
            createAttendanceTableIfNotExist();
        }
    }
    
    /**
     * Create attendance table if it doesn't exist
     */
    private void createAttendanceTableIfNotExist() throws SQLException {
        Statement stmt = null;
        
        try {
            stmt = connection.createStatement();
            
            // Create attendance table (without attendance_id)
            String createAttendanceTable = "CREATE TABLE IF NOT EXISTS attendance (" +
                    "student_id VARCHAR(50) NOT NULL, " +
                    "attendance_date DATE NOT NULL, " +
                    "status INT NOT NULL, " +
                    "score DOUBLE NOT NULL, " +
                    "PRIMARY KEY (student_id, attendance_date)" +
                    ")";
            stmt.executeUpdate(createAttendanceTable);
            
            System.out.println("Attendance table created/verified successfully");
            
        } catch (SQLException e) {
            System.err.println("Error creating attendance table: " + e.getMessage());
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    /**
     * Get all students from studentinfo table
     */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT studentID, studentName FROM studentinfo ORDER BY studentName";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            String id = rs.getString("studentID");
            String name = rs.getString("studentName");
            students.add(new Student(id, name));
        }
        
        rs.close();
        stmt.close();
        
        return students;
    }
    
    /**
     * Get all attendance records
     */
    public List<AttendanceRecord> getAllAttendance() throws SQLException {
        List<AttendanceRecord> records = new ArrayList<>();
        String query = "SELECT a.student_id, s.studentName, a.attendance_date, a.status, a.score " +
                       "FROM attendance a " +
                       "JOIN studentinfo s ON a.student_id = s.studentID " +
                       "ORDER BY a.attendance_date DESC, s.studentName";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            String studentId = rs.getString("student_id");
            String studentName = rs.getString("studentName");
            LocalDate date = rs.getDate("attendance_date").toLocalDate();
            int status = rs.getInt("status");
            double score = rs.getDouble("score");
            
            records.add(new AttendanceRecord(studentId, studentName, date, status, score));
        }
        
        rs.close();
        stmt.close();
        
        return records;
    }
    
    /**
     * Check if attendance already exists for a student on a specific date
     */
    public AttendanceRecord getExistingAttendance(String studentId, LocalDate date) throws SQLException {
        String query = "SELECT a.student_id, s.studentName, a.attendance_date, a.status, a.score " +
                       "FROM attendance a " +
                       "JOIN studentinfo s ON a.student_id = s.studentID " +
                       "WHERE a.student_id = ? AND a.attendance_date = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, studentId);
        pstmt.setDate(2, Date.valueOf(date));
        
        ResultSet rs = pstmt.executeQuery();
        
        AttendanceRecord record = null;
        if (rs.next()) {
            String studentName = rs.getString("studentName");
            int status = rs.getInt("status");
            double score = rs.getDouble("score");
            record = new AttendanceRecord(studentId, studentName, date, status, score);
        }
        
        rs.close();
        pstmt.close();
        
        return record;
    }
    
    /**
     * Update existing attendance record
     */
    public void updateAttendance(AttendanceRecord record) throws SQLException {
        String query = "UPDATE attendance SET status = ?, score = ? WHERE student_id = ? AND attendance_date = ?";
        PreparedStatement pstmt = null;
        
        try {
            pstmt = connection.prepareStatement(query);
            
            pstmt.setInt(1, record.getStatus());
            pstmt.setDouble(2, record.getScore());
            pstmt.setString(3, record.getStudentId());
            pstmt.setDate(4, Date.valueOf(record.getAttendanceDate()));
            
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("  [DEBUG] Rows updated: " + rowsAffected);
            
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update attendance record!");
            }
            
        } catch (SQLException e) {
            System.err.println("  [ERROR] Failed to update attendance: " + e.getMessage());
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    
    /**
     * Save attendance record (INSERT only, does not update)
     */
    public void saveAttendance(AttendanceRecord record) throws SQLException {
        String query = "INSERT INTO attendance (student_id, attendance_date, status, score) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            pstmt = connection.prepareStatement(query);
            
            pstmt.setString(1, record.getStudentId());
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
    
    /**
     * Close database connection
     */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}