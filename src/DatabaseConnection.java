import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("CallToPrintStackTrace")
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gym_management";
    private static final String USER = "root";
    private static final String PASSWORD = "your_mysql_password_here"; // Use your actual MySQL password

    static {
        try {
            // Load MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}