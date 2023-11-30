package Code.Entity;

public class AirlineAgent extends User {

    public AirlineAgent(String fname, String lname, String email, String password) {
        super(fname, lname, email, password);
    }

    @Override
    public void performStrategy() {
        strategy.showGUI();
    }

    
}
