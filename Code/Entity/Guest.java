package Code.Entity;

import Code.GUI.CustomerGUI;

public class Guest extends User{
    private CustomerGUI guestGUI;

    public Guest() {
        super(null, null, null, null);
        guestGUI = new CustomerGUI();
        guestGUI.setVisible(true);
    }

    @Override
    public void performStrategy() {
        strategy.showGUI();
    }


    
}
