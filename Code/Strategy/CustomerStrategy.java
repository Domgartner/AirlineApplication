package Code.Strategy;

import Code.GUI.CustomerGUI;

public class CustomerStrategy implements Strategy{
    

    public void showGUI() {
        CustomerGUI customerGUI = new CustomerGUI();
        customerGUI.display();
    }
}
