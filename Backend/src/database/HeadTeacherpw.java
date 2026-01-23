package Backend.src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HeadTeacherpw {

    // ===== DATABASE CONFIG =====
    private static final String DB_URL = "jdbc:mysql://localhost:3306/login_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "MRHENGXD123";
    private static final String DB_ROOT_URL = "jdbc:mysql://localhost:3306";

    // ===== CREATE DATABASE =====
    public static void createDatabase() {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_ROOT_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS login_system");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    // ===== CONNECT DATABASE =====
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===== CREATE TABLE =====
    public static void createUserTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = connectDB();
            String sql = "CREATE TABLE IF NOT EXISTS headteacher_password (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "HeadteacherID VARCHAR(30) UNIQUE, " +
                    "password VARCHAR(100) NOT NULL" +
                    ")";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    // ===== INSERT =====
    public static boolean saveHeadTeacherInfo(String HeadteacherID, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connectDB();
            String sql = "INSERT INTO headteacher_password (HeadteacherID, password) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, HeadteacherID);
            pstmt.setString(2, password);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // ===== UPDATE =====
    public static boolean updateHeadTeacherPassword(String HeadteacherID, String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connectDB();
            String sql = "UPDATE headteacher_password SET password = ? WHERE HeadteacherID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, HeadteacherID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // ===== VERIFY LOGIN =====
    public static boolean verifyHeadTeacherPassword(String HeadteacherID, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            String sql = "SELECT password FROM headteacher_password WHERE HeadteacherID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, HeadteacherID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return password.equals(storedPassword);
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    // ===== DELETE =====
    public static boolean deleteTeacherPassword(String HeadteacherID) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connectDB();
            String sql = "DELETE FROM headteacher_password WHERE HeadteacherID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, HeadteacherID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // ===== CLOSE RESOURCES =====
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
