import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    private Connection connection;
    
    public AttendanceDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public boolean recordAttendance(AttendanceRecord record) throws SQLException {
        String query = "INSERT INTO attendance (student_id, attendance_date, status, score) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        
        pstmt.setInt(1, record.getStudentID());
        pstmt.setDate(2, Date.valueOf(record.getAttendanceDate()));
        pstmt.setInt(3, record.getStatus());
        pstmt.setDouble(4, record.getScore());
        
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();
        
        return rowsAffected > 0;
    }
    
    public List<AttendanceRecord> getAttendanceByDate(String date) throws SQLException {
        List<AttendanceRecord> records = new ArrayList<>();
        String query = "SELECT student_id, attendance_date, status, score FROM attendance WHERE attendance_date = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setDate(1, Date.valueOf(date));
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            AttendanceRecord record = new AttendanceRecord(
                rs.getInt("student_id"),
                rs.getDate("attendance_date").toString(),
                rs.getInt("status"),
                (int) rs.getDouble("score")
            );
            records.add(record);
        }
        
        rs.close();
        pstmt.close();
        return records;
    }
}
