package database;

import java.sql.*;

public class TeacherCourseManager {

    /**
     * Create teachercourse table if it doesn't exist
     */
    public static void createTeacherCourseTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS teacher_course (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "teacherID VARCHAR(20) NOT NULL, " +
                        "course_id VARCHAR(10) NOT NULL, " +
                        "UNIQUE KEY unique_teacher_course (teacherID, course_id))";

                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                // System.out.println("teachercourse table created/verified successfully.");
            }
        } catch (SQLException e) {
            // System.out.println("Error creating teachercourse table: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * CREATE - Add teacher-course assignment to database
     */
    public static boolean addTeacherCourse(String teacherID, String courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                // Check if course is already assigned to this teacher
                if (isTeacherCourseAssigned(teacherID, courseId)) {
                    System.out.println("Course already joined by this teacher.");
                    return false;
                }

                String sql = "INSERT INTO teacher_course (teacherID, course_id) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);
                pstmt.setString(2, courseId);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Update course count after successful insertion
                    getTeacherCourseCount(teacherID);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding teacher-course assignment: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * CHECK - Verify if a teacher-course assignment already exists
     */
    private static boolean isTeacherCourseAssigned(String teacherID, String courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT COUNT(*) as count FROM teacher_course WHERE teacherID = ? AND course_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);
                pstmt.setString(2, courseId);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking teacher-course assignment: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }

    /**
     * READ - Get all courses for a specific teacher
     */
    public static void getTeacherCourses(String teacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT id, teacherID, course_id FROM teacher_course WHERE teacherID = ? ORDER BY id";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                rs = pstmt.executeQuery();

                System.out.println("\n" + "=".repeat(70));
                System.out.println("          COURSES ASSIGNED TO TEACHER ID: " + teacherID);
                System.out.println("=".repeat(70));
                System.out.printf("%-5s %-20s %-15s%n", "ID", "Teacher ID", "Course ID");
                System.out.println("-".repeat(70));

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-20s %-15s%n",
                            rs.getInt("id"),
                            rs.getString("teacherID"),
                            rs.getString("course_id"));
                }

                if (!hasData) {
                    System.out.println("No courses found for this teacher.");
                }
                System.out.println("=".repeat(70));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving teacher courses: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * READ - Get teacher-course assignment by ID
     */
    public static String[] getTeacherCourseById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT id, teacherID, course_id FROM teacher_course WHERE id = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new String[] {
                            String.valueOf(rs.getInt("id")),
                            rs.getString("teacherID"),
                            rs.getString("course_id")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving teacher-course assignment: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * READ - Display all teacher-course assignments
     */
    public static void displayAllTeacherCourses() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT id, teacherID, course_id FROM teacher_course ORDER BY id";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                System.out.println("\n" + "=".repeat(70));
                System.out.println("                  ALL TEACHER-COURSE ASSIGNMENTS");
                System.out.println("=".repeat(70));
                System.out.printf("%-5s %-20s %-15s%n", "ID", "Teacher ID", "Course ID");
                System.out.println("-".repeat(70));
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-5d %-20s %-15s%n",
                            rs.getInt("id"),
                            rs.getString("teacherID"),
                            rs.getString("course_id"));
                }

                if (!hasData) {
                    System.out.println("No teacher-course records found.");
                }
                System.out.println("=".repeat(70));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying teacher-course assignments: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    /**
     * UPDATE - Update teacher-course assignment
     */
    public static boolean updateTeacherCourse(int id, String teacherID, String courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "UPDATE teachercourse SET teacherID = ?, course_id = ? WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);
                pstmt.setString(2, courseId);
                pstmt.setInt(3, id);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating teacher-course assignment: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * DELETE - Remove teacher-course assignment by ID
     */
    public static boolean deleteTeacherCourse(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM teacher_course WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting teacher-course assignment: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * DELETE - Remove all courses for a specific teacher
     */
    public static boolean deleteTeacherAllCourses(String teacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM teacher_course WHERE teacherID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting teacher's courses: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * COUNT - Get the number of courses assigned to a teacher and update
     * course_count in teacherinfo
     */
    public static int getTeacherCourseCount(String teacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT COUNT(*) as course_count FROM teacher_course WHERE teacherID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherID);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt("course_count");
                    return count;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error counting teacher courses: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return 0;
    }

    /**
     * Close database resources
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
