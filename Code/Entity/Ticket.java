package Code.Entity;

public class Ticket {
    private Flight flight;
    private Seat seat;

    public Ticket(Flight f, Seat s) {
        flight = f;
        seat = s;
    }

    public Flight getFlight() {
        return flight;
    }

    public Seat getSeat() {
        return seat;
    }
    
}
