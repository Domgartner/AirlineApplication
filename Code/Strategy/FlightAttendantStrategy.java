package Code.Strategy;

import Code.GUI.FlightAttendantGUI;

public class FlightAttendantStrategy implements Strategy {
    
    public void showGUI() {
        FlightAttendantGUI FAGui = new FlightAttendantGUI();
        FAGui.setVisible(true);
    }
    
}