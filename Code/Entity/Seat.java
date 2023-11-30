package Code.Entity;

public class Seat {
    private String seatNum;
    private boolean available;
    private String type;
    private double price;

    public Seat(String num, boolean available, String t, double p) {
        seatNum = num;
        this.available = available;
        type = t;
        price = p;
    }

    public boolean getAvailable() {
        return available;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public String getSeatType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

}
