package Code.Entity;

public class Receipt {
    private Flight flight;
    private Seat seat;

    public Receipt(Flight f, Seat s) {
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
