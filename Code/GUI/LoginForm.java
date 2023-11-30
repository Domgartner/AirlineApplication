package Code.GUI;
import javax.swing.*;

import Code.Control.DatabaseController;
import Code.Entity.Admin;
import Code.Entity.AirlineAgent;
import Code.Entity.Customer;
import Code.Entity.FlightAttendant;
import Code.Entity.Guest;
import Code.Strategy.AAstrategy;
import Code.Strategy.AdminStrategy;
import Code.Strategy.CustomerStrategy;
import Code.Strategy.FlightAttendantStrategy;
import Code.Strategy.GuestStrategy;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private static LoginForm loginGUI;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("ENSF480 Airline Login");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 0, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ENSF480 Airline");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        formPanel.add(titleLabel);

        emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        formPanel.add(emailField);

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        formPanel.add(loginButton);

        panel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignUp();
            }
        });
        buttonPanel.add(signUpButton);

        JButton guestButton = new JButton("Continue as Guest");
        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueAsGuest();
            }
        });
        buttonPanel.add(guestButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setLocationRelativeTo(null); // Center the frame on the screen
    }
    private void handleLogin() {
        String email = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        String[] status = DatabaseController.checkLogin(email, password);
        System.out.println(status);
        if (status != null) {
            if (email.contains("aircraftAdmin")) {
                // Redirect to aircraft admin functionality
                System.out.println("Redirecting to Aircraft Admin functionality");
                setVisible(false);
                Admin a = new Admin(status[0], status[1], email, password);        // Create Admin object
                AdminStrategy strat = new AdminStrategy();
                a.setStrategy(strat);
                a.performStrategy();
            } else if (email.contains("aircraftAttendant")) {
                // Redirect to regular user functionality
                System.out.println("Redirecting to Aircraft Attendant functionality");
                setVisible(false);
                FlightAttendant fa = new FlightAttendant(status[0], status[1],email, password);        // Create Flight Attendant object
                FlightAttendantStrategy strat = new FlightAttendantStrategy();
                fa.setStrategy(strat);
                fa.performStrategy();
            } else if (email.contains("airlineAgent")) {
                System.out.println("Redirecting to airlineAgent functionality");
                AirlineAgent aa = new AirlineAgent(status[0], status[1],email, password);        // Create Airline Agent object
                AAstrategy strat = new AAstrategy();
                aa.setStrategy(strat);
                aa.performStrategy();
            } else {
                System.out.println("Redirecting to Customer functionality");
                setVisible(false);
                Customer c = new Customer(status[0], status[1], email, password);    
                CustomerStrategy strat = new CustomerStrategy();
                c.setStrategy(strat);
                c.performStrategy();
            }
        } else {
            // Handle incorrect login credentials
            JOptionPane.showMessageDialog(null, "Invalid Login.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void continueAsGuest() {
        // Action for "Continue as Guest" button
        setVisible(false);
        Guest g = new Guest();
        GuestStrategy strat = new GuestStrategy();
        g.setStrategy(strat);
        g.performStrategy();
    }

    private void openSignUp() {
        // Close the current LoginForm
        setVisible(false);
        // Open the SignUpGUI
        new SignUpGUI().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DatabaseController.getOnlyInstance();
                loginGUI = new LoginForm();
                loginGUI.setVisible(true);
            }
        });
    }
}