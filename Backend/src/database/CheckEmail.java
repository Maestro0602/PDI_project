package Backend.src.database;

import java.sql.*;

/**
 * CheckEmail handles email verification and ID retrieval from account database
 * Verifies if emails exist and retrieves corresponding IDs
 */
public class CheckEmail {

    // ====== DATABASE CONNECTION FOR ACCOUNT DATABASE ======
    private static class AccountDatabaseConnection {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/account";
        private static final String DB_USER = "myuser";
        private static final String DB_PASSWORD = "mypassword";

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

        /**
         * Close database resources safely
         */
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
     * Check if email already exists in any table
     */
    public static boolean emailExists(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT 1 FROM studentemail WHERE StudentEmail = ? " +
                        "UNION SELECT 1 FROM teacheremail WHERE TeacherEmail = ? " +
                        "UNION SELECT 1 FROM owneremail WHERE Owneremail = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);
                pstmt.setString(2, email);
                pstmt.setString(3, email);

                rs = pstmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking email existence: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * Get Student ID by email
     */
    public static String getStudentIdByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT StudentID FROM studentemail WHERE StudentEmail = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("StudentID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving student ID: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Get Teacher ID by email
     */
    public static String getTeacherIdByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT TeacherID FROM teacheremail WHERE TeacherEmail = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("TeacherID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving teacher ID: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Get Owner ID by email
     */
    public static String getOwnerIdByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT OwnerID FROM owneremail WHERE Owneremail = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("OwnerID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving owner ID: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Get ID from email - returns the ID that matches the email
     * Checks all three tables and returns the ID found
     */
    public static String getIdFromEmail(String email) {
        String studentId = getStudentIdByEmail(email);
        if (studentId != null) {
            return studentId;
        }

        String teacherId = getTeacherIdByEmail(email);
        if (teacherId != null) {
            return teacherId;
        }

        String ownerId = getOwnerIdByEmail(email);
        if (ownerId != null) {
            return ownerId;
        }

        return null;
    }

    /**
     * Get email type (STUDENT, TEACHER, OWNER)
     */
    public static String getEmailType(String email) {
        if (getStudentIdByEmail(email) != null) {
            return "STUDENT";
        }
        if (getTeacherIdByEmail(email) != null) {
            return "TEACHER";
        }
        if (getOwnerIdByEmail(email) != null) {
            return "OWNER";
        }
        return null;
    }
}
