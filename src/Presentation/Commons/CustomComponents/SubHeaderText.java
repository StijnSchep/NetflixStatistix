package Presentation.Commons.CustomComponents;

import Application.Properties;

import javax.swing.*;
import java.awt.*;

public class SubHeaderText extends JLabel {
    //Custom JLabel to create headers, is a JLabel with bigger and bold font
    //Is a smaller version of HeaderText

    public SubHeaderText(String s) {
        this(s, SwingConstants.LEFT);
    }

    public SubHeaderText(String s, int alignment) {
        super(s);
        setFont(new Font(Properties.FONTFAMILY, Font.PLAIN, 20));
        setHorizontalAlignment(alignment);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }
}
