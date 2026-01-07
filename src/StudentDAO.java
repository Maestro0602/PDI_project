import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection connection;
    
    public StudentDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
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
    
    public Student getStudentById(int studentId) throws SQLException {
        String query = "SELECT student_id, student_name FROM students WHERE student_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, studentId);
        ResultSet rs = pstmt.executeQuery();
        
        Student student = null;
        if (rs.next()) {
            student = new Student(rs.getInt("student_id"), rs.getString("student_name"));
        }
        
        rs.close();
        pstmt.close();
        return student;
    }
}