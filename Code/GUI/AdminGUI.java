package Code.GUI;

import javax.swing.*;

import Code.Control.DatabaseController;
import Code.Control.GenerateUsersFile;
import Code.Entity.Aircraft;
import Code.Entity.Crew;
import Code.Entity.Date;
import Code.Entity.Flight;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;    

public class AdminGUI extends JFrame {
    private JPanel panel;

    // For Search Flights functionality
    private JFrame frame;
    private JTextField departureDateField;
    private JTextField flightNumField;
    private JTextField flightStartField;
    private JTextField flightDestField;
    private JEditorPane resultArea; // Replaced JTextArea with JEditorPane
    private JScrollPane resultScrollPane;

    public AdminGUI() {
        this.panel = new JPanel(new BorderLayout());

        setTitle("Admin - ENSF480 Airline");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton logOutButton = new JButton("Logout");
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });

        // Create menu items
        JMenuItem browseFlightsItem = createMenuItem("Browse Flights");
        JMenuItem searchFlightsItem = createMenuItem("Search Flights");
        JMenuItem browseCrewsItem = createMenuItem("Browse Crews");
        JMenuItem browseAircraftsItem = createMenuItem("Browse Aircrafts");
        JMenuItem printUsersItem = createMenuItem("Print Users");

        // Create menus & menu bar
        JMenu flightMenu = createMenu("Flights", browseFlightsItem, searchFlightsItem, browseCrewsItem);
        JMenu aircraftMenu = createMenu("Aircrafts", browseAircraftsItem);
        JMenu userMenu = createMenu("Users", printUsersItem);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(flightMenu);
        menuBar.add(aircraftMenu);
        menuBar.add(userMenu);
        setFontForMenuBar(menuBar, new Font("Arial", Font.BOLD, 14));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Add components to the panel using GridBagLayout
        JPanel titlePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0); // Add margins
        titlePanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1; // Make it fill the horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make it horizontally fill the available space
        titlePanel.add(menuBar, gbc);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoutPanel.add(logOutButton);

        // Add panels and menu bar to the frame
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(logoutPanel, BorderLayout.SOUTH);

        // Create a JTextArea for displaying text and cat pixel art
        JTextArea mainTextArea = new JTextArea();
        mainTextArea.setEditable(false); // Set text area as non-editable
        mainTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Use a monospaced font for proper alignment
        mainTextArea.setText("\n\n\n" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣠⣄⣀⣀⣀⣀⣀\n" + 
            "⠀⠀⠀⣠⠤⣤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣴⠶⠛⠛⢉⣽⠿⠛⣋⣉⣭⠭⠿⠓⠶⣄⡀\n" + 
            "⣠⡴⠋⠁⠀⠀⠀⠉⠉⠛⠒⠲⠤⣤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⢴⠚⠋⣽⠟⠲⡄⠀⠈⠳⠚⠉⣉⣤⠤⠤⠤⠤⢄⣸⡇\n" + 
            "⠛⠦⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢉⣹⢿⣶⠦⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠴⠚⠉⣼⠞⢢⡀⠘⠷⠖⠃⣀⡤⠴⠚⠉⠁⠀⠀⠀⠀⠀⠀⣸⠧\n" + 
            "⠀⠀⠈⠙⠶⣄⡀⠀⠀⠀⠀⠀⣠⠶⠛⠉⢻⣄⣈⡇⢀⣀⣽⠿⣗⠶⠤⣄⣴⠚⠉⣶⠞⢲⠀⠘⠷⠚⢁⣀⠴⠖⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⠋\n" + 
            "⠀⠀⠀⠀⠀⠈⠙⠲⣄⡀⠀⣾⠁⠀⠀⣀⡤⣾⡿⠟⠉⠘⢧⣀⣘⡷⠀⠀⠈⠉⣳⣾⠟⠉⢀⣠⠴⠛⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡾⠁\n" + 
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠳⣾⣗⠚⠋⠁⣼⠋⠀⠀⠀⢀⣠⠟⠉⠀⣀⡤⠖⠋⣁⣤⠶⠛⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠋\n" + 
            "⠀⠀⠀⠀⢀⣤⣄⡀⠀⠀⠀⠀⠉⠛⢶⣤⡛⠲⠶⠒⠋⢉⣀⡴⠖⢋⣁⡤⠖⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠴⠚⠁\n" + 
            "⠀⠀⠀⠀⢸⡇⠀⠉⠳⢤⡀⠀⢀⣠⠶⣿⣙⡳⢦⡴⠞⢋⣡⡴⠖⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⠴⠚⠉\n" + 
            "⠀⠀⠀⠀⠘⣇⠀⠀⠀⠀⣩⠖⣿⣗⠀⠿⠼⢃⣠⡴⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⣶⣞⣯⠁\n" + 
            "⠀⣠⠤⠤⠤⠼⣤⣤⣴⣊⡀⠀⠛⣾⣡⡴⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣤⠴⣲⠿⠛⠉⠹⣧⣼⣏⠳⣄\n" + 
            "⠀⠳⣤⣀⠀⠀⠀⠀⣀⣴⣋⠴⠞⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡤⠴⠖⠛⠙⠣⣄⠐⣯⣀⣀⣤⣴⠿⠛⣟⠙⡏⢳⡄\n" + 
            "⠀⠀⠀⠉⠙⠛⣿⡿⠛⠉⠀⠀⠀⢀⣀⣀⣀⣀⣤⠤⠴⠶⠚⠋⠉⠀⠀⠀⠀⠀⠀⠀⠈⠙⠷⣄⣀⣿⣁⣀⣤⠼⠛⠁⠀⠙⢦\n" + 
            "⠀⠀⠀⠀⠀⠀⠈⠉⠋⠉⠉⠙⠿⣍⡉⠉⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠿⣤⣀⠀⢀⣀⣠⠤⠴⠚⠁\n" + 
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠓⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠉⠉⠉⠁");
        mainTextArea.setBackground(new Color(151, 220, 230));
        // Add the text area to the center of the frame
        panel.add(new JScrollPane(mainTextArea), BorderLayout.CENTER);

        add(panel);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void goBackToLogin() {
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
        setVisible(false);
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMenuItem(text);
            }
        });
        return menuItem;
    }

    private JMenu createMenu(String text, JMenuItem... items) {
        JMenu menu = new JMenu(text);
        menu.setMargin(new Insets(10, 10, 10, 10));
        for (JMenuItem item : items) {
            menu.add(item);
        }
        return menu;
    }

    private void setFontForMenuBar(JMenuBar menuBar, Font font) {
        for (int i = 0; i < menuBar.getComponentCount(); i++) {
            Component menuComponent = menuBar.getComponent(i);
            if (menuComponent instanceof JMenu) {
                JMenu menu = (JMenu) menuComponent;
                menu.setFont(font);
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem menuItem = menu.getItem(j);
                    if (menuItem != null) {
                        menuItem.setFont(font);
                    }
                }
            }
        }
    }

    private void handleMenuItem(String text) {
        switch (text) {
            case "Browse Flights":
                browseFlights();
                break;
            case "Search Flights":
                searchFlightsForm();
                break;
            case "Browse Crews":
                browseCrews();
                break;
            case "Browse Aircrafts":
                browseAircrafts();
                break;
            case "Print Users":
                printUsers();
                break;
        }
    }

