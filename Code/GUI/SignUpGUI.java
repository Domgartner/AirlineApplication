package Code.GUI;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import javax.swing.*;

import Code.Control.DatabaseController;
import Code.Control.EmailNotif;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpGUI extends JFrame implements EmailNotif {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox promotionCheckBox;

    public SignUpGUI() {
        setTitle("Sign Up - ENSF480 Airline");
        setSize(400, 350);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Buttons
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });

        // Back button in the top-left corner
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(backButton, gbc);

        // Title label
        JLabel titleLabel = new JLabel("Sign Up - ENSF480 Airline");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);

        // Labels and Text Fields
        addFormField(formPanel, gbc, "First Name:", firstNameField = new JTextField(20));
        addFormField(formPanel, gbc, "Last Name:", lastNameField = new JTextField(20));
        addFormField(formPanel, gbc, "Address:", addressField = new JTextField(20));
        addFormField(formPanel, gbc, "Email:", emailField = new JTextField(20));
        addFormField(formPanel, gbc, "Password:", passwordField = new JPasswordField(20));

        // Checkbox for Promotion News
        promotionCheckBox = new JCheckBox("Receive Promotion News");
        promotionCheckBox.setSelected(true);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(promotionCheckBox, gbc);

        // Add the form panel to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Sign Up button at the bottom center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(signUpButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        JLabel formLabel = new JLabel(label);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(formLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    private void handleSignUp() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        boolean receivePromotion = promotionCheckBox.isSelected();
        boolean worked = DatabaseController.signUp(firstName, lastName, address, email, password, receivePromotion);
        sendEmail();
        if(promotionCheckBox.isSelected()) {
            sendEmailPromo();
        }
        if(worked) {
            setVisible(false);
            new LoginForm().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed. Please check your information and try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBackToLogin() {
        // Close the SignUpGUI
        setVisible(false);
        // Open the LoginForm
        new LoginForm().setVisible(true);
    }

    public void sendEmail() {
        String senderEmail = "ensf480airline@gmail.com";
        String senderPassword = "qdcn irej hdrz pqdb";
        // Recipient's email address
        String recipientEmail = emailField.getText();
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
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Sign Up Confirmation");
            message.setText(String.format("Thank you %s %s for signing up with ENSF480 Airline.",
                    firstNameField.getText(), lastNameField.getText()));
            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to: " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    public void sendEmailPromo() {
        String senderEmail = "ensf480airline@gmail.com";
        String senderPassword = "qdcn irej hdrz pqdb";
        // Recipient's email address
        String recipientEmail = emailField.getText();
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
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("ENSF 480 Airline Promo");
            message.setText(String.format("Exciting Promo Right Now At ENSF480 Airlines!\n" +
        "\n" +
        "Dear %s %s,\n" +
        "\n" +
        "We hope this message finds you well. We're thrilled to share some exciting news with you! ENSF480 Airlines is currently running fantastic promotions that you won't want to miss.\n" +
        "\n" +
        "Promo Examples:\n" +
        "- Save 20 percent on your next international flight with promo code: WORLD20\n" +
        "- Book a round-trip and enjoy a complimentary upgrade to business class.\n" +
        "- Exclusive 48-hour flash sale: Get double reward points on all bookings!\n" +
        "\n" +
        "These are just a few examples of the amazing offers available to you. Take advantage of these promotions now and make your travel experience with ENSF480 Airlines even more delightful.\n" +
        "\n" +
        "Here are the details:\n" +
        "- Booking Period: 2023/09/22 to 2023/10/04\n" +
        "- Travel Period: 2024/01/01 to 2024/08/31\n" +
        "\n" +
        "Visit our website or use our mobile app to explore all the destinations and book your tickets. Don't miss out on the opportunity to travel in style and comfort.\n" +
        "\n" +
        "Thank you for choosing ENSF480 Airlines. We look forward to serving you on your next journey!\n" +
        "\n" +
        "Safe travels,\n" +
        "The ENSF480 Airlines Team", firstNameField.getText(), lastNameField.getText()));


                // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to: " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

}
