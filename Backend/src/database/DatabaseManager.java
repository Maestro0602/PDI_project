package Backend.src.database;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseManager {

    // ====== DATABASE CONNECTION CLASS ======
    public static class DatabaseConnection {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/login_system";
        private static final String DB_USER = "myuser";
        private static final String DB_PASSWORD = "mypassword";
        private static final String DB_ROOT_URL = "jdbc:mysql://localhost:3306";

        /**
         * Create login_system database if it doesn't exist
         */
        public static void createDatabase() {
            Connection conn = null;
            Statement stmt = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_ROOT_URL, DB_USER, DB_PASSWORD);
                if (conn != null) {
                    String sql = "CREATE DATABASE IF NOT EXISTS login_system";
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    // System.out.println("✓ Login system database initialized successfully");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Error creating database: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        }

        /**
         * Connect to MySQL database
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
                System.out.println("Database connection error: " + e.getMessage());
                return null;
            }
        }

        /**
         * Create users table if it doesn't exist
         */
        public static void createUserTable() {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = connectDB();
                if (conn != null) {
                    String sql = "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "UserID VARCHAR(30), " +
                            "Name VARCHAR(50) UNIQUE NOT NULL, " +
                            "email VARCHAR(100) UNIQUE NOT NULL, " +
                            "password VARCHAR(100) NOT NULL, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    // System.out.println("✓ Users table initialized successfully");
                } else {
                    System.out.println("✗ Failed to connect to login_system database");
                }
            } catch (SQLException e) {
                System.out.println("Error creating table: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
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

    // ====== CONDITION CHECK CLASS ======
    public static class ConditionChecker {

        /**
         * Hash password using SHA-256
         */
        public static String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(password.getBytes());
                StringBuilder hexString = new StringBuilder();

                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                System.out.println("Error hashing password: " + e.getMessage());
                return null;
            }
        }

        /**
         * Check if user exists by username or email
         */
        public static boolean checkUserExists(String usernameOrEmail) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseConnection.connectDB();
                if (conn != null) {
                    String sql = "SELECT * FROM users WHERE Name = ? OR email = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, usernameOrEmail);
                    pstmt.setString(2, usernameOrEmail);

                    rs = pstmt.executeQuery();
                    return rs.next();
                }
            } catch (SQLException e) {
                System.out.println("Error checking user: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
            return false;
        }

        /**
         * Check if email exists in database
         */
        public static boolean checkEmailExists(String email) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseConnection.connectDB();
                if (conn != null) {
                    String sql = "SELECT email FROM users WHERE email = ?";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, email);

                    rs = pstmt.executeQuery();
                    return rs.next();
                }
            } catch (SQLException e) {
                System.out.println("Error checking email: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
            return false;
        }

        /**
         * Check if UserID exists in database
         */
        public static boolean checkUserIDExists(String userID) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseConnection.connectDB();
                if (conn != null) {
                    String sql = "SELECT UserID FROM users WHERE UserID = ?";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userID);

                    rs = pstmt.executeQuery();
                    return rs.next();
                }
            } catch (SQLException e) {
                System.out.println("Error checking UserID: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
            return false;
        }

        /**
         * Verify login credentials (username or email + password)
         * Returns username if successful, null if failed
         */
        public static String verifyLogin(String usernameOrEmail, String password) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseConnection.connectDB();
                if (conn != null) {
                    // Hash the input password for comparison
                    String hashedPassword = hashPassword(password);
                    String sql = "SELECT Name FROM users WHERE " +
                            "(Name = ? OR email = ?) AND password = ?";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, usernameOrEmail);
                    pstmt.setString(2, usernameOrEmail);
                    pstmt.setString(3, hashedPassword);

                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        return rs.getString("Name");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error verifying login: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
            return null;
        }

        /**
         * Get user details by username or email
         */
        public static void getUserDetails(String usernameOrEmail) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseConnection.connectDB();
                if (conn != null) {
                    String sql = "SELECT id, Name, email, created_at FROM users " +
                            "WHERE Name = ? OR email = ?";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, usernameOrEmail);
                    pstmt.setString(2, usernameOrEmail);

                    rs = pstmt.executeQuery();

                    // if (rs.next()) {
                    // System.out.println("\nUser Details:");
                    // System.out.println("ID: " + rs.getInt("id"));
                    // System.out.println("Username: " + rs.getString("username"));
                    // System.out.println("Email: " + rs.getString("email"));
                    // System.out.println("Created: " + rs.getTimestamp("created_at"));
                    // }
                }
            } catch (SQLException e) {
                System.out.println("Error getting user details: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
        }
    }

    // ====== REGISTRATION CLASS ======
    public static class RegistrationHandler {

        /**
         * Register new user in database with ID from email account
         */
        public static boolean registerUser(String username, String email, String password) {
            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                conn = DatabaseConnection.connectDB();
                if (conn != null) {
                    // Get UserID from email using CheckEmail
                    String userId = CheckEmail.getIdFromEmail(email);

                    // Hash the password before storing
                    String hashedPassword = ConditionChecker.hashPassword(password);
                    String sql = "INSERT INTO users (Name, email, password, UserID) VALUES (?, ?, ?, ?)";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, username);
                    pstmt.setString(2, email);
                    pstmt.setString(3, hashedPassword);
                    pstmt.setString(4, userId);

                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Username or email already exists!");
                } else {
                    System.out.println("Registration error: " + e.getMessage());
                }
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, null);
            }
            return false;
        }

        /**
         * Reset password for a user by email
         */
        public static boolean resetPassword(String email, String newPassword) {
            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                conn = DatabaseConnection.connectDB();
                if (conn != null) {
                    // Hash the new password before storing
                    String hashedPassword = ConditionChecker.hashPassword(newPassword);
                    String sql = "UPDATE users SET password = ? WHERE email = ?";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, hashedPassword);
                    pstmt.setString(2, email);

                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                System.out.println("Error resetting password: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, null);
            }
            return false;
        }
    }

    // ====== PUBLIC WRAPPER METHODS FOR BACKWARD COMPATIBILITY ======
    public static Connection connectDB() {
        return DatabaseConnection.connectDB();
    }

    public static void createUserTable() {
        DatabaseConnection.createUserTable();
    }

    public static String hashPassword(String password) {
        return ConditionChecker.hashPassword(password);
    }

    public static boolean checkUserExists(String usernameOrEmail) {
        return ConditionChecker.checkUserExists(usernameOrEmail);
    }

    public static String verifyLogin(String usernameOrEmail, String password) {
        return ConditionChecker.verifyLogin(usernameOrEmail, password);
    }

    public static boolean registerUser(String username, String email, String password) {
        return RegistrationHandler.registerUser(username, email, password);
    }

    public static void getUserDetails(String usernameOrEmail) {
        ConditionChecker.getUserDetails(usernameOrEmail);
    }

    public static boolean checkEmailExists(String email) {
        return ConditionChecker.checkEmailExists(email);
    }

    public static boolean resetPassword(String email, String newPassword) {
        return RegistrationHandler.resetPassword(email, newPassword);
    }

    /**
     * Display temporary teacher entries from users table
     * Shows entries where UserID starts with 'T2024', Name starts with 'TEACHER'
     * and contains '_2026'
     */
    public static void displayTemporaryTeacherUsers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connectDB();
            if (conn != null) {
                String sql = "SELECT UserID, Name FROM users WHERE " +
                            "UserID LIKE 'T2024%' AND " +
                            "Name LIKE 'TEACHER%' AND " +
                            "Name LIKE '%\\\\_2026'";



                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                System.out.println("\n" + "=".repeat(50));
                System.out.println("TEMPORARY TEACHER ENTRIES IN USERS TABLE");
                System.out.println("=".repeat(50));
                System.out.printf("%-15s %-40s %n", "UserID", "Name");
                System.out.println("-".repeat(50));

                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.printf("%-15s %-40s %n",
                            rs.getString("UserID"),
                            rs.getString("Name"));
                }

                System.out.println("=".repeat(50));
                if (count == 0) {
                    System.out.println("No temporary teacher entries found.");
                } else {
                    System.out.println("Total entries found: " + count);
                }
                System.out.println("=".repeat(50));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying temporary teacher entries: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
    }
}
