package Code;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CustomerGUI {
    private JFrame frame;
    private JTextField departureDateField;
    private JTextField flightNumField;
    private JTextField flightDestField;
    private JTextArea resultArea;

    public CustomerGUI() {
        // Initialize components
        frame = new JFrame("Flight Search");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        departureDateField = new JTextField(20);
        departureDateField.setBorder(BorderFactory.createTitledBorder("Departure Date"));
        flightNumField = new JTextField(20);
        flightNumField.setBorder(BorderFactory.createTitledBorder("Flight Num"));
        flightDestField = new JTextField(20);
        flightDestField.setBorder(BorderFactory.createTitledBorder("Flight Dest"));
        JButton searchButton = new JButton("Search");
        

        formPanel.add(departureDateField);
        formPanel.add(flightNumField);
        formPanel.add(flightDestField);
        formPanel.add(searchButton);
        
        // Results area
        resultArea = new JTextArea(20, 40);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);

        // Layout the main frame
        frame.getContentPane().add(BorderLayout.WEST, formPanel);
        frame.getContentPane().add(BorderLayout.CENTER, resultScrollPane);

        // Add action listener to the search button
        searchButton.addActionListener(this::searchAction);
    }
    
    private void searchAction(ActionEvent event) {
        // Perform search based on the flight number
        String flightNum = flightNumField.getText().trim();
        String flightDest = flightDestField.getText().trim();
        String departureTime = departureDateField.getText().trim();
        
        // Check for empty strings and set them to null
        flightNum = flightNum.isEmpty() ? null : flightNum;
        flightDest = flightDest.isEmpty() ? null : flightDest;
        departureTime = departureTime.isEmpty() ? null : departureTime;
    
        System.out.println(departureTime);
        System.out.println(flightNum);
        System.out.println(flightDest);
    
        // Use your existing search function here
        ArrayList<String> searchResults = DatabaseController.searchFlights(departureTime, flightNum, flightDest);
        
        // Display the results in the result area
        resultArea.setText(""); // Clear previous results
        for (String info : searchResults) {
            resultArea.append(info + "\n\n");
        }
    }
    

    public void display() {
        // Show the frame
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    
}