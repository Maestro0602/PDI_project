package Backend.src.database;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseManager {

    // ====== DATABASE CONNECTION CLASS (SINGLETON) ======
    public static class DatabaseConnection {
        // Database connection parameters
        private static final String URL = "jdbc:mysql://localhost:3306/login_system";
        private static final String USER = "root";
        private static final String PASSWORD = "MRHENGXD123";
        
        // Singleton instance
        private static DatabaseConnection instance;
        private Connection connection;
        
        /**
         * Private constructor to prevent direct instantiation
         * Establishes connection to MySQL database
         * @throws SQLException if connection fails
         */
        private DatabaseConnection() throws SQLException {
            try {
                // Load MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Establish connection with properties
                java.util.Properties props = new java.util.Properties();
                props.setProperty("user", USER);
                props.setProperty("password", PASSWORD);
                props.setProperty("useSSL", "false");
                props.setProperty("serverTimezone", "UTC");
                
                this.connection = DriverManager.getConnection(URL, props);
                
                System.out.println("Database connection established successfully!");
                
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found. Please add MySQL Connector/J to classpath", e);
            } catch (SQLException e) {
                throw new SQLException("Failed to connect to database. Check if MySQL is running and credentials are correct", e);
            }
        }
        
        /**
         * Get singleton instance of DatabaseConnection
         * @return DatabaseConnection instance
         */
        public static DatabaseConnection getInstance() {
            try {
                if (instance == null || instance.connection == null || instance.connection.isClosed()) {
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            return instance;
        }
        
        /**
         * Get the active connection
         * @return Connection object
         */
        public Connection getConnection() {
            return this.connection;
        }
        
        /**
         * Legacy method for compatibility with old code
         * @return Connection object
         */
        public static Connection connectDB() {
            DatabaseConnection dbConn = getInstance();
            if (dbConn != null) {
                return dbConn.getConnection();
            }
            return null;
        }

        public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Note: Don't close the singleton connection here
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // ====== CONDITION CHECK CLASS ======
    public static class ConditionChecker {

        public static String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(password.getBytes());
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Error hashing password: " + e.getMessage());
                return null;
            }
        }

        /**
         * Check if user exists in users table by username or email
         */
        public static boolean checkUserExists(String usernameOrEmail) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                conn = DatabaseConnection.connectDB();
                if (conn == null) {
                    System.err.println("Failed to establish database connection.");
                    return false;
                }
                String sql = "SELECT * FROM users WHERE EmailName = ? OR email = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, usernameOrEmail);
                pstmt.setString(2, usernameOrEmail);
                rs = pstmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                System.out.println("Error checking user: " + e.getMessage());
                return false;
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
        }

        /**
         * Check if email exists in users table
         */
        public static boolean checkEmailExists(String email) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                conn = DatabaseConnection.connectDB();
                if (conn == null) {
                    System.err.println("Failed to establish database connection.");
                    return false;
                }
                String sql = "SELECT email FROM users WHERE email = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);
                rs = pstmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                System.out.println("Error checking email: " + e.getMessage());
                return false;
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
        }

        /**
         * Verify login credentials against users table
         * Returns username if successful, null otherwise
         */
        public static String verifyLogin(String usernameOrEmail, String password) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                conn = DatabaseConnection.connectDB();
                if (conn == null) {
                    System.err.println("Failed to establish database connection.");
                    return null;
                }
                String hashedPassword = hashPassword(password);
                String sql = "SELECT EmailName FROM users WHERE (EmailName = ? OR email = ?) AND password = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, usernameOrEmail);
                pstmt.setString(2, usernameOrEmail);
                pstmt.setString(3, hashedPassword);
                rs = pstmt.executeQuery();
                if (rs.next()) return rs.getString("EmailName");
            } catch (SQLException e) {
                System.out.println("Error verifying login: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, rs);
            }
            return null;
        }

        /**
         * Get user details from users table
         */
        public static void getUserDetails(String usernameOrEmail) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                conn = DatabaseConnection.connectDB();
                if (conn == null) {
                    System.err.println("Failed to establish database connection.");
                    return;
                }
                String sql = "SELECT id, EmailName, email, role, role_id, password FROM users WHERE EmailName = ? OR email = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, usernameOrEmail);
                pstmt.setString(2, usernameOrEmail);
                rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("User Details:");
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Username: " + rs.getString("EmailName"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Role: " + rs.getString("role"));
                    System.out.println("Role ID: " + rs.getInt("role_id"));
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
         * Register user with known role and role_id
         */
        public static boolean registerUser(String username, String email, String password, String role, int roleId) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connectDB();
                if (conn == null) {
                    System.err.println("Failed to establish database connection.");
                    return false;
                }
                String hashedPassword = ConditionChecker.hashPassword(password);
                String sql = "INSERT INTO users (EmailName, email, password, role, role_id) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, hashedPassword);
                pstmt.setString(4, role);
                pstmt.setInt(5, roleId);
                return pstmt.executeUpdate() > 0;
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
         * Reset password for user by email
         */
        public static boolean resetPassword(String email, String newPassword) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DatabaseConnection.connectDB();
                if (conn == null) {
                    System.err.println("Failed to establish database connection.");
                    return false;
                }
                String hashedPassword = ConditionChecker.hashPassword(newPassword);
                String sql = "UPDATE users SET password = ? WHERE email = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, hashedPassword);
                pstmt.setString(2, email);
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println("Error resetting password: " + e.getMessage());
            } finally {
                DatabaseConnection.closeResources(conn, pstmt, null);
            }
            return false;
        }
    }

    // ====== GET USER ROLE ======
    /**
     * Get user role from users table
     */
    public static String getUserRole(String usernameOrEmail) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.connectDB();
            if (conn == null) {
                System.err.println("Failed to establish database connection.");
                return null;
            }
            String sql = "SELECT role FROM users WHERE EmailName = ? OR email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (SQLException e) {
            System.out.println("Error getting user role: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, rs);
        }
        return null;
    }

    // ====== PUBLIC WRAPPER METHODS ======
    public static Connection connectDB() {
        return DatabaseConnection.connectDB();
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

    public static boolean registerUser(String username, String email, String password, String role, int roleId) {
        return RegistrationHandler.registerUser(username, email, password, role, roleId);
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
}