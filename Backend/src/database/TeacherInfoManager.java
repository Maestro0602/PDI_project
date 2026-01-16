package Backend.src.database;

import java.sql.*;

public class TeacherInfoManager {

    /**
     * Create teacherInfo table if it doesn't exist
     */
    public static void createTeacherInfoTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS teacherInfo (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "teacherID VARCHAR(50) NOT NULL UNIQUE, " +
                        "department VARCHAR(100) NOT NULL, " +
                        "major VARCHAR(100) NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                // System.out.println("teacherInfo table created/verified successfully.");
            }
        } catch (SQLException e) {
            // System.out.println("Error creating teacherInfo table: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Save teacher information to database (CREATE) - 3 parameters
     * This is the main method used by ManageDepartment
     */
    public static boolean saveTeacherInfo(String teacherID, String department, String major) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO teacherInfo (teacherID, department, major) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);
                pstmt.setString(2, department);
                pstmt.setString(3, major);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Teacher information saved successfully.");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving teacher information: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Get teacher information by teacher ID (READ)
     */
    public static String[] getTeacherInfo(String teacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT teacherID, department, major FROM teacherInfo WHERE teacherID = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new String[] {
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting teacher information: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * Get all teachers information (READ)
     * Now fetches course count dynamically from teacher_course table
     */
    public static void displayAllTeachers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT t.teacherID, t.department, t.major, " +
                        "COUNT(tc.course_id) as course_count " +
                        "FROM teacherInfo t " +
                        "LEFT JOIN teacher_course tc ON t.teacherID = tc.teacherID " +
                        "GROUP BY t.teacherID, t.department, t.major " +
                        "ORDER BY t.teacherID";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("                           ALL TEACHERS INFORMATION");
                System.out.println("=".repeat(100));
                System.out.printf("%-5s %-15s %-25s %-35s %-15s%n", "NO", "Teacher ID",
                        "Department", "Major", "Course Count");
                System.out.println("-".repeat(100));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-15s %-25s %-35s %-15d%n",
                            count++,
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getInt("course_count"));
                }

                if (!hasData) {
                    System.out.println("No teacher records found.");
                }
                System.out.println("=".repeat(100));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying teachers: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    /**
     * Update teacher information (UPDATE) - 3 parameters
     * This is the main method used by ManageDepartment
     */
    public static boolean updateTeacherInfo(String teacherID, String department, String major) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "UPDATE teacherInfo SET department = ?, major = ? WHERE teacherID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, department);
                pstmt.setString(2, major);
                pstmt.setString(3, teacherID);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Teacher information updated successfully.");
                    return true;
                } else {
                    System.out.println("Teacher with ID " + teacherID + " not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating teacher information: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Delete teacher by teacher ID (DELETE)
     */
    public static boolean deleteTeacher(String teacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM teacherInfo WHERE teacherID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Teacher deleted successfully.");
                    return true;
                } else {
                    System.out.println("Teacher with ID " + teacherID + " not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting teacher: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Check if teacher ID exists
     */
    public static boolean teacherExists(String teacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT teacherID FROM teacherInfo WHERE teacherID = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                rs = pstmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking teacher existence: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }

    /**
     * Get teachers by department
     * Now shows course count dynamically
     */
    public static void displayTeachersByDepartment(String department) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT t.teacherID, t.department, t.major, " +
                        "COUNT(tc.course_id) as course_count " +
                        "FROM teacherInfo t " +
                        "LEFT JOIN teacher_course tc ON t.teacherID = tc.teacherID " +
                        "WHERE t.department = ? " +
                        "GROUP BY t.teacherID, t.department, t.major " +
                        "ORDER BY t.teacherID";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, department);

                rs = pstmt.executeQuery();

                System.out.println("\n" + "=".repeat(100));
                System.out.println("                    TEACHERS IN " + department.toUpperCase() + " DEPARTMENT");
                System.out.println("=".repeat(100));
                System.out.printf("%-5s %-15s %-25s %-35s %-15s%n", "NO", "Teacher ID",
                        "Department", "Major", "Course Count");
                System.out.println("-".repeat(100));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-15s %-25s %-35s %-15d%n",
                            count++,
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getInt("course_count"));
                }

                if (!hasData) {
                    System.out.println("No teachers found in " + department + " department.");
                }
                System.out.println("=".repeat(100));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying teachers by department: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Get teachers by course
     * Now uses JOIN with teacher_course table
     */
    public static void displayTeachersByCourse(String courseName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT t.teacherID, t.department, t.major, c.course_name " +
                        "FROM teacherInfo t " +
                        "INNER JOIN teacher_course tc ON t.teacherID = tc.teacherID " +
                        "INNER JOIN course c ON tc.course_id = c.course_id " +
                        "WHERE c.course_name = ? " +
                        "ORDER BY t.teacherID";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseName);

                rs = pstmt.executeQuery();

                System.out.println("\n" + "=".repeat(100));
                System.out.println("                    TEACHERS FOR " + courseName.toUpperCase() + " COURSE");
                System.out.println("=".repeat(100));
                System.out.printf("%-5s %-15s %-25s %-35s %-15s%n", "NO", "Teacher ID",
                        "Department", "Major", "Course");
                System.out.println("-".repeat(100));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-15s %-25s %-35s %-15s%n",
                            count++,
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getString("courseName"));
                }

                if (!hasData) {
                    System.out.println("No teachers found for " + courseName + " course.");
                }
                System.out.println("=".repeat(100));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying teachers by course: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Get course count for a teacher from teacher_course table
     */
    public static int getCourseCountForTeacher(String teacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT COUNT(*) as course_count FROM teacher_course WHERE teacherID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("course_count");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting course count: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return 0;
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