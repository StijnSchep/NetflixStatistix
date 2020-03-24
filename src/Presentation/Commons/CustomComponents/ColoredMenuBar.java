package Presentation.Commons.CustomComponents;

import Business.Listeners.MenuBarListener;
import Business.PanelSwitcher;
import Application.Properties;
import Business.PanelValue;

import javax.swing.*;
import java.awt.*;

public class ColoredMenuBar extends JMenuBar {
    /*
        Extends JMenuBar to create a menu bar with a desired colour and desired mouseListener on the different
        menu items.

        ColoredMenuBar has buttons for the home screen, series panel, movies panel and account info panel
        Each button has a MenuBarListener, which listens for a click on the button. Together with MainPanel,
        ColoredMenuBar gives the user the possibility to switch between screens.
     */

    public ColoredMenuBar(PanelSwitcher switcher) {

        JMenu menu = new JMenu("Home");
        menu.setForeground(Properties.MAINTTEXTCOLOR);
        menu.addMouseListener(new MenuBarListener(switcher, PanelValue.MAIN));

        JMenu serie = new JMenu("Serie Statistieken");
        serie.setForeground(Properties.MAINTTEXTCOLOR);
        serie.addMouseListener(new MenuBarListener(switcher, PanelValue.SERIES));

        JMenu film = new JMenu("Film Statistieken");
        film.setForeground(Properties.MAINTTEXTCOLOR);
        film.addMouseListener(new MenuBarListener(switcher, PanelValue.FILM));

        JMenu account = new JMenu("Account Statistieken");
        account.setForeground(Properties.MAINTTEXTCOLOR);
        account.addMouseListener(new MenuBarListener(switcher, PanelValue.ACCOUNT));

        add(menu);
        add(serie);
        add(film);
        add(account);
    }

    //Changes the color of the menu bar to a gradient color based on values in the Properties class
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, Properties.MAINCOLOR, 0, h + 10, Properties.GRADIENTCOLOR);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
