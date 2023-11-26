package java;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseManager {
    private static Connection conn = null;

    private DatabaseManager() {
        // Private constructor to prevent instantiation
    }

    public static Connection getOnlyInstance() {
        if (conn == null) {
            conn = createConnection();
        }
        return conn;
    }

    private static Connection createConnection() {
        try {
            // Set up your database connection parameters (replace with your database details)
            String jdbcUrl = "jdbc:mysql://localhost:3306/Airline   ";
            String username = "root";
            String password = "Redsky1?";

            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create the connection
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions (e.g., log or throw a runtime exception)
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }


    public static boolean checkLogin(String email, String password) {
        Connection connection = getOnlyInstance();

        // Define the SQL query
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameters
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has any rows
            return resultSet.next();

        } catch (SQLException e) {
            return false;
            // e.printStackTrace();
            // // Handle exceptions (e.g., log or throw a runtime exception)
            // throw new RuntimeException("Failed to check login credentials.");
        }
    }
}
