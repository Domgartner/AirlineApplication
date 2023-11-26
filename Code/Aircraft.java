package Code;

import java.util.ArrayList;

public class Aircraft {
    private int aircraftID;
    private String aircraftType;
    private int numSeats;
    // private ArrayList<Seat> seats;


    public Aircraft(int id, String type, int num) {
        aircraftID = id;
        aircraftType = type;
        numSeats = num;
    }

    public int getAircaftID() {
        return aircraftID;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public int getNumSeats() {
        return numSeats;
    }
}
