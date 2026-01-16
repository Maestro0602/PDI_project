import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager
{
    private Connection connection;

    public DatabaseManager() throws ClassNotFoundException, SQLException
    {
        Class.forName(DatabaseConfig.JDBC_DRIVER);
        this.connection = DriverManager.getConnection(
            DatabaseConfig.DB_URL,
            DatabaseConfig.DB_USER,
            DatabaseConfig.DB_PASSWORD
        );
        connection.setAutoCommit(true);
    }

    public List<Student> getAllStudents() throws SQLException
    {
        List<Student> students = new ArrayList();
        String query = "SELECT student_id, student_name FROM students ORDER BY student_name";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next())
        {
            int id = rs.getInt("student_id");
            String name = rs.getString("student_name");
            students.add(new Student(id, name));
        }

        rs.close();
        stmt.close();

        return students;
    }

    public void saveAttendance(AttendanceRecord record) throws SQLException
    {
        String query = "INSERT INTO attendance (student_id, attendance_date, status, score) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;

        try
        {
            pstmt = connection.prepareStatement(query);
            
            pstmt.setInt(1, record.getStudentId());
            pstmt.setDate(2, Date.valueOf(record.getAttendaceDate()));
            pstmt.setInt(3, record.getStatus());
            pstmt.setDouble(4, record.getScore());
            
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("  [DEBUG] Rows inserted: " + rowsAffected);

            if(rowsAffected == 0)
            {
                throw new SQLException("Failed to insert attendance record!");
            }
        }
        catch (Exception e)
        {
            System.err.println("  [ERROR] Failed to save attendance: " + e.getMessage());
            throw e;
        }
        finally
        {
            if (pstmt != null)
            {
                pstmt.close();
            }
        }
    }

    public void close() throws SQLException
    {
        if(connection != null && !connection.isClosed())
        {
            connection.close();
        }
    }
}