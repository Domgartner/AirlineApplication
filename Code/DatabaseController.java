package Code;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseController {
    private static Connection conn = null;

    private DatabaseController() {
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


    public static ArrayList<String> browseAvailableFlights(){
        Connection connection = getOnlyInstance();
        String sql = "SELECT * FROM FLIGHTS";
        ArrayList<String> availableFlights = new ArrayList<>();


        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String flightNum = resultSet.getString("FlightNum");
                String flightDest = resultSet.getString("FlightDest");
                int aircraft = resultSet.getInt("AircraftID");
                String departure = resultSet.getString("Departure");

                String flightInfo = String.format("Flight Number: %s \n  Destination: %s \n Aircraft: %s \n  Departure: %s", flightNum, flightDest, aircraft, departure);
                availableFlights.add(flightInfo);
                   
            }
            return availableFlights;
        }
        
        catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., log or throw a runtime exception)
            throw new RuntimeException("Failed to browse available flights.");
        }

        
    }



    public static ArrayList<String> browsePassengersByFlight(String flightnum) {
        ArrayList<String> passengers = new ArrayList<>();
    
        try {

             PreparedStatement preparedStatement = conn.prepareStatement("SELECT FirstName, LastName, Email, SeatNum FROM PASSENGERS WHERE FlightNum = ?");
         
            // Set the FlightNum parameter in the prepared statement
            preparedStatement.setString(1, flightnum);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Fetch data from the result set
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("Email");
                    String seatNum = resultSet.getString("SeatNum");
    
                    String passengerInfo = String.format("Name: %s %s, Email: %s, Seat: %s", firstName, lastName, email, seatNum);
                    passengers.add(passengerInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to browse passengers by flight.");
        }
        return passengers;
    }


    public static boolean signUp(String firstName, String lastName, String address, String email, String password) {
        if (firstName == null || lastName == null || address == null || email == null || password == null) {
            // At least one of the fields is null
            return false;
        }
        try {
            Connection connection = getOnlyInstance();
            String query = "INSERT INTO USERS (FirstName, LastName, Email, Address, Password) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, address);
                preparedStatement.setString(5, password);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static ArrayList<String> searchFlights(String departureDate, String flightNum, String flightDest) {
        Connection connection = getOnlyInstance();
        ArrayList<String> matchingFlights = new ArrayList<>();
    
        // Constructing the base of the SQL query
        StringBuilder sql = new StringBuilder("SELECT * FROM FLIGHTS WHERE ");
    
        // List to keep track of parameters to be set in the prepared statement
        ArrayList<Object> parameters = new ArrayList<>();
    
        // Check and append conditions based on the provided parameters
        if (departureDate != null && !departureDate.isEmpty()) {
            sql.append("DepartureDate = ? ");
            parameters.add(departureDate);
        }
        if (flightNum != null && !flightNum.isEmpty()) {
            if (!parameters.isEmpty()) {
                sql.append("AND ");
            }
            sql.append("FlightNum = ? ");
            parameters.add(flightNum);
        }
        if (flightDest != null && !flightDest.isEmpty()) {
            if (!parameters.isEmpty()) {
                sql.append("AND ");
            }
            sql.append("FlightDest = ? ");
            parameters.add(flightDest);
        }
    
        // If no parameters are given, return all flights
        if (parameters.isEmpty()) {
            return browseAvailableFlights();
        }
    
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
    
            // Setting the parameters in the prepared statement
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Processing the result set
            while (resultSet.next()) {
                String flightNumber = resultSet.getString("FlightNum");
                String destination = resultSet.getString("FlightDest");
                int aircraft = resultSet.getInt("AircraftID");
                String departure = resultSet.getString("DepartureDate");
    
                String flightInfo = String.format(
                        "Flight Number: %s \n  Destination: %s \n Aircraft: %s \n  Departure: %s",
                        flightNumber, destination, aircraft, departure);
                matchingFlights.add(flightInfo);
            }
            return matchingFlights;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., log or throw a runtime exception)
            throw new RuntimeException("Failed to search flights.");
        }
    }
    
    
}
