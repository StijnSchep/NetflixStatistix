package Presentation.Commons.MainPanel;

import Business.Listeners.InfoPanelListener;
import Business.PanelSwitcher;
import Application.Properties;
import Business.PanelValue;

import javax.swing.*;
import java.awt.*;


public class InfoPanel extends JPanel {
    /*
        Extends JPanel, functions as a button that switches to a desired panel. InfoPanel has a background image and
        a title at the bottom
     */

    //The background image
    private Image img;

    //The title at the bottom
    private String title;

    //The PanelSwitcher that is used to switch to the desired panel
    private PanelSwitcher switcher;

    //The value that this InfoPanel should hold, this allows the PanelSwitcher to know where to switch to
    private PanelValue panelValue;

    InfoPanel(String title, String image, PanelSwitcher switcher, PanelValue panelValue) {
        setLayout(new BorderLayout());

        this.img = new ImageIcon("src/Presentation/images/" + image).getImage();
        this.title = title;
        this.switcher = switcher;
        this.panelValue = panelValue;

        //MouseListener creates hover effects and uses the switcher upon click
        addMouseListener(new InfoPanelListener(this));
    }

    //get panelValue, used by InfoPanelListener
    public PanelValue getPanelValue() {
        return panelValue;
    }

    //get the switcher, used by InfoPanelListener
    public PanelSwitcher getSwitcher() {
        return switcher;
    }

    //Actually creates the graphics for the panel, sets the desired image as background, adds rectangle to bottom with
    //the given title
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        g.setColor(Properties.MAINPANELRECTCOLOR);
        g.fillRect(0,getHeight() - 75, getWidth(),75);

        //Draw Title
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font font = new Font(Properties.FONTFAMILY, Font.BOLD, 40);
        g2d.setColor(Properties.MAINPANELTEXTCOLOR);
        g2d.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);
        int x = (getWidth() - metrics.stringWidth(title)) / 2;
        int y = getHeight()- 25;

        g2d.drawString(title, x,y);
    }


}