// BROWSE FLIGHTS -------------------------------------------------------------------------------->
    private void browseFlights() {
        // Call the method in DatabaseController to retrieve available flights
        ArrayList<Flight> flights = DatabaseController.browseAvailableFlights();

        // Create a panel for displaying available flights using buttons
        JPanel flightPanel = new JPanel();
        flightPanel.setLayout(new BoxLayout(flightPanel, BoxLayout.Y_AXIS));

        if (flights.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flights available.");
        } else {
            // Create a new JFrame for the flight list
            JFrame flightListFrame = new JFrame("Flight Information");
            flightListFrame.setPreferredSize(new Dimension(720, 500));
            for (Flight flight : flights) {
                StringBuilder flightInfo = new StringBuilder();
                flightInfo.append("Flight Number: ").append(flight.getFlightNum());
                flightInfo.append(" Departure Date: ").append(flight.getDepartureDate().getFormattedDate());
                flightInfo.append(" Departure Location: ").append(flight.getStartPoint());
                flightInfo.append(" Destination: ").append(flight.getDestination());
                flightInfo.append(" Aircraft ID: ").append(flight.getAircraftID()).append('\n');

                JButton flightButton = new JButton(flightInfo.toString());

                // Set the maximum size to make the button extend horizontally
                flightButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, flightButton.getPreferredSize().height));

                flightButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showFlightDetails(flight, flightListFrame);
                    }
                });

                flightPanel.add(Box.createVerticalStrut(15)); // Add spacing between entries
                flightPanel.add(flightButton);
            }

            // Create a panel for additional buttons (Add Flight & Search)
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // Add button to add a flight
            JButton addFlightButton = new JButton("Add Flight");
            addFlightButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAddFlightForm(flightListFrame);
                }
            });
            buttonPanel.add(addFlightButton);

            // Create a panel for the entire content
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(flightPanel, BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            JScrollPane scrollPane = new JScrollPane(contentPanel);

            flightListFrame.add(scrollPane);
            flightListFrame.pack();
            flightListFrame.setLocationRelativeTo(this);
            flightListFrame.setVisible(true);
        }
    }

    private void showFlightDetails(Flight flight, JFrame flightListFrame) {
        JDialog detailsDialog = new JDialog(this, "Flight Details", Dialog.ModalityType.APPLICATION_MODAL);
        detailsDialog.setSize(400, 200);
    
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Flight Details");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
    
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        JLabel flightNumLabel = new JLabel("Flight Number: " + flight.getFlightNum());
        JLabel departureDateLabel = new JLabel("Departure Date: " + flight.getDepartureDate().getFormattedDate());
        JLabel departureLocationLabel = new JLabel("Departure Location: " + flight.getStartPoint());
        JLabel destinationLabel = new JLabel("Destination: " + flight.getDestination());
    
        detailsPanel.add(flightNumLabel);
        detailsPanel.add(departureDateLabel);
        detailsPanel.add(departureLocationLabel);
        detailsPanel.add(destinationLabel);
    
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
        JButton removeButton = new JButton("Remove Flight");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(detailsDialog,
                        "Are you sure you want to remove this flight?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Handle removing the flight
                    DatabaseController.removeFlight(flight.getFlightNum());
                    flightListFrame.dispose(); // Close flight list
                    JOptionPane.showMessageDialog(detailsDialog, "Flight removed successfully.");
                    detailsDialog.dispose(); // Close the details dialog
                    browseFlights(); // Refresh flight...
                }
            }
        });
        buttonPanel.add(removeButton);
        
        JButton updateButton = new JButton("Update Flight");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a dialog for selecting the update type
                String[] options = {"Change Flight Origin", "Change Flight Destination", "Change Aircraft", "Change Departure Date"};
                String adjustment = (String) JOptionPane.showInputDialog(
                    detailsDialog,
                    "Select the update type:",
                    "Update Type",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

                if (adjustment != null) { // User selected an option
                    // Handle updating the flight based on the selected adjustment
                    if (manageFlightUpdate(adjustment, flight)) {
                        // Only show the success message if the update was successful
                        flightListFrame.dispose(); // Close flight list
                        JOptionPane.showMessageDialog(detailsDialog, "Flight updated successfully.");
                        detailsDialog.dispose(); // Close the details dialog
                        browseFlights(); // Refresh flight...
                    }
                }
            }
        });
        buttonPanel.add(updateButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        detailsDialog.add(mainPanel);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setVisible(true);
    }
    
    public boolean manageFlightUpdate(String adjustment, Flight flight) {
        String flightNum = flight.getFlightNum();
        String userInput = null;
    
        switch (adjustment) {
            case "Change Flight Origin":
                userInput = showUpdateForm("Enter new flight origin:", "Change Flight Origin", flight.getStartPoint());
                DatabaseController.changeFlightOrigin(flightNum, userInput);
                break;
            case "Change Flight Destination":
                userInput = showUpdateForm("Enter new flight destination:", "Change Flight Destination", flight.getDestination());
                DatabaseController.changeFlightDestination(flightNum, userInput);
                break;
            case "Change Aircraft":
                userInput = showUpdateForm("Enter new aircraft ID:", "Change Aircraft ID", String.valueOf(flight.getAircraftID()));
                DatabaseController.changeAircraft(flightNum, userInput);
                break;
            case "Change Departure Date":
                userInput = showUpdateForm("Enter new departure date:", "Change Departure Date", flight.getDepartureDate().getFormattedDate());
                DatabaseController.changeDepartureDate(flightNum, userInput);
                break;
            default:
                return false; // Indicate that the update was not successful for unknown adjustment types
        }
        return userInput != null; // Indicate if the update was successful
    }    

    private String showUpdateForm(String message, String title, String defaultValue) {
        Object result = JOptionPane.showInputDialog(
                null,
                message,
                title,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                defaultValue
        );
    
        // Return the result string if the user provided input; otherwise, return null
        return (result != null) ? result.toString() : null;
    }    

    private void showAddFlightForm(JFrame flightListFrame) {
        JTextField flightNumField = new JTextField();
        JTextField departureDateField = new JTextField();
        JTextField departureLocationField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField aircraftIDField = new JTextField();
    
        JPanel p = new JPanel(new GridLayout(5, 2));
        p.add(new JLabel("Flight Number:"));
        p.add(flightNumField);
        p.add(new JLabel("Departure Date:"));
        p.add(departureDateField);
        p.add(new JLabel("Departure Location:"));
        p.add(departureLocationField);
        p.add(new JLabel("Destination:"));
        p.add(destinationField);
        p.add(new JLabel("Aircraft ID:"));
        p.add(aircraftIDField);
    
        int result = JOptionPane.showConfirmDialog(
                this,
                p,
                "Add Flight",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                String flightNumber = flightNumField.getText().trim();
                String departureDateStr = departureDateField.getText().trim();
                String departureLocation = departureLocationField.getText().trim();
                String destination = destinationField.getText().trim();
                String aircraftid = aircraftIDField.getText().trim();
                String[] departureDateParts = departureDateStr.split("-");
                int year = Integer.parseInt(departureDateParts[0]);
                int month = Integer.parseInt(departureDateParts[1]);
                int day = Integer.parseInt(departureDateParts[2]);

                Date departureDate = new Date(day, month, year);
    
                if (addFlight(flightNumber, departureDate, departureLocation, destination, aircraftid, flightListFrame)) {
                    JOptionPane.showMessageDialog(this, "Flight added successfully!");
                    browseFlights(); // Refresh flight list...
                }
            } catch (Exception e) {
                System.out.print(e);
                JOptionPane.showMessageDialog(this, "Invalid details. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean addFlight(String flightNumber, Date departureDate, String departureLocation, String destination, String aircraftid, JFrame flightListFrame) {
        try {
            int aid = Integer.parseInt(aircraftid);
    
            // List of valid aircraft IDs or aircraft objects
            ArrayList<Integer> validAircraftIds = getValidAircraftIds();
    
            // Check if the provided aircraftid is valid
            if (validAircraftIds.contains(aid)) {
                Flight newFlight = new Flight(flightNumber, departureDate, aid, destination, departureLocation);
                DatabaseController.addFlight(newFlight);
                flightListFrame.dispose(); // Close main list
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Aircraft ID. Provide a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Aircraft ID. Provide a numeric ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }   

    public static ArrayList<Integer> getValidAircraftIds() {
        DatabaseController.browseAircraft();
        ArrayList<Integer> validAircraftIds = new ArrayList<>();
        
        // Iterate through the existing aircrafts list and extract the IDs
        for (Aircraft aircraft : DatabaseController.aircrafts) {
            validAircraftIds.add(aircraft.getAircraftID());
        }

        return validAircraftIds;
    }

// SEARCH FLIGHTS -------------------------------------------------------------------------------->
    private void searchFlightsForm() {
        // Initialize components
        frame = new JFrame("Search Flights Form");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(940, 610);
    
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding
    
        departureDateField = new JTextField(20);
        departureDateField.setBorder(BorderFactory.createTitledBorder("Departure Date"));
        flightNumField = new JTextField(20);
        flightNumField.setBorder(BorderFactory.createTitledBorder("Flight Num"));
        flightDestField = new JTextField(20);
        flightDestField.setBorder(BorderFactory.createTitledBorder("Flight Dest"));
        flightStartField = new JTextField(20);
        flightStartField.setBorder(BorderFactory.createTitledBorder("Departure Location"));
        JButton searchButton = new JButton("Search");
    
        // Center panel with flight list
        resultArea = new JEditorPane("text/html", "");
        resultArea.setEditable(false);
        resultScrollPane = new JScrollPane(resultArea);
    
        // Add components to the formPanel using GridBagConstraints
        formPanel.add(departureDateField, gbc);
        gbc.gridy++;
        formPanel.add(flightNumField, gbc);
        gbc.gridy++;
        formPanel.add(flightDestField, gbc);
        gbc.gridy++;
        formPanel.add(flightStartField, gbc);
        gbc.gridy++;
        formPanel.add(searchButton, gbc);
        // Add resultScrollPane using GridBagConstraints
        gbc.gridy++;
        gbc.gridwidth = 5; // Set the grid width to span multiple columns
        gbc.fill = GridBagConstraints.BOTH; // Allow the component to expand both horizontally and vertically
        formPanel.add(resultScrollPane, gbc);
    
        // Layout the main frame
        frame.setLayout(new BorderLayout());
        frame.add(resultScrollPane, BorderLayout.CENTER);
        frame.add(formPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
    
        // Add action listener to the search button & initialize
        searchButton.addActionListener(this::searchAction);
        searchAction(null);
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

// BROWSE AIRCRAFTS ------------------------------------------------------------------------------>
    private void browseAircrafts() {
        DatabaseController.browseAircraft();

        // Create a panel for displaying available aircraft using buttons
        JPanel acPanel = new JPanel();
        acPanel.setLayout(new BoxLayout(acPanel, BoxLayout.Y_AXIS));

        if (DatabaseController.aircrafts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No aircrafts available.");
        } else {
            // Create a new JFrame for the aircraft list
            JFrame aircraftListFrame = new JFrame("Aircraft Information");

            for (Aircraft aircraft : DatabaseController.aircrafts) {
                StringBuilder aircraftInfo = new StringBuilder();
                aircraftInfo.append("Aircraft Type: " + aircraft.getAircraftType());
                aircraftInfo.append(" Aircraft ID: " + aircraft.getAircraftID());
                aircraftInfo.append(" Seating Capacity: " + aircraft.getNumSeats()).append('\n');

                JButton acButton = new JButton(aircraftInfo.toString());

                // Set the maximum size to make the button extend horizontally
                acButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, acButton.getPreferredSize().height));

                acButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showAircraftDetails(aircraft, aircraftListFrame);
                    }
                });

                acPanel.add(acButton);
                acPanel.add(Box.createVerticalStrut(15)); // Add spacing between entries
            }

            // Create a panel for additional buttons (Add Aircraft)
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // Add button to add an aircraft
            JButton addAircraftButton = new JButton("Add Aircraft");
            addAircraftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAddAircraftForm(aircraftListFrame);
                }
            });
            buttonPanel.add(addAircraftButton);

            // Create a panel for the entire content
            JPanel contentPanel = new JPanel(new BorderLayout());
            JScrollPane scrollPane = new JScrollPane(acPanel);

            // Set vertical scroll bar policy
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Set preferred size for the scroll pane
            scrollPane.setPreferredSize(new Dimension(404, 300));

            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            aircraftListFrame.add(contentPanel);
            aircraftListFrame.pack();
            aircraftListFrame.setLocationRelativeTo(this);
            aircraftListFrame.setVisible(true);
        }
    }
    
    private void showAddAircraftForm(JFrame aircraftListFrame) {
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();
    
        JPanel p = new JPanel(new GridLayout(2, 2));
        p.add(new JLabel("Aircraft Type:"));
        p.add(nameField);
        p.add(new JLabel("Seating Capacity:"));
        p.add(capacityField);
    
        int result = JOptionPane.showConfirmDialog(
                this,
                p,
                "Add Aircraft",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                String aircraftName = nameField.getText();
                int seatingCapacity = Integer.parseInt(capacityField.getText());
    
                addAircraft(aircraftName, seatingCapacity, aircraftListFrame);
                JOptionPane.showMessageDialog(this, "Aircraft added successfully!");
                browseAircrafts(); // Refresh list...
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid details. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }        
    
    private void showAircraftDetails(Aircraft aircraft, JFrame aircraftListFrame) {
        JDialog detailsDialog = new JDialog(this, "Aircraft Details", Dialog.ModalityType.APPLICATION_MODAL);
        detailsDialog.setSize(400, 200);
    
        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        // Create a panel for the header
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Aircraft Details");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font to bold and size 16
        headerPanel.add(headerLabel);
    
        // Add the header panel to the main panel at the NORTH position
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    
        // Create a panel for the details
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1)); // Adjust the number of rows based on your details
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
    
        // Add JLabels for each detail
        JLabel typeLabel = new JLabel("Type: " + aircraft.getAircraftType());
        JLabel idLabel = new JLabel("ID: " + aircraft.getAircraftID());
        JLabel capacityLabel = new JLabel("Seating Capacity: " + aircraft.getNumSeats());
    
        // Add the detail labels to the details panel
        detailsPanel.add(typeLabel);
        detailsPanel.add(idLabel);
        detailsPanel.add(capacityLabel);
    
        // Add the details panel to the main panel at the CENTER position
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
    
        // Create a button panel for the "Remove Aircraft" button
        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Remove Aircraft");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> associatedFlights = DatabaseController.getAssociatedFlightNumbers(aircraft.getAircraftID());
                
                if (!associatedFlights.isEmpty()) {
                    String flightsMessage = String.join(", ", associatedFlights);
                    String errorMessage = "Cannot remove the aircraft. It is associated with flight(s):\n" + flightsMessage;
                    
                    JOptionPane.showMessageDialog(detailsDialog, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int option = JOptionPane.showConfirmDialog(detailsDialog,
                            "Are you sure you want to remove this aircraft?",
                            "Confirm Removal", JOptionPane.YES_NO_OPTION);
                    
                    if (option == JOptionPane.YES_OPTION) {
                        // Handle removing the aircraft
                        DatabaseController.removeAircraft(aircraft.getAircraftID());
                        aircraftListFrame.dispose(); // Close main list
                        JOptionPane.showMessageDialog(detailsDialog, "Aircraft removed successfully.");
                        detailsDialog.dispose(); // Close the details dialog
                        browseAircrafts(); // Refresh list...
                    }
                }
            }
        });
        buttonPanel.add(removeButton);
    
        // Add the button panel to the main panel at the SOUTH position
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        detailsDialog.add(mainPanel); // Add the main panel to the dialog
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setVisible(true);
    }           
        
    private void addAircraft(String aircraftName, int seatingCapacity, JFrame aircraftListFrame) {
        Aircraft newAircraft = new Aircraft(aircraftName, seatingCapacity);
        DatabaseController.addAircraft(newAircraft);
        aircraftListFrame.dispose(); // Close main list
    }    

