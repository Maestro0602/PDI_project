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
                        "course_count INT DEFAULT 0, " +
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
     * Save teacher information to database with course count (CREATE)
     */
    public static boolean saveTeacherInfo(String teacherID, String department, String major, String course,
            int courseCount) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO teacherInfo (teacherID, department, major, course_count) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);
                pstmt.setString(2, department);
                pstmt.setString(3, major);
                pstmt.setInt(4, courseCount);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(" Teacher information saved successfully.");
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
     * Save teacher information to database (CREATE) - Overloaded for backward
     * compatibility
     */
    public static boolean saveTeacherInfo(String teacherID, String department, String major, String course) {
        return saveTeacherInfo(teacherID, department, major, course, 0);
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
                String sql = "SELECT teacherID, department, major, course_count FROM teacherInfo WHERE teacherID = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new String[] {
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getString("course_count")
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
     */
    public static void displayAllTeachers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT * FROM teacherInfo ORDER BY teacherID";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                System.out.println("\n" + "=".repeat(95));
                System.out.println("                           ALL TEACHERS INFORMATION");
                System.out.println("=".repeat(95));
                System.out.printf("%-5s %-15s %-20s %-40s %-20s%n", "NO", "Teacher ID",
                        "Department", "Major", "Course");
                System.out.println("-".repeat(95));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-15s %-20s %-40s %-20s%n",
                            count++,
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getString("course"));
                }

                if (!hasData) {
                    System.out.println("No teacher records found.");
                }
                System.out.println("=".repeat(95));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying teachers: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    /**
     * Update teacher information (UPDATE)
     */
    public static boolean updateTeacherInfo(String teacherID, String department, String major, String course) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "UPDATE teacherInfo SET department = ?, major = ?, course = ? WHERE teacherID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, department);
                pstmt.setString(2, major);
                pstmt.setString(3, course);
                pstmt.setString(4, teacherID);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(" Teacher information updated successfully.");
                    return true;
                } else {
                    System.out.println(" Teacher with ID " + teacherID + " not found.");
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
     * Update teacher information with course count (UPDATE)
     */
    public static boolean updateTeacherInfo(String teacherID, String department, String major, String course,
            int courseCount) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "UPDATE teacherInfo SET department = ?, major = ?, course_count = ? WHERE teacherID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, department);
                pstmt.setString(2, major);
                pstmt.setInt(4, courseCount);
                pstmt.setString(5, teacherID);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(" Teacher information updated successfully.");
                    return true;
                } else {
                    System.out.println(" Teacher with ID " + teacherID + " not found.");
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
                    System.out.println(" Teacher deleted successfully.");
                    return true;
                } else {
                    System.out.println(" Teacher with ID " + teacherID + " not found.");
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
     */
    public static void displayTeachersByDepartment(String department) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT * FROM teacherInfo WHERE department = ? ORDER BY teacherID";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, department);

                rs = pstmt.executeQuery();

                System.out.println("\n" + "=".repeat(95));
                System.out.println("                    TEACHERS IN " + department.toUpperCase() + " DEPARTMENT");
                System.out.println("=".repeat(95));
                System.out.printf("%-5s %-15s %-20s %-20s %-20s%n", "NO", "Teacher ID",
                        "Department", "Major", "Course");
                System.out.println("-".repeat(95));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-15s %-20s %-20s %-20s%n",
                            count++,
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getString("course"));
                }

                if (!hasData) {
                    System.out.println("No teachers found in " + department + " department.");
                }
                System.out.println("=".repeat(95));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying teachers by department: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Get teachers by course
     */
    public static void displayTeachersByCourse(String course) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT * FROM teacherInfo WHERE course = ? ORDER BY teacherID";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, course);

                rs = pstmt.executeQuery();

                System.out.println("\n" + "=".repeat(95));
                System.out.println("                    TEACHERS FOR " + course.toUpperCase() + " COURSE");
                System.out.println("=".repeat(95));
                System.out.printf("%-5s %-15s %-20s %-20s %-20s%n", "NO", "Teacher ID",
                        "Department", "Major", "Course");
                System.out.println("-".repeat(95));

                int count = 1;
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-15s %-20s %-20s %-20s%n",
                            count++,
                            rs.getString("teacherID"),
                            rs.getString("department"),
                            rs.getString("major"),
                            rs.getString("course"));
                }

                if (!hasData) {
                    System.out.println("No teachers found for " + course + " course.");
                }
                System.out.println("=".repeat(95));
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
                String sql = "SELECT COUNT(*) as course_count FROM teachercourse WHERE teacherID = ?";
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