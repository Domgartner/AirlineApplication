package Code.GUI;

import javax.swing.*;
import javax.swing.text.Utilities;

import Code.Control.DatabaseController;
import Code.Entity.Aircraft;
import Code.Entity.Date;
import Code.Entity.Flight;

import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuestGUI extends JFrame{
    private JFrame frame;
    private JTextField departureDateField;
    private JTextField flightNumField;
    private JTextField flightStartField;
    private JTextField flightDestField;
    private JEditorPane resultArea; // Replaced JTextArea with JEditorPane
    private JLabel welcomeLabel;

    public GuestGUI() {
        // Initialize components
        frame = new JFrame("Guest GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(940, 610);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        welcomeLabel = new JLabel("Welcome Guest");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(this::backAction);

        // Top panel with Back button, Welcome, and Sign Up message
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(welcomeLabel);
        JLabel managePurchases = new JLabel("Sign Up to Manage Purchases");
        managePurchases.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(managePurchases);
            
        // Add some vertical spacing between components
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 75));

        topPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with flight list
        resultArea = new JEditorPane("text/html", "");
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Bottom panel with search form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        departureDateField = new JTextField(20);
        departureDateField.setBorder(BorderFactory.createTitledBorder("Departure Date"));
        flightNumField = new JTextField(20);
        flightNumField.setBorder(BorderFactory.createTitledBorder("Flight Num"));
        flightDestField = new JTextField(20);
        flightDestField.setBorder(BorderFactory.createTitledBorder("Flight Dest"));
        flightStartField = new JTextField(20);
        flightStartField.setBorder(BorderFactory.createTitledBorder("Departure Location"));
        JButton searchButton = new JButton("Search");

        formPanel.add(departureDateField);
        formPanel.add(flightNumField);
        formPanel.add(flightDestField);
        formPanel.add(flightStartField);
        formPanel.add(searchButton);

        // Set size for formPanel & center
        formPanel.setPreferredSize(new Dimension(400, 200));
        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.add(formPanel);
        mainPanel.add(centeringPanel, BorderLayout.SOUTH);

        // Layout the main frame
        frame.getContentPane().add(mainPanel);

        // Add action listener to the search button
        searchButton.addActionListener(this::searchAction);

        // Initialize the result area after it's added to the panel
        searchAction(null);

        // Add mouse listener to the result area
        resultArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int offset = resultArea.viewToModel2D(evt.getPoint());
        
                try {
                    // Get the current paragraph element
                    Element paragraph = Utilities.getParagraphElement(resultArea, offset);
                    // Extract the selected block of text
                    String selectedText = resultArea.getText(paragraph.getStartOffset(), paragraph.getEndOffset() - paragraph.getStartOffset()).trim();
                    System.out.println(selectedText);
        
                    // Define a regular expression pattern for values between single quotes
                    String regex = "'([^']+)'";
        
                    // Create a pattern matcher
                    Matcher matcher = Pattern.compile(regex).matcher(selectedText);
        
                    // Initialize variables to store extracted values
                    String flightNumber = null;
                    String destination = null;
                    String departureLocation = null;
                    String aircraftID = null;
                    String aircraftType = null;
                    String totalSeats = null;
                    String departureDateStr = null;
        
                    // Extract values using the matcher
                    int index = 0;
                    while (matcher.find() && index < 7) {
                        String value = matcher.group(1).trim();
                        switch (index) {
                            case 0:
                                flightNumber = value;
                                break;
                            case 1:
                                destination = value;
                                break;
                            case 2:
                                departureLocation = value;
                                break;
                            case 3:
                                aircraftID = value;
                                break;
                            case 4:
                                aircraftType = value;
                                break;
                            case 5:
                                totalSeats = value;
                                break;
                            case 6:
                                departureDateStr = value;
                                break;
                            // Add more cases if needed
                        }
                        index++;
                    }
        
                    // Check if all values are extracted
                    if (flightNumber != null && destination != null && departureLocation != null && aircraftID != null && aircraftType != null && totalSeats != null && departureDateStr != null) {
                        // Create Date object
                        Date departureDate = createDateFromFormattedString(departureDateStr);
                        int intAircraftID = Integer.parseInt(aircraftID);
                        int intTotalSeats = Integer.parseInt(totalSeats);
        
                        // Create Aircraft object
                        Aircraft aircraft = new Aircraft(intAircraftID, aircraftType, intTotalSeats);
        
                        // Handle the click with all extracted information
                        handleFlightClick(flightNumber, departureDate, departureLocation, destination, aircraft);
                    } else {
                        System.out.println("Values not found.");
                    }
        
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Date createDateFromFormattedString(String formattedDate) {
        String[] parts = formattedDate.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return new Date(day, month, year);
    }

    private void searchAction(ActionEvent event) {
        // Perform search based on the flight number
        String flightNum = flightNumField.getText().trim();
        String flightDest = flightDestField.getText().trim();
        String departureTime = departureDateField.getText().trim();
        String flightstart = flightStartField.getText().trim();

        // Check for empty strings and set them to null
        flightNum = flightNum.isEmpty() ? null : flightNum;
        flightDest = flightDest.isEmpty() ? null : flightDest;
        departureTime = departureTime.isEmpty() ? null : departureTime;
        flightstart = flightstart.isEmpty() ? null : flightstart;

        System.out.println(departureTime);
        System.out.println(flightNum);
        System.out.println(flightDest);

        // Use your existing search function here
        ArrayList<String> searchResults = DatabaseController.searchFlights(departureTime, flightNum, flightDest, flightstart);

        // Display the results in the result area
        StringBuilder htmlContent = new StringBuilder("<html>");
        htmlContent.append("<style>body { font-family: Arial; font-size: 9.5px; }");
        htmlContent.append("div { border: 1px solid black; padding: 10px; margin-bottom: 10px; background-color: #c3f3fa; }");
        htmlContent.append("</style>");
        for (String info : searchResults) {
            htmlContent.append("<div>");
            htmlContent.append(info);
            htmlContent.append("</div>");
        }
        htmlContent.append("</html>");

        // Set the HTML content to the JEditorPane
        resultArea.setText(htmlContent.toString());
    }

    private void handleFlightClick(String flightNumber, Date departureDate, String location, String dest, Aircraft aircraft) {
        // Create a new Flight object based on the clicked flight number
        Flight selectedFlight = new Flight(flightNumber, departureDate, aircraft, dest, location);
        setVisible(false);
        // Create an instance of SeatSelectGUI with the selected flight
        SeatSelectGUI seatSelectGUI = new SeatSelectGUI(selectedFlight);
        // Make the SeatSelectGUI visible
        seatSelectGUI.display();
    }

    private void backAction(ActionEvent event) {
        // Dispose of the current frame
        frame.dispose();
        // Create a new instance of LoginFormGUI
        new LoginForm().setVisible(true);
    }

    public void display() {
        // Show the frame
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
