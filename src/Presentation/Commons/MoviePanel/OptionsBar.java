package Presentation.Commons.MoviePanel;

import Business.Listeners.OptionsBarListener;
import Application.Properties;

import javax.swing.*;
import java.awt.*;

public class OptionsBar extends JPanel {
    /*
        Extends JPanel, creates a panel with:
        - Button to get the longest movie for people under 16 years old {left side}
        - Button to add a movie {left side)
        - input field and filter button to filter the list of movies {right side)
     */

    OptionsBar(MoviePanel panel) {
        setLayout(new BorderLayout());

        //Add buttons and textfield to correct side
        createLeftSide(panel);
        createRightSide(panel);
    }

    //Creates a JPanel with two buttons to view longest movie and to add a movie
    private void createLeftSide(MoviePanel p) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0,0,0,0));

        JButton longestMovie = new JButton("Langste film voor onder 16 jaar");
        JButton addMovie = new JButton("Voeg film toe");

        longestMovie.addMouseListener(new OptionsBarListener(p, longestMovie));
        addMovie.addMouseListener(new OptionsBarListener(p, addMovie));

        longestMovie.setBackground(Properties.BUTTONCOLOR);
        addMovie.setBackground(Properties.BUTTONCOLOR);

        panel.add(longestMovie);
        panel.add(addMovie);

        add(panel, BorderLayout.WEST);
    }

    //Creates a JPanel with an input field and a filter button to filter the list of movies with the value in the input field
    private void createRightSide(MoviePanel p) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0,0,0,0));

        JTextField textInput = new JTextField();
        JButton filter = new JButton("Filter");
        filter.addMouseListener(new OptionsBarListener(p, filter, textInput));

        textInput.setColumns(20);
        filter.setBackground(Properties.BUTTONCOLOR);


        panel.add(textInput);
        panel.add(filter);
        add(panel, BorderLayout.EAST);
    }

    //Add a gradient color to the options bar
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0,0, Properties.MAINCOLOR, 0, h + 50, Properties.BGCOLOR);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
