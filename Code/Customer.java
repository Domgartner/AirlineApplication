package Code;
import java.util.ArrayList;

public class Customer extends User {
    private CustomerGUI customerGUI;


    public Customer(String email, String password) {
        super(email, password);
        customerGUI = new CustomerGUI();
        customerGUI.display();
    }

    public static ArrayList<String> searchFlights(String departureDate, String flightNum, String flightDest) {
        ArrayList<String> res = DatabaseController.searchFlights(departureDate,flightNum,flightDest);
        return res;
    }


}
