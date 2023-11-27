package Code;

import java.util.ArrayList;

public class FlightAttendant extends User {
    private FlightAttendantGUI attendantGUI;

    public FlightAttendant(String email, String password) {
        super(email, password);
        attendantGUI = new FlightAttendantGUI(this);
        attendantGUI.setVisible(true);
    }

    
    public static ArrayList<String> browsePassengersByFlight(String flightnum) {
        ArrayList<String> res = DatabaseController.browsePassengersByFlight(flightnum);
        return res;
    }



}
