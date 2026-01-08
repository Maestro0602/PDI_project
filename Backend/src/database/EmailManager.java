package Backend.src.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    /**
     * Get all student emails from database
     */
    public static List<String> getStudentEmails() {
        List<String> emails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return emails;
            }

            String sql = "SELECT email FROM students";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                if (email != null && !email.trim().isEmpty()) {
                    emails.add(email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student emails: " + e.getMessage());
        } finally {
            DatabaseManager.DatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return emails;
    }

    /**
     * Get all teacher emails from database
     */
    public static List<String> getTeacherEmails() {
        List<String> emails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return emails;
            }

            String sql = "SELECT email FROM teachers";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                if (email != null && !email.trim().isEmpty()) {
                    emails.add(email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching teacher emails: " + e.getMessage());
        } finally {
            DatabaseManager.DatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return emails;
    }

    /**
     * Get all owner emails from database
     */
    public static List<String> getOwnerEmails() {
        List<String> emails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return emails;
            }

            String sql = "SELECT email FROM owner";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                if (email != null && !email.trim().isEmpty()) {
                    emails.add(email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching owner emails: " + e.getMessage());
        } finally {
            DatabaseManager.DatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return emails;
    }

    /**
     * Get student ID by email
     */
    public static int getStudentIdByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return -1;
            }

            String sql = "SELECT student_id FROM students WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("student_id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student ID: " + e.getMessage());
        } finally {
            DatabaseManager.DatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return -1;
    }

    /**
     * Get teacher ID by email
     */
    public static int getTeacherIdByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return -1;
            }

            String sql = "SELECT teacher_id FROM teachers WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("teacher_id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching teacher ID: " + e.getMessage());
        } finally {
            DatabaseManager.DatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return -1;
    }

    /**
     * Get owner ID by email
     */
    public static int getOwnerIdByEmail(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return -1;
            }

            String sql = "SELECT owner_id FROM owner WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("owner_id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching owner ID: " + e.getMessage());
        } finally {
            DatabaseManager.DatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return -1;
    }

    /**
     * Check if email exists in students table
     */
    public static boolean isStudentEmail(String email) {
        return getStudentIdByEmail(email) != -1;
    }

    /**
     * Check if email exists in teachers table
     */
    public static boolean isTeacherEmail(String email) {
        return getTeacherIdByEmail(email) != -1;
    }

    /**
     * Check if email exists in owner table
     */
    public static boolean isOwnerEmail(String email) {
        return getOwnerIdByEmail(email) != -1;
    }
}