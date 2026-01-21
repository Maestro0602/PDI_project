package Backend.src.database;

import java.sql.*;

/**
 * AnnouncementManager - Handles announcements CRUD operations
 * Stores and retrieves announcement records from the database
 */
public class AnnouncementManager {

    /**
     * Create announcements table if it doesn't exist
     */
    public static void createAnnouncementsTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS announcements (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(255) NOT NULL, " +
                        "content TEXT NOT NULL, " +
                        "author VARCHAR(100) NOT NULL, " +
                        "authorId VARCHAR(50), " +
                        "category VARCHAR(50) DEFAULT 'General', " +
                        "upvotes INT DEFAULT 0, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                        ") ENGINE=InnoDB;";

                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error creating announcements table: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Create a new announcement
     */
    public static boolean createAnnouncement(String title, String content, String author, 
                                              String authorId, String category) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO announcements (title, content, author, authorId, category) " +
                        "VALUES (?, ?, ?, ?, ?)";
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setString(3, author);
                pstmt.setString(4, authorId);
                pstmt.setString(5, category);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error creating announcement: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Get all announcements
     * Returns array of [id, title, content, author, category, upvotes, createdAt]
     */
    public static String[][] getAllAnnouncements() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT id, title, content, author, category, upvotes, created_at " +
                        "FROM announcements ORDER BY created_at DESC";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                
                java.util.List<String[]> results = new java.util.ArrayList<>();
                while (rs.next()) {
                    results.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getString("category"),
                        String.valueOf(rs.getInt("upvotes")),
                        rs.getTimestamp("created_at").toString()
                    });
                }
                
                return results.toArray(new String[0][]);
            }
        } catch (SQLException e) {
            System.out.println("Error getting announcements: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return new String[0][];
    }

    /**
     * Get announcements by category
     */
    public static String[][] getAnnouncementsByCategory(String category) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT id, title, content, author, category, upvotes, created_at " +
                        "FROM announcements WHERE category = ? ORDER BY created_at DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, category);
                rs = pstmt.executeQuery();
                
                java.util.List<String[]> results = new java.util.ArrayList<>();
                while (rs.next()) {
                    results.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getString("category"),
                        String.valueOf(rs.getInt("upvotes")),
                        rs.getTimestamp("created_at").toString()
                    });
                }
                
                return results.toArray(new String[0][]);
            }
        } catch (SQLException e) {
            System.out.println("Error getting announcements by category: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return new String[0][];
    }

    /**
     * Upvote an announcement
     */
    public static boolean upvoteAnnouncement(int announcementId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "UPDATE announcements SET upvotes = upvotes + 1 WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, announcementId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error upvoting announcement: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Delete an announcement
     */
    public static boolean deleteAnnouncement(int announcementId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "DELETE FROM announcements WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, announcementId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting announcement: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Update an announcement
     */
    public static boolean updateAnnouncement(int announcementId, String title, String content, String category) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "UPDATE announcements SET title = ?, content = ?, category = ? WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setString(3, category);
                pstmt.setInt(4, announcementId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating announcement: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Get announcement count
     */
    public static int getAnnouncementCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT COUNT(*) as count FROM announcements";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting announcement count: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return 0;
    }

    /**
     * Close database resources safely
     */
    private static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}
