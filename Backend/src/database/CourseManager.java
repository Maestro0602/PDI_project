package Backend.src.database;

import java.sql.*;

public class CourseManager {

    /**
     * Create course table if it doesn't exist
     */
    public static void createCourseTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS course (" +
                        "course_id VARCHAR(10) PRIMARY KEY, " +
                        "course_name VARCHAR(100) NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                //System.out.println("course table created/verified successfully.");

                // Populate table with courses if empty
                if (isCourseTableEmpty()) {
                    populateDefaultCourses();
                }
            }
        } catch (SQLException e) {
            //System.out.println("Error creating course table: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Check if course table is empty
     */
    private static boolean isCourseTableEmpty() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT COUNT(*) as count FROM course";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getInt("count") == 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking course table: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return false;
    }

    /**
     * Populate course table with default courses
     */
    private static void populateDefaultCourses() {
        String[][] courses = {
                // AI Courses
                { "AI001", "Introduction to Artificial Intelligence" },
                { "AI002", "Machine Learning" },
                { "AI003", "Data Science Fundamentals" },
                { "AI004", "Deep Learning" },
                // AU Courses
                { "AU101", "Control Systems" },
                { "AU102", "Industrial Automation" },
                { "AU103", "PLC and SCADA Systems" },
                { "AU104", "Robotics Fundamentals" },
                // CS Courses
                { "CS001", "Computer and Network Security" },
                { "CS002", "Ethical Hacking Fundamentals" },
                { "CS003", "Cyber Security Management" },
                { "CS004", "Cryptography Basics" },
                // EE Courses
                { "EE101", "Electrical Circuits" },
                { "EE102", "Electrical Machines" },
                { "EE103", "Power Systems" },
                { "EE104", "Power Electronics" },
                // EL Courses
                { "EL101", "Analog Electronics" },
                { "EL102", "Digital Electronics" },
                { "EL103", "Microprocessors and Microcontrollers Systems" },
                { "EL104", "Embedded Systems" },
                // IE Courses
                { "IE101", "Operations Research" },
                { "IE102", "Quality Control and Assurance" },
                { "IE103", "Supply Chain Management" },
                { "IE104", "Work Study and Ergonomics" },
                // ME Courses
                { "ME101", "Engineering Mechanics" },
                { "ME102", "Thermodynamics" },
                { "ME103", "Machine Design" },
                { "ME104", "Fluid Mechanics" },
                // MF Courses
                { "MF101", "Manufacturing Processes" },
                { "MF102", "Computer-Aided Manufacturing" },
                { "MF103", "Production Planning and Control" },
                { "MF104", "Industrial Equipment" },
                // SE Courses
                { "SE001", "Programming Fundamentals" },
                { "SE002", "Object-Oriented Programming" },
                { "SE003", "Programming Fundamentals" },
                { "SE004", "Software Engineering Principles" }
        };

        for (String[] course : courses) {
            addCourse(course[0], course[1]);
        }
        System.out.println("Default courses populated successfully.");
    }

    /**
     * CREATE - Add course to database
     */
    public static boolean addCourse(String courseId, String courseName) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO course (course_id, course_name) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseId);
                pstmt.setString(2, courseName);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error adding course: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * READ - Get course by ID
     */
    public static String[] getCourseById(String courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT course_id, course_name FROM course WHERE course_id = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseId);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new String[] {
                            rs.getString("course_id"),
                            rs.getString("course_name")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving course: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * READ - Display all courses
     */
    public static void displayAllCourses() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT course_id, course_name FROM course ORDER BY course_id";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                System.out.println("\n" + "=".repeat(90));
                System.out.println("                              ALL COURSES");
                System.out.println("=".repeat(90));
                System.out.printf("%-15s %-70s%n", "Course ID", "Course Name");
                System.out.println("-".repeat(90));

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-15s %-70s%n",
                            rs.getString("course_id"),
                            rs.getString("course_name"));
                }

                if (!hasData) {
                    System.out.println("No courses found.");
                }
                System.out.println("=".repeat(90));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying courses: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    /**
     * READ - Get course count
     */
    public static int getCourseCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT COUNT(*) as count FROM course";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count >= 0 ? count : 0; // Ensure non-negative
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting course count: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return 0;
    }

    /**
     * READ - Get all courses as array for UI
     * Returns array of course names
     */
    public static String[] getAllCoursesArray() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                // First count
                String countSql = "SELECT COUNT(*) as count FROM course";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(countSql);
                
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt("count");
                }
                
                if (count == 0) {
                    return new String[0];
                }
                
                // Get all courses
                String sql = "SELECT course_name FROM course ORDER BY course_id";
                rs = stmt.executeQuery(sql);
                
                String[] results = new String[count];
                int index = 0;
                
                while (rs.next() && index < count) {
                    String courseName = rs.getString("course_name");
                    results[index] = courseName != null ? courseName : "";
                    index++;
                }
                
                return results;
            }
        } catch (SQLException e) {
            System.out.println("Error getting all courses: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return new String[0];
    }

    /**
     * READ - Get all unique departments from departmentMajor table
     */
    public static String[] getAllDepartments() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT DISTINCT department FROM departmentMajor ORDER BY department";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                
                java.util.List<String> departments = new java.util.ArrayList<>();
                while (rs.next()) {
                    String dept = rs.getString("department");
                    if (dept != null && !dept.isEmpty()) {
                        departments.add(dept);
                    }
                }
                
                return departments.toArray(new String[0]);
            }
        } catch (SQLException e) {
            System.out.println("Error getting departments: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return new String[0];
    }

    /**
     * READ - Get all unique majors from departmentMajor table
     */
    public static String[] getAllMajors() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT DISTINCT major FROM departmentMajor ORDER BY major";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                
                java.util.List<String> majors = new java.util.ArrayList<>();
                while (rs.next()) {
                    String major = rs.getString("major");
                    if (major != null && !major.isEmpty()) {
                        majors.add(major);
                    }
                }
                
                return majors.toArray(new String[0]);
            }
        } catch (SQLException e) {
            System.out.println("Error getting majors: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return new String[0];
    }

    /**
     * UPDATE - Update course information
     */
    public static boolean updateCourse(String courseId, String courseName) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "UPDATE course SET course_name = ? WHERE course_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseName);
                pstmt.setString(2, courseId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating course: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * DELETE - Remove course by ID
     */
    public static boolean deleteCourse(String courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM course WHERE course_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting course: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * DELETE - Remove all courses
     */
    public static boolean deleteAllCourses() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM course";
                stmt = conn.createStatement();
                int rowsAffected = stmt.executeUpdate(sql);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting all courses: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * READ - Get course ID by course name
     */
    public static String getCourseIdByName(String courseName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT course_id FROM course WHERE course_name = ? LIMIT 1";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseName);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("course_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving course ID: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
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
