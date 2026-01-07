public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/school_db";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "password";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
}
