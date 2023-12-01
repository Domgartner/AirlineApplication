package Code.Strategy;

import Code.GUI.AAGUI;

public class AAstrategy implements Strategy{

    public void showGUI() {
        AAGUI aagui = new AAGUI();
        aagui.display();
    }
    
}
