package Presentation.Commons.CustomComponents;

import Application.Properties;

import javax.swing.*;
import java.awt.*;

public class Footer extends JPanel {
    /*
        Extends JPanel, shows static information about the creators of the application.
     */

    public Footer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        ColoredText NS = new ColoredText("Netflix Statistix", Properties.MAINTTEXTCOLOR, Properties.MAINCOLOR);

        String infoText = "Informatica jaar 1 | 1D | Niels de Hoon (2122836) / Tessa Koppens (2145434) / Stijn Schep (2142553)";
        ColoredText info = new ColoredText(infoText, Properties.MAINTTEXTCOLOR, Properties.MAINCOLOR);

        add(NS, BorderLayout.WEST);
        add(info, BorderLayout.EAST);
    }

    //Creates gradient color based on values in Properties class
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0,0, Properties.MAINCOLOR, 0, h + 20, Properties.GRADIENTCOLOR);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
