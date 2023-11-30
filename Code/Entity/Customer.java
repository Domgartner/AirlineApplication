package Code.Entity;
import java.util.ArrayList;

import Code.Control.DatabaseController;

public class Customer extends User {

    public Customer(String fname, String lname, String email, String password) {
        super(fname, lname, email, password);
    }

    public static ArrayList<String> searchFlights(String departureDate, String flightNum, String flightDest, String start) {
        ArrayList<String> res = DatabaseController.searchFlights(departureDate,flightNum,flightDest, start);
        return res;
    }

    @Override
    public void performStrategy() {
        strategy.showGUI();
    }


}
