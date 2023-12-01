package Code.GUI;

import javax.swing.*;

import Code.Control.DatabaseController;
import Code.Control.EmailNotif;
import Code.Entity.Flight;
import Code.Entity.Purchase;
import Code.Entity.Receipt;
import Code.Entity.Seat;
import Code.Entity.Ticket;
import Code.Entity.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class PaymentForm extends JFrame implements EmailNotif {
    private Ticket ticket;
    private Receipt receipt;
    private Flight flight;
    private Seat seat;
    private double gst;
    private double totalPrice;
    private JFrame frame;
    private JTextField emailField;
    private JTextField cardNumberField;
    private JTextField expiryDateField; 
    private JTextField securityCodeField;
    private JTextField firstNameField;
    private JTextField lastNameField; 
    private JCheckBox insuranceCheckBox; 
    private JTextArea priceBreakdownArea;

    public PaymentForm(Flight flight, Seat seat) {
        this.flight = flight;
        this.seat = seat;
        initializeComponents();
    }

    private void initializeComponents() {
        frame = new JFrame("Payment Form");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 600); // Increased height for additional information
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Display flight information
        JTextArea flightInfoArea = new JTextArea();
        flightInfoArea.setEditable(false);
        flightInfoArea.append("Flight Summary:\n");
        flightInfoArea.append(String.format("Flight Number: %s\n", flight.getFlightNum()));
        flightInfoArea.append(String.format("Destination: %s\n", flight.getDestination()));
        flightInfoArea.append(String.format("Departure Location: %s\n", flight.getStartPoint()));
        flightInfoArea.append(String.format("Departure Date: %s\n", flight.getDepartureDate().getFormattedDate()));

        // Display seat information
        JTextArea seatInfoArea = new JTextArea();
        seatInfoArea.setEditable(false);
        seatInfoArea.append("Seat Summary:\n");
        seatInfoArea.append(String.format("Seat Number: %s\n", seat.getSeatNum()));
        seatInfoArea.append(String.format("Type: %s\n", seat.getSeatType()));
        seatInfoArea.append(String.format("Price: %.2f\n", seat.getPrice()));

        // Calculate GST and total price
        double gstRate = 0.05;
        gst = seat.getPrice() * gstRate;
        totalPrice = seat.getPrice() + gst;

        // Display price breakdown
        priceBreakdownArea = new JTextArea();
        priceBreakdownArea.setEditable(false);
        priceBreakdownArea.append("Price Breakdown:\n");
        priceBreakdownArea.append(String.format("Seat Price: $%.2f\n", seat.getPrice()));
        priceBreakdownArea.append(String.format("GST (5%%): $%.2f\n", gst));
        priceBreakdownArea.append(String.format("Total Price: $%.2f\n", totalPrice));

        if (User.getEmail() == null || User.getEmail().contains("airlineAgent")) {
            emailField = new JTextField(20);
            emailField.setBorder(BorderFactory.createTitledBorder("Email To Send Ticket To"));
            formPanel.add(emailField);
    
            firstNameField = new JTextField(20);
            firstNameField.setBorder(BorderFactory.createTitledBorder("First Name"));
            formPanel.add(firstNameField);
    
            lastNameField = new JTextField(20);
            lastNameField.setBorder(BorderFactory.createTitledBorder("Last Name"));
            formPanel.add(lastNameField);
        }
        
        cardNumberField = new JTextField(20);
        cardNumberField.setBorder(BorderFactory.createTitledBorder("Credit Card Number"));
        expiryDateField = new JTextField(20); // Initialize expiryDateField
        expiryDateField.setBorder(BorderFactory.createTitledBorder("Expiry Date"));
        securityCodeField = new JTextField(20); // Replace pinField with securityCodeField
        securityCodeField.setBorder(BorderFactory.createTitledBorder("Security Code"));

        insuranceCheckBox = new JCheckBox("Purchase Cancellation Insurance $10"); // Updated insurance checkbox text

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this::submitAction);

        // Add an ItemListener to the insuranceCheckBox
        insuranceCheckBox.addItemListener(e -> {
            boolean isSelected = insuranceCheckBox.isSelected();
            double insurancePrice = isSelected ? 10 : 0;
            double totalWithInsurance = totalPrice + insurancePrice;

            // Update the price breakdown text
            priceBreakdownArea.setText("");
            priceBreakdownArea.append("Price Breakdown:\n");
            priceBreakdownArea.append(String.format("Seat Price: $%.2f\n", seat.getPrice()));
            priceBreakdownArea.append(String.format("GST (5%%): $%.2f\n", gst));
            priceBreakdownArea.append(String.format("Cancellation Insurance $%d\n", isSelected ? 10 : 0));
            priceBreakdownArea.append(String.format("Total Price: $%.2f\n", totalWithInsurance));
        });

        formPanel.add(flightInfoArea);
        formPanel.add(seatInfoArea);
        formPanel.add(priceBreakdownArea);
        formPanel.add(insuranceCheckBox); // Updated insurance checkbox
        
        formPanel.add(cardNumberField);
        formPanel.add(expiryDateField);
        formPanel.add(securityCodeField);
        formPanel.add(submitButton);

        frame.getContentPane().add(BorderLayout.CENTER, formPanel);
    }

    private void submitAction(ActionEvent event) {
        // Perform actions when the Submit button is clicked
        String email = "";
        String firstName = "";
        String lastName = "";
        if (User.getEmail() == null || User.getEmail().contains("airlineAgent")) {
            email = emailField.getText();
            firstName = firstNameField.getText();
            lastName = lastNameField.getText();
            // Check if any of the fields are empty
            if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Do not proceed further if fields are not filled
            }
        } else {
            email = User.getEmail();
            firstName = User.getFirstName();
            lastName = User.getLastName();
        }
        String cardNumber = cardNumberField.getText();
        String expiryDate = expiryDateField.getText();
        String securityCode = securityCodeField.getText();
        boolean purchaseInsurance = insuranceCheckBox.isSelected();
        // Check if any of the fields are empty
        if (cardNumber.isEmpty() || expiryDate.isEmpty() || securityCode.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Do not proceed further if fields are not filled
        }
        System.out.println(seat.getSeatNum());
        DatabaseController.purchaseTicket(firstName, lastName, email, flight.getFlightNum().trim(), seat.getSeatNum().trim(), purchaseInsurance, totalPrice);
        ticket = new Ticket(flight, seat);      // Create a new ticket
        receipt = new Receipt(flight, seat); // Create a new receipt
        sendEmail();
        String price = String.valueOf(totalPrice);
        User.addPurchase(new Purchase(firstName, lastName, seat, flight, insuranceCheckBox.isSelected(), price));
        frame.dispose();
    }
    
    public void display() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    @Override
    public void sendEmail() {
        String senderEmail = "ensf480airline@gmail.com";
        String senderPassword = "qdcn irej hdrz pqdb";
        // Setup properties for the email server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // or your SMTP server
        properties.put("mail.smtp.port", "587"); // or your SMTP server port
        // Create a session with the sender's email and password
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            // Create a MimeMessage
            boolean isSelected = insuranceCheckBox.isSelected();
            double insurancePrice = isSelected ? 10 : 0;
            double totalWithInsurance = totalPrice + insurancePrice;
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            if (User.getEmail() == null || User.getEmail().contains("airlineAgent")) {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailField.getText()));
                message.setText(String.format("%s %s, thank you for purchasing a ticket with ENSF480 Airline. Below contains your ticket and receipt:" +
                "\n\nFlight Summary:\n\tFlight Number: %s\n\tFlight Departure Location: %s\n\tFlight Destination: %s\n\tFlight Date: %s\n\tSeat Number: %s\n\tSeat Type: %s\n\nTicket Summary:\n\tCancellation Insurance: %s\n\tSeat Price: %s\n\tGST: %s\n\tTotal Price: %s", firstNameField.getText(), lastNameField.getText(), receipt.getFlight().getFlightNum(), receipt.getFlight().getStartPoint(), receipt.getFlight().getDestination(),
                receipt.getFlight().getDepartureDate().getFormattedDate(), receipt.getSeat().getSeatNum(), receipt.getSeat().getSeatType(), insuranceCheckBox.isSelected(), receipt.getSeat().getPrice(), gst, totalWithInsurance));
            } else {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(User.getEmail()));
                message.setText(String.format("%s %s, thank you for purchasing a ticket with ENSF480 Airline. Below contains your ticket and receipt:" +
                "\n\nFlight Summary:\n\tFlight Number: %s\n\tFlight Departure Location: %s\n\tFlight Destination: %s\n\tFlight Date: %s\n\tSeat Number: %s\n\tSeat Type: %s\n\nTicket Summary:\n\tCancellation Insurance: %s\n\tSeat Price: %s\n\tGST: %s\n\tTotal Price: %s", User.getFirstName(), User.getLastName(), receipt.getFlight().getFlightNum(), receipt.getFlight().getStartPoint(), receipt.getFlight().getDestination(),
                receipt.getFlight().getDepartureDate().getFormattedDate(), receipt.getSeat().getSeatNum(), receipt.getSeat().getSeatType(),insuranceCheckBox.isSelected(), receipt.getSeat().getPrice(), gst, totalWithInsurance));
            }
            message.setSubject("Sign Up Confirmation");
            // Send the email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}