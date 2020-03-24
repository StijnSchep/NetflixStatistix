package Business;

import Presentation.Commons.AccountPanel.AccountPanel;
import Presentation.Commons.MainPanel.MainPanel;
import Presentation.Commons.MoviePanel.MoviePanel;
import Presentation.Commons.SeriesPanel.SeriesPanel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelSwitcher {
    private Container container;

    /*
        The main frame of the application has a spot for one panel in the CENTER position, this class switches between
        different panels to change the screen of the user
     */

    //Container c is the container that the switching should apply to, in this case, c is the main frame's ContentPane
    public PanelSwitcher(Container c) { container = c; }


    //Given a value of the PanelValue enum, removes the current panel in the CENTER position and adds a new panel of the
    //given type. Possible values: MAIN, SERIES, FILM, ACCOUNT
    public void switchTo(PanelValue value) {

        //remove current panel if there is one, the first component added to the container is the footer, this is
        //index 0, the second and last component is, by default, a MainPanel on index 1
        //By removing whatever panel is on index 1 and adding a panel back to that index, the panels can be switched
        container.remove(1);

        //refresh screen
        container.repaint();

        //Add the desired panel. Panel is determined by the given PanelValue
        switch(value) {
            case MAIN:
                //MainPanel is the only panel that needs a PanelSwitcher as parameter, because it is the only panel
                //that functions as a screen selection panel
                container.add(new MainPanel(this));
                break;
            case SERIES:
                container.add(new SeriesPanel());
                break;
            case FILM:
                container.add(new MoviePanel());
                break;
            case ACCOUNT:
                container.add(new AccountPanel());
                break;
        }

        //update container to show newly added panel
        container.validate();
    }
}
