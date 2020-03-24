package Business.Listeners;

import Business.PanelSwitcher;
import Business.PanelValue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuBarListener extends MouseAdapter {
    /*
        Listens to the menu bar, on click uses the JMenu's PanelValue and switcher to switch to the appropriate panel
     */

    private PanelSwitcher switcher;
    private PanelValue panelValue;

    public MenuBarListener(PanelSwitcher switcher, PanelValue panelValue) {
        this.switcher = switcher;
        this.panelValue = panelValue;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switcher.switchTo(panelValue);
    }
}
