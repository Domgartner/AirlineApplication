package Code.Strategy;

import Code.GUI.AdminGUI;

public class AdminStrategy implements Strategy {

    public void showGUI() {
        AdminGUI adminGUI = new AdminGUI();
        adminGUI.display();
    }
    
}
