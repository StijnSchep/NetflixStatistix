package Application;

import java.awt.*;

public class Properties {
    /* This class holds properties that are used throughout the application */

    /* ---- constants for application size ---- */

    // Used for the application frame width and height. DEFAULT: new Dimension(1500, 810)
    public final static Dimension APPSIZE = new Dimension(1500, 810);

    /* ---- constants for color scheme ---- */

    // Color for menu, footer and other menu items.         DEFAULT: Color.decode("#E50914")
    public final static Color MAINCOLOR = Color.decode("#E50914");

    // Lighter version of main color, used for hover effect DEFAULT: Color.decode("#fdced0")
    public final static Color HOVERCOLOR = Color.decode("#fdced0");

    // Used for creating a gradient with the main color, default is equal to BGCOLOR
    public final static Color GRADIENTCOLOR = Color.decode("#141414");

    // Used for text that goes on top of main color, should contrast main color well. DEFAULT: Color.WHITE
    public final static Color MAINTTEXTCOLOR = Color.WHITE;

    // Background color, should contrast main color well.   DEFAULT: Color.decode("#141414")
    public final static Color BGCOLOR = Color.decode("#141414");

    // Used for button colors
    public final static Color BUTTONCOLOR = Color.WHITE;


    /* ---- constants for main panel ---- */

    // Used for the color of the rectangles on top of main panel images
    // Should contrast well with MAINPANELTEXTCOLOR. DEFAULT: new Color(0,0,0, 200)
    public final static Color MAINPANELRECTCOLOR = new Color(0,0,0, 200);

    // Used for the text on top of main panel images
    // Should contrast well with MAINPANELRECTCOLOR. DEFAULT: new Color(255,255,255,150)
    public final static Color MAINPANELTEXTCOLOR = new Color(255,255,255,150);



    /* ---- text constants ---- */

    // Font-family used in the application. DEFAULT: "sans-serif"
    public final static String FONTFAMILY = "sans-serif";
}
