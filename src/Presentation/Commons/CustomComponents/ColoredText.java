package Presentation.Commons.CustomComponents;

import javax.swing.*;
import java.awt.*;

public class ColoredText extends JLabel {
    //used in menu bar and footer to create text with proper colors

    public ColoredText(String text, Color textColor, Color bgColor) {
        super(text);
        setForeground(textColor);
        setBackground(bgColor);
    }
}