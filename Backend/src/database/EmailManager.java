package Backend.src.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EmailManager handles both retrieving and creating email accounts
 * for students, teachers, and owners in the account database
 */
public class EmailManager {

    // ====== DATABASE CONNECTION FOR ACCOUNT DATABASE ======
    public static class AccountDatabaseConnection {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/account";
        private static final String DB_USER = "myuser";
        private static final String DB_PASSWORD = "mypassword";
        private static final String DB_ROOT_URL = "jdbc:mysql://localhost:3306";

        /**
         * Create account database if it doesn't exist
         */
        public static void createAccountDatabase() {
            Connection conn = null;
            Statement stmt = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_ROOT_URL, DB_USER, DB_PASSWORD);
                if (conn != null) {
                    String sql = "CREATE DATABASE IF NOT EXISTS account";
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    // System.out.println(" Account database initialized successfully");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Error creating account database: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        }

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

        /**
         * Create student email table if it doesn't exist in account database
         */
        public static void createStudentEmailTable() {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectDB();
                if (conn != null) {
                    String sql = "CREATE TABLE IF NOT EXISTS studentemail (" +
                            "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "StudentID VARCHAR(20) UNIQUE NOT NULL, " +
                            "StudentEmail VARCHAR(100) UNIQUE NOT NULL)";

                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    // System.out.println(" Student email table initialized successfully");
                } else {
                    System.out.println(" Failed to connect to account database");
                }
            } catch (SQLException e) {
                System.out.println("Error creating student email table: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        }

        /**
         * Create teacher email table if it doesn't exist in account database
         */
        public static void createTeacherEmailTable() {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectDB();
                if (conn != null) {
                    String sql = "CREATE TABLE IF NOT EXISTS teacheremail (" +
                            "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "TeacherID VARCHAR(20) UNIQUE NOT NULL, " +
                            "TeacherEmail VARCHAR(100) UNIQUE NOT NULL)";

                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    // System.out.println(" Teacher email table initialized successfully");
                } else {
                    System.out.println(" Failed to connect to account database");
                }
            } catch (SQLException e) {
                System.out.println("Error creating teacher email table: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        }

        /**
         * Create owner email table if it doesn't exist in account database
         */
        public static void createOwnerEmailTable() {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectDB();
                if (conn != null) {
                    String sql = "CREATE TABLE IF NOT EXISTS owneremail (" +
                            "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "OwnerID VARCHAR(20) UNIQUE NOT NULL, " +
                            "Owneremail VARCHAR(100) UNIQUE NOT NULL)";

                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    // System.out.println(" Owner email table initialized successfully");
                } else {
                    System.out.println(" Failed to connect to account database");
                }
            } catch (SQLException e) {
                System.out.println("Error creating owner email table: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        }

        /**
         * Initialize all email tables in account database
         */
        public static void initializeEmailTables() {
            // Create account database first
            createAccountDatabase();

            System.out.println("\n========================================");
            System.out.println("   INITIALIZING EMAIL TABLES");
            System.out.println("========================================");
            createStudentEmailTable();
            createTeacherEmailTable();
            createOwnerEmailTable();
            System.out.println("========================================\n");
        }
    }

    // ====== ACCOUNT TYPE ENUM ======
    public enum AccountType {
        STUDENT("P2024"),
        TEACHER("T2024"),
        OWNER("O2024");

        private final String prefix;

        AccountType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }

        /**
         * Get AccountType from prefix
         */
        public static AccountType fromPrefix(String prefix) {
            for (AccountType type : AccountType.values()) {
                if (type.prefix.equals(prefix.substring(0, Math.min(5, prefix.length())))) {
                    return type;
                }
            }
            return null;
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

    // ====== EMAIL ACCOUNT CREATION METHODS ======

    /**
     * Validate ID format based on account type
     */
    public static boolean validateIdFormat(String id, AccountType type) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        return id.startsWith(type.getPrefix());
    }

    /**
     * Validate email format
     */
    public static boolean validateEmailFormat(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * Create email account for student
     */
    public static boolean createStudentEmail(String studentId, String email) {
        if (!validateIdFormat(studentId, AccountType.STUDENT)) {
            System.out.println("Invalid student ID format. Must start with P2024");
            return false;
        }
        if (!validateEmailFormat(email)) {
            System.out.println("Invalid email format");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO studentemail (StudentID, StudentEmail) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentId);
                pstmt.setString(2, email);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student email account created successfully!");
                    return true;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: Email or ID already exists in the system");
            } else {
                System.out.println("Error creating student email account: " + e.getMessage());
            }
            return false;
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Create email account for teacher
     */
    public static boolean createTeacherEmail(String teacherId, String email) {
        if (!validateIdFormat(teacherId, AccountType.TEACHER)) {
            System.out.println("Invalid teacher ID format. Must start with T2024");
            return false;
        }
        if (!validateEmailFormat(email)) {
            System.out.println("Invalid email format");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO teacheremail (TeacherID, TeacherEmail) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, teacherId);
                pstmt.setString(2, email);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Teacher email account created successfully!");
                    return true;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: Email or ID already exists in the system");
            } else {
                System.out.println("Error creating teacher email account: " + e.getMessage());
            }
            return false;
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Create email account for owner
     */
    public static boolean createOwnerEmail(String ownerId, String email) {
        if (!validateIdFormat(ownerId, AccountType.OWNER)) {
            System.out.println("Invalid owner ID format. Must start with O2024");
            return false;
        }
        if (!validateEmailFormat(email)) {
            System.out.println("Invalid email format");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO owneremail (OwnerID, Owneremail) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, ownerId);
                pstmt.setString(2, email);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Owner email account created successfully!");
                    return true;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: Email or ID already exists in the system");
            } else {
                System.out.println("Error creating owner email account: " + e.getMessage());
            }
            return false;
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Create email account based on account type
     */
    public static boolean createEmailAccount(AccountType type, String id, String email) {
        switch (type) {
            case STUDENT:
                return createStudentEmail(id, email);
            case TEACHER:
                return createTeacherEmail(id, email);
            case OWNER:
                return createOwnerEmail(id, email);
            default:
                System.out.println("Unknown account type");
                return false;
        }
    }

    /**
     * Check if email already exists (updated version)
     */
    public static boolean emailExistsInSystem(String email) {
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
     * Check if ID already exists
     */
    public static boolean idExists(String id, AccountType type) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = AccountDatabaseConnection.connectDB();
            if (conn != null) {
                String tableName = "";
                String columnName = "";

                switch (type) {
                    case STUDENT:
                        tableName = "studentemail";
                        columnName = "StudentID";
                        break;
                    case TEACHER:
                        tableName = "teacheremail";
                        columnName = "TeacherID";
                        break;
                    case OWNER:
                        tableName = "owneremail";
                        columnName = "OwnerID";
                        break;
                }

                String sql = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, id);

                rs = pstmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking ID existence: " + e.getMessage());
        } finally {
            AccountDatabaseConnection.closeResources(conn, pstmt, rs);
        }

        return false;
    }
}