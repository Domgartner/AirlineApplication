package ProjectCode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("Welcome to ENSF480 Airline");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10)); // Adjusted for better spacing
        JLabel titleLabel = new JLabel("Welcome to ENSF480 Airline");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Increased font size for emphasis
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        JButton guestButton = new JButton("Continue as Guest");
        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueAsGuest();
            }
        });
        panel.add(titleLabel);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(guestButton);
        add(panel);
    }
    private void handleLogin() {
        String email = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        boolean status = DatabaseManager.checkLogin(email, password);
        System.out.println(status);
        if (status) {
            if (email.contains("aircraftAdmin")) {
                // Redirect to aircraft admin functionality
                System.out.println("Redirecting to Aircraft Admin functionality");
                new Admin(email);        // Create Admin object
            } else if (email.contains("aircraftAttendant")) {
                // Redirect to regular user functionality
                System.out.println("Redirecting to aircraftAttendant functionality");
                new FlightAttendant();        // Create Flight Attendent object
            } else if (email.contains("airlineAgent")) {
                System.out.println("Redirecting to airlineAgent functionality");
                new AirlineAgent();        // Create Airline Agent object
            } else {
                System.out.println("Redirecting to Customer functionality");
                new Customer();     
            }
        } else {
            // Handle incorrect login credentials
            System.out.println("Invalid login credentials");
        }
        System.out.println("Username: " + email);
        System.out.println("Password: " + password);
    }
    private void continueAsGuest() {
        // Action for "Continue as Guest" button
        new Guest();
        System.out.println("Continue as Guest");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getOnlyInstance();
                new LoginGUI().setVisible(true);
            }
        });
    }
}