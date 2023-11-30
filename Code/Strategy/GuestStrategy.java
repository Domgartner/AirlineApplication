package Code.Strategy;

import Code.GUI.GuestGUI;

public class GuestStrategy implements Strategy {

    public void showGUI() {
        GuestGUI Guest = new GuestGUI();
        Guest.display();
    }
    
}
