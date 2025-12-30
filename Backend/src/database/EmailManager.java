package Backend.src.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

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
     * Retrieve all owner emails from account database
     */
    public static List<String> getOwnerEmails() {
        List<String> ownerEmails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT Owneremail FROM owneremail WHERE Owneremail IS NOT NULL";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    ownerEmails.add(rs.getString("owneremail"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving owner emails: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return ownerEmails;
    }

    /**
     * Retrieve all student emails from account database
     */
    public static List<String> getStudentEmails() {
        List<String> studentEmails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT StudentEmail FROM studentemail WHERE StudentEmail IS NOT NULL";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    studentEmails.add(rs.getString("StudentEmail"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving student emails: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return studentEmails;
    }

    /**
     * Retrieve all teacher emails from account database
     */
    public static List<String> getTeacherEmails() {
        List<String> teacherEmails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT teacheremail FROM teacheremail WHERE teacheremail IS NOT NULL";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    teacherEmails.add(rs.getString("teacheremail"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving teacher emails: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return teacherEmails;
    }

    /**
     * Retrieve all emails from all three tables
     */
    public static List<String> getAllEmails() {
        List<String> allEmails = new ArrayList<>();

        allEmails.addAll(getOwnerEmails());
        allEmails.addAll(getStudentEmails());
        allEmails.addAll(getTeacherEmails());

        return allEmails;
    }

    /**
     * Check if a specific email exists in any of the three tables
     */
    public static boolean emailExists(String email) {
        List<String> allEmails = getAllEmails();
        boolean exists = allEmails.contains(email);
        
        if (!exists) {
            System.out.println("Incorrect email");
        }
        
        return exists;
    }

    /**
     * Get email count from all three tables
     */
    public static int getTotalEmailCount() {
        return getAllEmails().size();
    }

    /**
     * Print all emails from the account database
     */
    public static void displayAllEmails() {
        System.out.println("\n========================================");
        System.out.println("           ALL EMAILS FROM ACCOUNT");
        System.out.println("========================================\n");

        List<String> ownerEmails = getOwnerEmails();
        List<String> studentEmails = getStudentEmails();
        List<String> teacherEmails = getTeacherEmails();

        System.out.println("OWNER EMAILS (" + ownerEmails.size() + "):");
        for (String email : ownerEmails) {
            System.out.println("  - " + email);
        }

        System.out.println("\nSTUDENT EMAILS (" + studentEmails.size() + "):");
        for (String email : studentEmails) {
            System.out.println("  - " + email);
        }

        System.out.println("\nTEACHER EMAILS (" + teacherEmails.size() + "):");
        for (String email : teacherEmails) {
            System.out.println("  - " + email);
        }

        System.out.println("\nTOTAL EMAILS: " + getTotalEmailCount());
        System.out.println("========================================\n");
    }
}