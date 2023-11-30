package Code.Entity;

public class Purchase {
    private String fName;
    private String lName;
    private String price;
    private Boolean insurance;
    private Seat seat;
    private Flight flight;

    public Purchase(String fn, String ln,Seat s, Flight f, boolean in, String price) {
        this.seat = s;
        this.flight = f;
        this.insurance = in;
        this.price = price;
        this.fName = fn;
        this.lName = ln;
    }

    public Seat getSeat() {
        return seat;
    }
    public Flight getFlight() {
        return flight;
    }
    
    public boolean getInsurance() {
        return insurance;
    }

    public String getPrice() {
        return price;
    }

    public String getFirstName() {
        return fName;
    }

    public String getLastName() {
        return lName;
    }
}
