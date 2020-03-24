package Presentation.Commons.MainPanel;

import Business.PanelSwitcher;
import Application.Properties;
import Business.PanelValue;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    /*
        Extends JPanel, contains three big panels (InfoPanel's) that function as buttons. These panels allow the
        user to switch to SeriesPanel, MoviesPanel or AccountPanel
     */

    public MainPanel(PanelSwitcher switcher) {
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(1, 3, 5, 5));
        setLayout(new BorderLayout());
        setBackground(Properties.BGCOLOR);

        //The three panels are created and directly added to the MainPanel
        center.add(new InfoPanel("Serie Statistieken", "PrisonBreakPoster.jpg", switcher, PanelValue.SERIES));
        center.add(new InfoPanel("Film Statistieken", "AvengersPoster.jpg", switcher, PanelValue.FILM));
        center.add(new InfoPanel("Account Statistieken", "AccountIcon.jpg", switcher, PanelValue.ACCOUNT));
        center.setBackground(Properties.BGCOLOR);
        add(center);

        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }
}
