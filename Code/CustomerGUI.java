package Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerGUI {

    private static Connection connection; // Assume connection is already set up

    public static void main(String[] args) {
        // Create the frame on the event dispatching thread
        SwingUtilities.invokeLater(CustomerGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Create and set up the window
        JFrame frame = new JFrame("Flight Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // Create a panel to hold components
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // Display the window
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Create a label for destination
        JLabel destLabel = new JLabel("Enter Destination:");
        destLabel.setBounds(10, 10, 120, 25);
        panel.add(destLabel);

        // Create a text field for destination input
        JTextField destText = new JTextField(20);
        destText.setBounds(140, 10, 160, 25);
        panel.add(destText);

        // Create a submit button
        JButton submitButton = new JButton("Search");
        submitButton.setBounds(310, 10, 80, 25);
        panel.add(submitButton);

        // Create a text area for flight results
        JTextArea flightResults = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(flightResults);
        scrollPane.setBounds(10, 50, 465, 200);
        panel.add(scrollPane);

        // Action Listener for button click
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destination = destText.getText();
                searchFlights(destination, flightResults);
            }
        });
    }

    private static void searchFlights(String destination, JTextArea flightResults) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM FLIGHTS WHERE FlightDest = '" + destination + "'";
            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set and display in the text area
            StringBuilder results = new StringBuilder();
            while (rs.next()) {
                String flightNum = rs.getString("FlightNum");
                String flightDest = rs.getString("FlightDest");
                int aircraft = rs.getInt("Aircraft");
                String departureDate = rs.getString("DepartureDate");

                results.append("Flight Number: ").append(flightNum)
                        .append(", Destination: ").append(flightDest)
                        .append(", Aircraft: ").append(aircraft)
                        .append(", Departure Date: ").append(departureDate)
                        .append("\n");
            }
            flightResults.setText(results.toString());
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setConnection(Connection conn) {
        connection = conn;
    }
}
