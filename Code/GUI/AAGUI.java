package Code.GUI;
import javax.swing.*;
import javax.swing.text.Utilities;

import Code.Control.DatabaseController;
import Code.Entity.Aircraft;
import Code.Entity.Date;
import Code.Entity.Flight;
import Code.Entity.User;

import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AAGUI {
    private JFrame frame;
    private JTextField departureDateField;
    private JTextField flightNumField;
    private JTextField flightStartField;
    private JTextField flightDestField;
    private JTextArea resultArea;
    private JLabel welcomeLabel;

    public AAGUI() {
        // Initialize components
        frame = new JFrame("AA GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(930, 600);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        welcomeLabel = new JLabel("Welcome " + User.getFirstName()+ " " + User.getLastName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Buttons
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this::logoutAction);
        JButton managePurchasesButton = new JButton("Manage Purchases");
        managePurchasesButton.addActionListener(this::managePurchasesAction);
        JButton browsePassengersButton = new JButton("Browse Passengers");
        browsePassengersButton.addActionListener(this::browsePassengersAction);
        
        // Top panel with Logout button and Welcome
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(logoutButton, BorderLayout.WEST);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(welcomeLabel);
        managePurchasesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        browsePassengersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
        // Add some vertical spacing between components
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 75));

        topPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with flight list
        resultArea = new JTextArea(20, 40);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);

        // Bottom panel with Form & Manage buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
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
        bottomPanel.add(centeringPanel, BorderLayout.CENTER);

        // Manage buttons
        JPanel manageButtonsPanel = new JPanel(new GridLayout(2, 1, 0, 5)); // 1 column, 0 rows
        manageButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // Add padding
        manageButtonsPanel.add(managePurchasesButton);
        manageButtonsPanel.add(browsePassengersButton);
        JPanel containerPanel = new JPanel();
        containerPanel.add(manageButtonsPanel);
        
        bottomPanel.add(containerPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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
        resultArea.setText(""); // Clear previous results
        for (String info : searchResults) {
            resultArea.append(info + "\n\n");
        }
    }

    private void handleFlightClick(String flightNumber, Date departureDate, String location, String dest, Aircraft aircraft) {
        // Create a new Flight object based on the clicked flight number
        Flight selectedFlight = new Flight(flightNumber, departureDate, aircraft, dest, location);
        // frame.dispose();
        // Create an instance of SeatSelectGUI with the selected flight
        SeatSelectGUI seatSelectGUI = new SeatSelectGUI(selectedFlight);
        // Make the SeatSelectGUI visible
        seatSelectGUI.display();
    }

    private void logoutAction(ActionEvent event) {
        // Dispose of the current frame
        frame.dispose();
        // Create a new instance of LoginFormGUI
        new LoginForm().setVisible(true);
    }


    private void managePurchasesAction(ActionEvent event) {
        // Perform actions when the "Manage Purchases" button is clicked
        // Create a new instance of ManagePurchasesGUI
        ManagePurchasesGUI managePurchasesGUI = new ManagePurchasesGUI();
        // Display the ManagePurchasesGUI
        managePurchasesGUI.display();
    }

    private void browsePassengersAction(ActionEvent event) {
        // Perform actions when the "Browse Passengers" button is clicked
        // Add your logic for browsing passengers here
        // For example, you can create a new instance of BrowsePassengersGUI and display it
        FlightAttendantGUI flightAttendantGUI = new FlightAttendantGUI();
        flightAttendantGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        flightAttendantGUI.setVisible(true);
    }
    
    public void display() {
        // Show the frame
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
