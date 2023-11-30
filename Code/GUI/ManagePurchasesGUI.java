package Code.GUI;

import javax.swing.*;

import Code.Control.DatabaseController;
import Code.Control.EmailNotif;
import Code.Entity.Purchase;
import Code.Entity.User;

import java.awt.*;
import java.util.ArrayList;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class ManagePurchasesGUI extends JFrame implements EmailNotif{
    private JFrame frame;
    private JPanel mainPanel;

    public ManagePurchasesGUI() {
        // Initialize components
        frame = new JFrame("Manage Purchases");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        displayPurchases();
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
    }

    private void displayPurchases() {
        // ArrayList<Purchase> purchases = DatabaseController.getPurchases(User.getEmail());
        ArrayList<Purchase> purchases = User.getPurchases();
        for (Purchase purchase : purchases) {
            JPanel purchasePanel = new JPanel();
            purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.X_AXIS));
            // Display purchase information using Purchase class functions
            JTextArea purchaseInfo = new JTextArea();
            purchaseInfo.append("Name: " + purchase.getFirstName() + " " + purchase.getLastName() + "\n");
            purchaseInfo.append("Flight Number: " + purchase.getFlight().getFlightNum() + "\n");
            purchaseInfo.append("Seat Number: " + purchase.getSeat().getSeatNum() + "\n");
            purchaseInfo.append("Flight Start: " + purchase.getFlight().getStartPoint() + "\n");
            purchaseInfo.append("Flight Destination: " + purchase.getFlight().getDestination() + "\n");
            purchaseInfo.append("Departure Date: " + purchase.getFlight().getDepartureDate().getFormattedDate() + "\n");
            purchaseInfo.setEditable(false);
            purchaseInfo.setLineWrap(true);
            purchasePanel.add(purchaseInfo);
            // Add some space between purchase information and the "Cancel" button
            purchasePanel.add(Box.createRigidArea(new Dimension(10, 0)));
            // Create a "Cancel" button for each purchase
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> handleCancelAction(purchase));
            purchasePanel.add(cancelButton);
            // Add some space between purchases
            mainPanel.add(purchasePanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Adjust as needed
        }
    }
    
    private void handleCancelAction(Purchase purchase) {
        // Display a confirmation dialog
        if(purchase.getInsurance()) {
            int option = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to cancel the purchase?\n$" + purchase.getPrice() + " will be refunded to your account",
                "Cancel Purchase",
                JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // User clicked "Yes," implement the action to cancel the purchase
                    System.out.println("Cancel action for: " + purchase.getFirstName() + " " + purchase.getLastName());
                    int rowsDeleted = DatabaseController.CancelBooking(purchase);
                    User.removePurchase(purchase);
                    sendEmail(purchase);
                    if(rowsDeleted != 0) {
                        JOptionPane.showConfirmDialog(
                            frame,
                            "Booking cancelled successfully",
                            "Booking Cancelled",
                            JOptionPane.CLOSED_OPTION);
                            frame.dispose();
                    }
                }
        } else {
            int option = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to cancel the purchase?\nNo refund will be issued.",
                "Cancel Purchase",
                JOptionPane.YES_NO_OPTION);
            // Check user's choice
            if (option == JOptionPane.YES_OPTION) {
                // User clicked "Yes," implement the action to cancel the purchase
                System.out.println("Cancel action for: " + purchase.getFirstName() + " " + purchase.getLastName());
                int rowsDeleted = DatabaseController.CancelBooking(purchase);
                User.removePurchase(purchase);
                sendEmail(purchase);
                if(rowsDeleted != 0) {
                    JOptionPane.showConfirmDialog(
                        frame,
                        "Booking cancelled successfully",
                        "Booking Cancelled",
                        JOptionPane.CLOSED_OPTION);
                        frame.dispose();
                }
            }
        }
    }

    public void display() {
        // Show the frame
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    public void sendEmail(Purchase purchase) {
        String senderEmail = "ensf480airline@gmail.com";
        String senderPassword = "qdcn irej hdrz pqdb";
        String recipientEmail = User.getEmail();
        // Setup properties for the email server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); 
        // Create a session with the sender's email and password
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Sign Up Confirmation");
            if(purchase.getInsurance()) {
                message.setText(String.format("Hello %s %s. Your purchased ticket for seat %s on flight %s from %s to %s has been successfully cancelled.\n" +
                "This ticket was purchased with cancellation insurance. $%s will be refunded to your account.\n\n Thank you,\nENSF480 Airline Team", User.getFirstName(), User.getLastName(), purchase.getSeat().getSeatNum(), purchase.getFlight().getFlightNum(), purchase.getFlight().getStartPoint(), purchase.getFlight().getDestination(), purchase.getPrice()));
            } else {
                message.setText(String.format("Hello %s %s. Your purchased ticket for seat %s on flight %s from %s to %s has been successfully cancelled.\n", User.getFirstName(), User.getLastName(), purchase.getSeat().getSeatNum(), purchase.getFlight().getFlightNum(), purchase.getFlight().getStartPoint(), purchase.getFlight().getDestination()));
            }
            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to: " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendEmail() {}
}