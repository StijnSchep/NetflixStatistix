package Presentation.Commons.AccountPanel;

import javax.swing.*;
import java.awt.*;

public class AccountPanel extends JPanel {
    /*
        Has two parts:
        1. options panel on BorderLayout's WEST position
        2. Information panel on BorderLayout's CENTER position

        AccOptionsPanel gets AccInformationPanel as parameter, so that the information can be updated based on chosen
        options
     */

    public AccountPanel() {
        setLayout(new BorderLayout());
        AccInformationPanel accInformation = new AccInformationPanel();
        add(accInformation);
        add(new AccOptionsPanel(accInformation), BorderLayout.WEST);
    }

}
