package Backend.src.database;

import java.sql.*;

public class StudentInfoManager {

    /**
     * Create studentInfo table if it doesn't exist
     */
    public static void createStudentInfoTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS studentInfo (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "studentName VARCHAR(100) NOT NULL, " +
                        "studentID VARCHAR(50) NOT NULL UNIQUE, " +
                        "gender VARCHAR(10) NOT NULL, " +
                        "year VARCHAR(20) NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("studentInfo table created/verified successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating studentInfo table: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Save student information to database
     */
    public static boolean saveStudentInfo(String studentName, String studentID, String gender, String year) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO studentInfo (studentName, studentID, gender, year) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentName);
                pstmt.setString(2, studentID);
                pstmt.setString(3, gender);
                pstmt.setString(4, year);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error saving student information: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Get student information by student ID
     */
    public static String[] getStudentInfo(String studentID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT studentName, studentID, gender, year FROM studentInfo WHERE studentID = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentID);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new String[] {
                            rs.getString("studentName"),
                            rs.getString("studentID"),
                            rs.getString("gender"),
                            rs.getString("year")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting student information: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * Get all students information
     */
    public static void displayAllStudents() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT * FROM studentInfo ORDER BY studentName";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                System.out.println("\n" + "=".repeat(80));
                System.out.println("                           ALL STUDENTS INFORMATION");
                System.out.println("=".repeat(80));
                System.out.printf("%-5s %-25s %-15s %-10s %-10s%n", "NO", "Student Name", "Student ID", "Gender",
                        "Year");
                System.out.println("-".repeat(80));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-25s %-15s %-10s %-10s%n",
                            count++,
                            rs.getString("studentName"),
                            rs.getString("studentID"),
                            rs.getString("gender"),
                            rs.getString("year"));
                }

                if (!hasData) {
                    System.out.println("No student records found.");
                }
                System.out.println("=".repeat(80));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying students: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    /**
     * Update student information
     */
    public static boolean updateStudentInfo(String studentID, String studentName, String gender, String year) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "UPDATE studentInfo SET studentName = ?, gender = ?, year = ? WHERE studentID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentName);
                pstmt.setString(2, gender);
                pstmt.setString(3, year);
                pstmt.setString(4, studentID);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating student information: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Delete student by student ID
     */
    public static boolean deleteStudent(String studentID) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM studentInfo WHERE studentID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentID);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Check if student ID exists
     */
    public static boolean studentExists(String studentID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT studentID FROM studentInfo WHERE studentID = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentID);

                rs = pstmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking student existence: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }

    /**
     * Display students by department with proper join
     */
    public static void displayStudentsByDepartment(String department) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT si.studentName, si.studentID, dm.department " +
                        "FROM departmentmajor dm " +
                        "JOIN studentinfo si ON si.studentID = dm.stuID " +
                        "WHERE dm.department = ? " +
                        "ORDER BY si.studentName";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, department);
                rs = pstmt.executeQuery();

                System.out.println("\n" + "=".repeat(80));
                System.out.println("                    STUDENTS IN " + department + " DEPARTMENT");
                System.out.println("=".repeat(80));
                System.out.printf("%-5s %-25s %-15s %-20s%n", "NO", "Student Name", "Student ID", "Department");
                System.out.println("-".repeat(80));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-25s %-15s %-20s%n",
                            count++,
                            rs.getString("studentName"),
                            rs.getString("studentID"),
                            rs.getString("department"));
                }

                if (!hasData) {
                    System.out.println("No students found in " + department + " department.");
                }

                System.out.println("=".repeat(80));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying students by department: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Close database resources safely
     */
    private static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
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
