package Code.GUI;

import javax.swing.*;

import Code.Control.DatabaseController;
import Code.Entity.Flight;
import Code.Entity.Seat;

import java.awt.*;
import java.util.ArrayList;

public class SeatSelectGUI extends JFrame {
    private Flight flight;
    private ArrayList<Seat> seats;
    private JFrame frame;
    private JPanel seatPanel;

    public SeatSelectGUI(Flight flight) {
        this.flight = flight;
        this.seats = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        // Initialize components
        frame = new JFrame("Seat Selection - Flight " + flight.getFlightNum());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setTitle(String.format("Flight %s from %s to %s on %s", flight.getFlightNum(), flight.getStartPoint(), flight.getDestination(), flight.getDepartureDate().getFormattedDate()));
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        seats = DatabaseController.getSeatsForFlight(flight.getFlightNum());

        seatPanel = new JPanel(new GridLayout(4, 3, 10, 10)); // Adjust the layout based on your seat arrangement

        // Create and add buttons for each seat
        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatNum());
            seatButton.addActionListener(e -> handleSeatClick(seat));
            updateSeatButton(seatButton, seat);
            seatPanel.add(seatButton);
        }

        JScrollPane scrollPane = new JScrollPane(seatPanel);

        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
    }

    private void updateSeatButton(JButton seatButton, Seat seat) {
        seatButton.setBackground(new Color(151, 220, 230));
        seatButton.setText(String.format("%s\nPrice: %.2f", seat.getSeatNum(), seat.getPrice()));

        seatButton.setToolTipText(String.format("Type: %s, Price: %.2f", seat.getSeatType(), seat.getPrice()));
    }
    

    private void handleSeatClick(Seat seat) {
        if (seat.getAvailable()) {
            // Handle the seat click event (e.g., show details, allow seat selection)
            frame.dispose();
            PaymentForm paymentForm = new PaymentForm(flight, seat);
            paymentForm.display();
        } else {
            // Display an error message if the seat is unavailable
            JOptionPane.showMessageDialog(frame, "Seat is taken. Please choose another seat.", "Seat Unavailable", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public void display() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
