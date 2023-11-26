package Code;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GuestGUI extends JFrame {
    private JTextField flightNumField;
    private JTextArea passengersTextArea;

    public GuestGUI(Guest guest) {
        setTitle("Welcome Guest!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JLabel flightNumLabel = new JLabel("Enter Flight Number:");
        flightNumField = new JTextField();

        JButton browseButton = new JButton("Browse Passengers");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBrowsePassengers();
            }
        });

        passengersTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(passengersTextArea);

        panel.add(flightNumLabel);
        panel.add(flightNumField);
        panel.add(browseButton);
        panel.add(scrollPane);

        add(panel);
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