package Code.Entity;

public class Admin extends User{
    
    public Admin(String fName, String lName, String email, String password) {
        super(fName, lName, email, password);
    }

    @Override
    public void performStrategy() {
        strategy.showGUI();
    }

    
}
