package Code.GUI;

import javax.swing.*;

import Code.Control.DatabaseController;
import Code.Entity.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountGUI extends JFrame {

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JCheckBox receiveNewsCheckBox;
    private JButton saveButton;

    public AccountGUI() {
        setTitle("Account GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        JLabel titleLabel = new JLabel("Account Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        receiveNewsCheckBox = new JCheckBox("Receive News");
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy = 2;
        panel.add(nameLabel, gbc);

        gbc.gridy = 3;
        panel.add(emailLabel, gbc);

        gbc.gridy = 4;
        panel.add(receiveNewsCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        add(panel);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void handleSave() {
        boolean receiveNews = receiveNewsCheckBox.isSelected();
        DatabaseController.updateAccount(receiveNews, User.getEmail());
        // For simplicity, just displaying a message
        JOptionPane.showMessageDialog(this, "Account Info Saved", "Info", JOptionPane.INFORMATION_MESSAGE);
        setVisible(false);
    }

    public void display() {
        // Set values from the User object
        nameLabel.setText("Name: " + User.getFirstName() + " " + User.getLastName());
        emailLabel.setText("Email: " + User.getEmail());
        receiveNewsCheckBox.setSelected(true);

        // Show the frame
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }

}