// BROWSE CREWS ---------------------------------------------------------------------------------->
    private void browseCrews() {
        // Prompt the user to enter a flight number
        String flightNumber = JOptionPane.showInputDialog(this, "Enter Flight Number:");
    
        // Check if the user canceled or entered an empty string
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Flight number is required. Please try again.");
            return;
        }
    
        // Now call the method with the entered flight number
        browseCrews(flightNumber);
    }
    
    private void browseCrews(String flightNumber) {
        ArrayList<Crew> crews = DatabaseController.browseCrew(flightNumber);
        Aircraft.setCrew(crews);

        if (crews.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No crews available for flight " + flightNumber);
        } else {
            // Create a new JFrame for the crew list
            JFrame crewListFrame = new JFrame("Crew Information");
    
            // Create a panel for crew entries
            JPanel crewPanel = new JPanel();
            crewPanel.setLayout(new BoxLayout(crewPanel, BoxLayout.Y_AXIS));
    
            for (Crew crew : Aircraft.getCrew()) {
                StringBuilder crewInfo = new StringBuilder();
                crewInfo.append("Name: ").append(crew.getFirstName()).append(" ").append(crew.getLastName());
                crewInfo.append(" Flight Number: ").append(crew.getFlightNumber()).append('\n');
    
                JButton crewButton = new JButton(crewInfo.toString());
                crewButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, crewButton.getPreferredSize().height));
    
                crewButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showCrewDetails(crew, flightNumber, crewListFrame);
                    }
                });
    
                crewPanel.add(Box.createVerticalStrut(15)); // Add spacing between entries
                crewPanel.add(crewButton);
            }
    
            // Create a panel for the "Add Crew" button
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
            // Add button to add a crew member
            JButton addCrewButton = new JButton("Add Crew");
            addCrewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAddCrewForm(flightNumber, crewListFrame);
                }
            });
            buttonPanel.add(addCrewButton);
    
            // Create a panel for the entire content
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(crewPanel, BorderLayout.CENTER);
    
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(404, 300));
    
            crewListFrame.add(scrollPane, BorderLayout.CENTER);
            crewListFrame.add(buttonPanel, BorderLayout.SOUTH);
            crewListFrame.pack();
            crewListFrame.setLocationRelativeTo(this);
            crewListFrame.setVisible(true);
        }
    }        
    
    private void showCrewDetails(Crew crew, String flightNumber, JFrame crewListFrame) {
        JDialog detailsDialog = new JDialog(this, "Crew Details", Dialog.ModalityType.APPLICATION_MODAL);
        detailsDialog.setSize(400, 200);
    
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Crew Details");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        JLabel nameLabel = new JLabel("Name: " + crew.getFirstName() + " " + crew.getLastName());
        JLabel flightNumLabel = new JLabel("Flight Number: " + crew.getFlightNumber());
    
        // Align the text to the left
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        flightNumLabel.setHorizontalAlignment(SwingConstants.LEFT);
    
        detailsPanel.add(nameLabel);
        detailsPanel.add(flightNumLabel);
    
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel();
    
        JButton removeButton = new JButton("Remove Crew");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(detailsDialog,
                        "Are you sure you want to remove this crew member?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Handle removing the crew member
                    DatabaseController.removeCrew(crew);
                    crewListFrame.dispose(); // Close crew list
                    JOptionPane.showMessageDialog(detailsDialog, "Crew member removed successfully.");
                    detailsDialog.dispose(); // Close the details dialog
                    browseCrews(flightNumber); // Refresh crew...
                }
            }
        });
        buttonPanel.add(removeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        detailsDialog.add(mainPanel);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setVisible(true);
    }    
    
    private void showAddCrewForm(String flightNumber, JFrame crewListFrame) {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
    
        JPanel p = new JPanel(new GridLayout(2, 2));
        p.add(new JLabel("First Name:"));
        p.add(firstNameField);
        p.add(new JLabel("Last Name:"));
        p.add(lastNameField);
    
        int result = JOptionPane.showConfirmDialog(
                this,
                p,
                "Add Crew",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
    
                if (firstName.isEmpty() || lastName.isEmpty()) { // Check if name(s) empty
                    throw new Exception("Name(s) cannot be empty.");
                }
    
                Crew newCrew = new Crew(firstName, lastName, flightNumber);
                DatabaseController.addCrew(newCrew);
                crewListFrame.dispose(); // Close crew list
                JOptionPane.showMessageDialog(this, "Crew member added successfully!");
                browseCrews(flightNumber); // Refresh crew...
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid details. Try again.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }               

// PRINT USERS ----------------------------------------------------------------------------------->
    private void printUsers() {
        try {
            ArrayList<String> users = DatabaseController.browseUsers();
            new GenerateUsersFile(users);
            // Show a success message
            JOptionPane.showMessageDialog(this, "Print Users executed successfully! File generated", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            // Handle exceptions as needed
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during Print Users", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void display() {
        // Show the frame
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}
