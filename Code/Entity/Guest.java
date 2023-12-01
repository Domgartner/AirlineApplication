package Code.Entity;

public class Guest extends User{

    public Guest() {
        super(null, null, null, null);
    }

    @Override
    public void performStrategy() {
        strategy.showGUI();
    }
  
}
