package Business.Listeners;

import Presentation.Commons.MainPanel.InfoPanel;
import Application.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoPanelListener extends MouseAdapter {
    /*
        Listeners for mouse activity on the given InfoPanel
        On hover, adds a border, on exit removes the border to let the user know that the panel is clickable
        On click uses the infoPanel's switcher and panelValue to change the panel in the main frame's CENTER position
     */

    //The panel that the listener listens to
    private InfoPanel panel;

    public InfoPanelListener(InfoPanel panel) {
        this.panel = panel;
    }

    //Hover is active, adds border and changes cursor
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setBorder(BorderFactory.createLineBorder(Properties.MAINCOLOR, 2));
    }

    //Hover is inactive, removes border and resets cursor to default
    public void mouseExited(java.awt.event.MouseEvent evt) {
        panel.setBorder(null);
        panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    //User clicked on the panel, uses the given PanelSwitcher to update the screen with the desired panel, which can
    //be found with the InfoPanel's PanelValue
    @Override
    public void mouseClicked(MouseEvent e) {
        panel.getSwitcher().switchTo(panel.getPanelValue());
    }
}