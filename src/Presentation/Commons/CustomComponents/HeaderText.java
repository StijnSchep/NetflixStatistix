package Presentation.Commons.CustomComponents;

import Application.Properties;

import javax.swing.*;
import java.awt.*;

public class HeaderText extends JLabel {
    //Custom JLabel to create headers, is a JLabel with bigger and bold font

    public HeaderText(String s) {
        this(s, SwingConstants.LEFT);
    }

    public HeaderText(String s, int alignment) {
        super(s);
        setFont(new Font(Properties.FONTFAMILY, Font.BOLD, 30));
        setHorizontalAlignment(alignment);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }


}
