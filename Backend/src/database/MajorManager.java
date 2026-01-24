package Backend.src.database;

import Backend.src.course.Course;
import java.sql.*;

public class MajorManager {

    /**
     * Create departmentMajor table if it doesn't exist
     */
    public static void createDepartmentMajorTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS departmentMajor (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "stuId VARCHAR(50) NOT NULL, " +
                        "department VARCHAR(50) NOT NULL, " +
                        "major VARCHAR(100) NOT NULL, " +
                        "Course1 VARCHAR(100) NOT NULL, " +
                        "Course2 VARCHAR(100) NOT NULL, " +
                        "Course3 VARCHAR(100) NOT NULL, " +
                        "Course4 VARCHAR(100) NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                // System.out.println("departmentMajor table created/verified successfully.");
            }
        } catch (SQLException e) {
            // System.out.println("Error creating departmentMajor table: " +
            // e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Save student department and major to database along with 4 courses
     */
    public static boolean saveDepartmentMajor(String stuId, String department, String major) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                // Get 4 courses for the selected major
                String[] courses = Course.getCoursesForMajor(major);

                if (courses == null || courses.length < 4) {
                    System.out.println("Error: Could not find 4 courses for major: " + major);
                    return false;
                }

                String sql = "INSERT INTO departmentMajor (stuId, department, major, Course1, Course2, Course3, Course4) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, stuId);
                pstmt.setString(2, department);
                pstmt.setString(3, major);
                pstmt.setString(4, courses[0]);
                pstmt.setString(5, courses[1]);
                pstmt.setString(6, courses[2]);
                pstmt.setString(7, courses[3]);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("\nAssigned 4 courses to student:");
                    System.out.println("  1. " + courses[0]);
                    System.out.println("  2. " + courses[1]);
                    System.out.println("  3. " + courses[2]);
                    System.out.println("  4. " + courses[3]);
                }
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error saving department and major: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Get department and major for a student by ID
     */
    public static String[] getDepartmentMajor(String stuId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT department, major FROM departmentMajor WHERE stuId = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, stuId);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new String[] { rs.getString("department"), rs.getString("major") };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting department and major: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * Update department and major for a student
     */
    public static boolean updateDepartmentMajor(String stuId, String department, String major) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "UPDATE departmentMajor SET department = ?, major = ? WHERE stuId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, department);
                pstmt.setString(2, major);
                pstmt.setString(3, stuId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating department and major: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Get full department, major, and courses information for a student
     */
    public static String[] getFullDepartmentMajorCourse(String stuId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT department, major, Course1, Course2, Course3, Course4 FROM departmentMajor WHERE stuId = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, stuId);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new String[] {
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getString("Course1"),
                            rs.getString("Course2"),
                            rs.getString("Course3"),
                            rs.getString("Course4")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting full department/major/course information: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
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

    /**
 * Delete ALL department/major records for a student by ID
 */
    public static boolean deleteDepartmentMajorByStudentId(String stuId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM departmentMajor WHERE stuId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, stuId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0; // true if at least one row deleted
            }
        } catch (SQLException e) {
            System.out.println("Error deleting department/major: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

}