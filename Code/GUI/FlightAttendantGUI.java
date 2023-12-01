package Code.GUI;

import javax.swing.*;

import Code.Entity.FlightAttendant;
import Code.Entity.User;

import java.awt.*;
import java.util.ArrayList;

public class FlightAttendantGUI extends JFrame {
    private JTextField flightNumField;
    private JTextArea passengersTextArea;

    public FlightAttendantGUI() {
        setTitle("Browse Passengers Dashboard");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Welcome " + User.getFirstName() + " " + User.getLastName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel flightNumLabel = new JLabel("Enter Flight Number:");
        flightNumField = new JTextField(20);

        JButton browseButton = new JButton("Browse Passengers");
        browseButton.addActionListener(e -> handleBrowsePassengers());

        passengersTextArea = new JTextArea(15, 40);
        JScrollPane scrollPane = new JScrollPane(passengersTextArea);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(titleLabel, gbc);
        // Flight Number Label and Field
        gbc.gridy = 1;
        panel.add(flightNumLabel, gbc);
        gbc.gridy = 2;
        panel.add(flightNumField, gbc);
        // Browse Passengers Button
        gbc.gridy = 2; 
        gbc.gridx = 1; 
        panel.add(browseButton, gbc);
        // Passengers Text Area
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Make the text area span two columns
        panel.add(scrollPane, gbc);
        // Logout Button
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(logoutButton, gbc);

        add(panel);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void handleLogout() {
        this.dispose();
        LoginForm log = new LoginForm();
        log.setVisible(true);
    }

    private void handleBrowsePassengers() {
        String flightNum = flightNumField.getText();

        // Assuming FlightAttendant has a static method to browse passengers by flight
        ArrayList<String> passengers = FlightAttendant.browsePassengersByFlight(flightNum);

        // Display passengers in the text area
        passengersTextArea.setEditable(false);
        passengersTextArea.setText("");
        for (String passenger : passengers) {
            passengersTextArea.append(passenger + "\n\n");
        }
    }
}
