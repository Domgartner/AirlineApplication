package Code.Control;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import Code.Entity.Aircraft;
import Code.Entity.Crew;
import Code.Entity.Date;
import Code.Entity.Flight;
import Code.Entity.Purchase;
import Code.Entity.Seat;
import Code.Entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseController {
    private static Connection conn = null;
    public static ArrayList<Aircraft> aircrafts = new ArrayList<>();

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
            String username = "oop";
            String password = "password";
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

    public static String[] checkLogin(String email, String password) {
        Connection connection = getOnlyInstance();
        // Define the SQL query
        String sql = "SELECT FirstName, LastName FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameters
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if the result set has any rows
            if (resultSet.next()) {
                // Retrieve first name and last name
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                // Create and return a String array with the retrieved information
                return new String[]{firstName, lastName};
            }
        } catch (SQLException e) {
            // Handle the exception appropriately (logging, throwing, etc.)
            e.printStackTrace();
        }
        // Return null if login fails or user not found
        return null;
    }

    public static ArrayList<String> getAssociatedFlightNumbers(int aircraftID) {
        Connection connection = getOnlyInstance();
        String sql = "SELECT FlightNum FROM FLIGHTS WHERE AircraftID = ?";
        
        try {
            ArrayList<String> associatedFlights = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, aircraftID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String flightNum = resultSet.getString("FlightNum");
                associatedFlights.add(flightNum);
            }
            
            return associatedFlights;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., log or throw a runtime exception)
            throw new RuntimeException("Failed to get associated flight numbers.");
        }
    }    

    public static ArrayList<Flight> browseAvailableFlights() {
        Connection connection = getOnlyInstance();
        String sql = "SELECT * FROM FLIGHTS";
        ArrayList<Flight> availableFlights = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                String flightNum = resultSet.getString("FlightNum");
                String[] departureDate = resultSet.getString("departureDate").split("-");
                int day = Integer.parseInt(departureDate[2]);
                int month = Integer.parseInt(departureDate[1]);
                int year = Integer.parseInt(departureDate[0]);
                Date departure = new Date(day, month, year);
                int aircraftID = resultSet.getInt("AircraftID");
                String flightDest = resultSet.getString("FlightDest");
                String depTime = resultSet.getString("leaveTime");
                String flightStart = resultSet.getString("FlightStart");
    
                // Assuming you have a Flight constructor that takes these parameters
                Flight flight = new Flight(flightNum, departure, aircraftID, flightDest, flightStart, depTime);
                availableFlights.add(flight);
            }
    
            return availableFlights;
        } catch (SQLException e) {
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

    public static void updateAccount(boolean news, String email) {
        try {
            Connection connection = getOnlyInstance();
            String query = "UPDATE USERS SET recieveNews = ? WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, news);
                preparedStatement.setString(2, email);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean signUp(String firstName, String lastName, String address, String email, String password, boolean receivePromotion) {
        if (firstName == null || lastName == null || address == null || email == null || password == null) {
            return false;
        }
        try {
            Connection connection = getOnlyInstance();
            String query = "INSERT INTO USERS (FirstName, LastName, Email, Address, Password, recieveNews) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, address);
                preparedStatement.setString(5, password);
                preparedStatement.setBoolean(6, receivePromotion);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addFlight(Flight flight){
        try {
            Connection connection = getOnlyInstance();
            String query = "INSERT INTO FLIGHTS (FlightNum, FlightStart, FlightDest, AircraftID, DepartureDate) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, flight.getFlightNum());
                preparedStatement.setString(2, flight.getStartPoint());
                preparedStatement.setString(3, flight.getDestination());
                preparedStatement.setInt(4, flight.getAircraftID());
                preparedStatement.setString(5, flight.getDepartureDate().getFormattedDate());
                preparedStatement.executeUpdate();
            }
            String insert = "INSERT INTO SEATS (SeatNum, FlightNum, Available, SeatType, Price) VALUES" +
            "('A1', ?, TRUE, 'Business', 210)," +
            "('A2', ?, TRUE, 'Business', 210)," +
            "('A3', ?, TRUE, 'Business', 210)," +
            "('B1', ?, TRUE, 'Comfort', 140)," +
            "('B2', ?, TRUE, 'Comfort', 140)," +
            "('B3', ?, TRUE, 'Comfort', 140)," +
            "('C1', ?, TRUE, 'Ordinary', 100)," +
            "('C2', ?, TRUE, 'Ordinary', 100)," +
            "('C3', ?, TRUE, 'Ordinary', 100)," +
            "('D1', ?, TRUE, 'Ordinary', 100)," +
            "('D2', ?, TRUE, 'Ordinary', 100)," +
            "('D3', ?, TRUE, 'Ordinary', 100);";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                // Replace each ? with the actual flight number
                String flightNumber = flight.getFlightNum();
                for (int i = 1; i <= 12; i++) {
                    preparedStatement.setString(i, flightNumber);
                }
                // Execute the statement
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void browseAircraft() {
        Connection connection = getOnlyInstance();
        String sql = "SELECT * FROM AIRCRAFT";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Clear the existing aircrafts list before populating it with new data
            aircrafts.clear();
            while (resultSet.next()) {
                int aircraftID = resultSet.getInt("AircraftID");
                String aircraftType = resultSet.getString("PlaneType");
                int capacity = resultSet.getInt("NumSeats");
    
                Aircraft aircraft = new Aircraft(aircraftID, aircraftType, capacity);
                aircrafts.add(aircraft);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., log or throw a runtime exception)
            throw new RuntimeException("Failed to browse available aircrafts.");
        }
    }

    public static void getFlightAircraft() {
        Connection connection = getOnlyInstance();
        ArrayList<Aircraft> aircrafts = new ArrayList<>();
        String sql = "SELECT * FROM AIRCRAFT A INNER JOIN Flights F ON F.AircraftID = A.AircraftID WHERE A.AircraftID = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Clear the existing aircrafts list before populating it with new data
            aircrafts.clear();
            while (resultSet.next()) {
                int aircraftID = resultSet.getInt("AircraftID");
                String aircraftType = resultSet.getString("PlaneType");
                int capacity = resultSet.getInt("NumSeats");
    
                Aircraft aircraft = new Aircraft(aircraftID, aircraftType, capacity);
                aircrafts.add(aircraft);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., log or throw a runtime exception)
            throw new RuntimeException("Failed to browse available aircrafts.");
        }
    }


    public static void addAircraft(Aircraft plane){
        try {
            Connection connection = getOnlyInstance();
            String query = "INSERT INTO AIRCRAFT (PlaneType, NumSeats) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, plane.getAircraftType());
                preparedStatement.setInt(2, plane.getNumSeats());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removeAircraft(int id) {
        try {
            Connection connection = getOnlyInstance();
            String query = "DELETE FROM AIRCRAFT WHERE AircraftID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Crew> browseCrew(String flightNumber) {
        String sql = "SELECT * FROM CREW WHERE FlightNum = ?";
        ArrayList<Crew> crew = new ArrayList<>();
        try  {
            Connection connection = getOnlyInstance();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, flightNumber); // Set the flightNumber parameter
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String flightNum = resultSet.getString("FlightNum");
                    Crew crewMem = new Crew(firstName, lastName, flightNum);
                    crew.add(crewMem);
                }
            }
            Aircraft.setCrew(crew);
            return crew;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to browse crew.");
        }
    }    


    public static void addCrew(Crew crew){
        try {
            Connection connection = getOnlyInstance();
            String query = "INSERT INTO CREW (FirstName, LastName, FlightNum) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, crew.getFirstName());
                preparedStatement.setString(2, crew.getLastName());
                preparedStatement.setString(3, crew.getFlightNumber());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removeCrew(Crew crew) {
        try {
            Connection connection = getOnlyInstance();
            String query = "DELETE FROM CREW WHERE FirstName = ? AND LastName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, crew.getFirstName());
                preparedStatement.setString(2, crew.getLastName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static ArrayList<String> browseUsers(){
        Connection connection = getOnlyInstance();
        String sql = "SELECT * FROM USERS";
        ArrayList<String> users = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String email = resultSet.getString("Email");

                String userInfo = String.format("Name: %s %s, Email: %s", firstName, lastName, email);
                users.add(userInfo);  
            }
            return users;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to browse registeres users.");
        } 
    }


    public static void removeFlight(String flightNum) {
        try {
            Connection connection = getOnlyInstance();
            String query = "DELETE FROM FLIGHTS WHERE FlightNum = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, flightNum);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> searchFlights(String departureDate, String flightNum, String flightDest, String flightStart) {
        Connection connection = getOnlyInstance();
        ArrayList<String> matchingFlights = new ArrayList<>();
        // Constructing the base of the SQL query
        StringBuilder sql = new StringBuilder("SELECT DISTINCT * FROM FLIGHTS F INNER JOIN AIRCRAFT A ON F.AircraftID = A.AircraftID");
        // List to keep track of parameters to be set in the prepared statement
        ArrayList<Object> parameters = new ArrayList<>();
        // Check and append conditions based on the provided parameters
        if (departureDate != null && !departureDate.isEmpty()) {
            sql.append(" WHERE F.DepartureDate = ? ");
            parameters.add(departureDate);
        }
        if (flightNum != null && !flightNum.isEmpty()) {
            if (!parameters.isEmpty()) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }
            sql.append("F.FlightNum = ? ");
            parameters.add(flightNum);
        }
        if (flightDest != null && !flightDest.isEmpty()) {
            if (!parameters.isEmpty()) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }
            sql.append("F.FlightDest = ? ");
            parameters.add(flightDest);
        }
        if (flightStart != null && !flightStart.isEmpty()) {
            if (!parameters.isEmpty()) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }
            sql.append("F.FlightStart = ? ");
            parameters.add(flightStart);
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
                int numseats = resultSet.getInt("NumSeats");
                String aircraftType = resultSet.getString("PlaneType");
                String departure = resultSet.getString("DepartureDate");
                String start = resultSet.getString("FlightStart");
                String depTime = resultSet.getString("leaveTime");
                String flightInfo = String.format(
                        "Flight Number: '%s'  Destination: '%s'  Departure Location: '%s'  AircraftID: '%s'  Aircraft Type: '%s'  Total Seats: '%s'  Departure: '%s'  DepartureTime: '%s'",
                        flightNumber, destination, start, aircraft, aircraftType, numseats, departure, depTime);
                matchingFlights.add(flightInfo);
            }
            return matchingFlights;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search flights.");
        }
    }
    
    public static ArrayList<Seat> getSeatsForFlight(String flightNum) {
        ArrayList<Seat> seats = new ArrayList<>();
        Connection connection = getOnlyInstance();
        try {
            String query = "SELECT * FROM SEATS WHERE FlightNum = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, flightNum);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String seatNum = resultSet.getString("SeatNum");
                        boolean available = resultSet.getBoolean("Available");
                        String seatType = resultSet.getString("SeatType");
                        int price = resultSet.getInt("Price");
                        Seat seat = new Seat(seatNum, available, seatType, price);
                        seats.add(seat);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public static void purchaseTicket(String fname, String lname, String email, String flightnum, String seatNum, boolean Insurance, double price) {
        Connection connection = getOnlyInstance();
        try {
            // Insert passenger information into PASSENGERS table
            String insertPassengerQuery = "INSERT INTO PASSENGERS (FirstName, LastName, Email, FlightNum, SeatNum, insurance, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement passengerStatement = connection.prepareStatement(insertPassengerQuery)) {
                passengerStatement.setString(1, fname);
                passengerStatement.setString(2, lname);
                passengerStatement.setString(3, email);
                passengerStatement.setString(4, flightnum);
                passengerStatement.setString(5, seatNum);
                passengerStatement.setBoolean(6, Insurance);
                passengerStatement.setDouble(7, price);
                passengerStatement.executeUpdate();
            }
            // Update the Available field in Seats table
            String updateSeatsQuery = "UPDATE SEATS SET Available = FALSE WHERE FlightNum = ? AND SeatNum = ?";
            try (PreparedStatement seatsStatement = connection.prepareStatement(updateSeatsQuery)) {
                seatsStatement.setString(1, flightnum);
                seatsStatement.setString(2, seatNum);
                seatsStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Purchase> getPurchases(String email) {
        ArrayList<Purchase> purchases = new ArrayList<>();
        Connection connection = getOnlyInstance();
        // Define the SQL query
        String sql = "SELECT DISTINCT P.firstName, P.lounge, P.lastName, P.flightNum, P.seatNum, P.insurance, P.price, F.FlightStart, F.FlightDest, F.leaveTime, F.departureDate, S.SeatType FROM passengers P INNER JOIN flights F ON P.flightNum = F.flightNum INNER JOIN Seats S ON S.SeatNum = P.SeatNum WHERE P.email = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the parameters
            preparedStatement.setString(1, email);
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            // Process the result set and add Purchase objects to the ArrayList
            while (resultSet.next()) {
                String fName = resultSet.getString("firstName");
                String lName = resultSet.getString("lastName");
                String flightNum = resultSet.getString("flightNum");
                String seatNum = resultSet.getString("seatNum");
                String deptime = resultSet.getString("leaveTIme");
                String price = resultSet.getString("price");
                String flightStart = resultSet.getString("FlightStart");
                Boolean insurance = resultSet.getBoolean("insurance");
                Boolean lounge = resultSet.getBoolean("lounge");
                String flightDest = resultSet.getString("FlightDest");
                String seatType = resultSet.getString("SeatType");
                String[] departureDate = resultSet.getString("departureDate").split("-");
                int day = Integer.parseInt(departureDate[0]);
                int month = Integer.parseInt(departureDate[1]);
                int year = Integer.parseInt(departureDate[2]);
                double intprice = Double.parseDouble(price);
                Date depDate = new Date(day, month, year);
                Seat seat = new Seat(seatNum, false, seatType, intprice);
                Flight flight = new Flight(flightNum, depDate, null, flightDest, flightStart, deptime);
                // Purchase purchase = new Purchase(fName, lName, flightNum, seatNum, flightStart, flightDest, depDate, price, insurance);
                Purchase purchase = new Purchase(fName, lName, seat, flight, insurance, price, lounge);
                purchases.add(purchase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchases;
    }

    public static int CancelBooking(Purchase purchase) {
        Connection connection = getOnlyInstance();
        // Define the SQL query to delete a booking
        String sql = "DELETE FROM PASSENGERS WHERE Email = ? AND FlightNum = ? AND SeatNum = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the query
            preparedStatement.setString(1, User.getEmail());
            preparedStatement.setString(2, purchase.getFlight().getFlightNum());
            preparedStatement.setString(3, purchase.getSeat().getSeatNum());
            // Execute the DELETE query
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected;
        } catch (SQLException e) {
            // Handle the exception appropriately (logging, throwing, etc.)
            e.printStackTrace();
        }
        return 0;
    }

    public static void changeFlightOrigin(String flightNum, String origin){
        try {
            Connection connection = getOnlyInstance();
            String query = "UPDATE FLIGHTS SET FlightStart = ? WHERE FlightNum = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, origin);
                preparedStatement.setString(2, flightNum);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeFlightDestination(String flightNum, String destination){
        try {
            Connection connection = getOnlyInstance();
            String query = "UPDATE FLIGHTS SET FlightDest = ? WHERE FlightNum = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, destination);
                preparedStatement.setString(2, flightNum);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeAircraft(String flightNum, String aircraftID){
        try {
            Connection connection = getOnlyInstance();
            String query = "UPDATE FLIGHTS SET AircraftID = ? WHERE FlightNum = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, aircraftID);
                preparedStatement.setString(2, flightNum);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeDepartureDate(String flightNum, String departureDate){
        try {
            Connection connection = getOnlyInstance();
            String query = "UPDATE FLIGHTS SET DepartureDate = ? WHERE FlightNum = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, departureDate);
                preparedStatement.setString(2, flightNum);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     public static void changeDepartureTime(String flightNum, String departureTime){
        try {
            Connection connection = getOnlyInstance();
            String query = "UPDATE FLIGHTS SET leaveTime = ? WHERE FlightNum = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, departureTime);
                preparedStatement.setString(2, flightNum);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}