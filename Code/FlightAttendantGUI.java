package Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FlightAttendantGUI extends JFrame {
    private JTextField flightNumField;
    private JTextArea passengersTextArea;

    public FlightAttendantGUI(FlightAttendant flightAttendant) {
        setTitle("Flight Attendant Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Flight Attendant Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel flightNumLabel = new JLabel("Enter Flight Number:");
        flightNumField = new JTextField(20);

        JButton browseButton = new JButton("Browse Passengers");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBrowsePassengers();
            }
        });

        passengersTextArea = new JTextArea(15, 40);
        JScrollPane scrollPane = new JScrollPane(passengersTextArea);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        panel.add(flightNumLabel, gbc);

        gbc.gridx = 1;
        panel.add(flightNumField, gbc);

        gbc.gridy = 2;
        panel.add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(scrollPane, gbc);

        gbc.gridy = 4;
        panel.add(logoutButton, gbc);

        add(panel);
    }

    private void handleLogout() {
        dispose();
        new LoginForm().setVisible(true);
    }

    private void handleBrowsePassengers() {
        String flightNum = flightNumField.getText();

        // Assuming FlightAttendant has a static method to browse passengers by flight
        ArrayList<String> passengers = FlightAttendant.browsePassengersByFlight(flightNum);

        // Display passengers in the text area
        passengersTextArea.setText("");
        for (String passenger : passengers) {
            passengersTextArea.append(passenger + "\n");
        }
    }

}
