package src;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/student_management";
        String username = "root"; // your MySQL username
        String password = "P@ssw0rd"; // your MySQL root password

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connection successful!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}