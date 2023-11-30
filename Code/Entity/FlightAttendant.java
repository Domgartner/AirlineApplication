package Code.Entity;

import java.util.ArrayList;

import Code.Control.DatabaseController;

public class FlightAttendant extends User {

    public FlightAttendant(String fname, String lname, String email, String password) {
        super(fname, lname, email, password);
    }

    public static ArrayList<String> browsePassengersByFlight(String flightnum) {
        ArrayList<String> res = DatabaseController.browsePassengersByFlight(flightnum);
        return res;
    }

    @Override
    public void performStrategy() {
        strategy.showGUI();
    }



}
