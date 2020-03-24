package Presentation;

import Business.PanelSwitcher;
import Presentation.Commons.CustomComponents.ColoredMenuBar;
import Presentation.Commons.CustomComponents.Footer;
import Presentation.Commons.MainPanel.MainPanel;
import Application.Properties;

import javax.swing.*;
import java.awt.*;

public class UserInterface implements Runnable {
    /*
        Creates the main frame of the application
        The frame has a menuBar (ColoredMenuBar, Presentation > Commons > CustomComponents) to switch panels
        The frame has a footer (Footer, Presentation > Commons > CustomComponents) to show static information
        The footer is set on the borderlayout's SOUTH position, the panel in the CENTER position changes to show
        different overviews
     */

    private JFrame frame;

    //Class PanelSwitcher handles the switching of the panel in the CENTER position
    private PanelSwitcher panelSwitcher;

    //Start the creation of the frame
    @Override
    public void run() {
        frame = new JFrame("Netflix Statistix");
        frame.setPreferredSize(Properties.APPSIZE);
        frame.setMinimumSize(Properties.APPSIZE);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setIconImage();
        createComponents(frame.getContentPane());

        //Add the ColoredMenuBar to the frame. A PanelSwitcher is given to the menu bar to switch between panels in the
        //CENTER position
        frame.setJMenuBar(new ColoredMenuBar(panelSwitcher));

        frame.pack();
        frame.setVisible(true);
    }

    //Set the application icon
    private void setIconImage() {
        ImageIcon img = new ImageIcon("src/Presentation/images/NetflixLogo.png");

        frame.setIconImage(img.getImage());
    }


    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        container.add(new Footer(), BorderLayout.SOUTH);


        //By default, the panel in the CENTER position is a Mainpanel
        panelSwitcher = new PanelSwitcher(container);
        container.add(new MainPanel(panelSwitcher));
    }

}
