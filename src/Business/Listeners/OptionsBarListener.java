package Business.Listeners;

import PopUps.AddMovie;
import Presentation.Commons.MoviePanel.MoviePanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsBarListener extends MouseAdapter {
    /*
        Listens to clicks on MoviePanel's OptionsBar.
     */


    private JButton button;
    private JTextField input;
    private MoviePanel panel;

    public OptionsBarListener(MoviePanel panel, JButton button) {
        this(panel, button, null);
    }

    public OptionsBarListener(MoviePanel panel, JButton button, JTextField input) {
        this.panel = panel;
        this.button = button;
        this.input = input;
    }


    //Determines which button is press:
    //If an input is given, it's a filter and the list should be filtered
    //If the text is "Voeg film toe", a movie is being added
    //Last option is the MovieList should display the longest movie
    @Override
    public void mouseClicked(MouseEvent e) {
        if(input != null) {
            panel.changeMovieList(1, input.getText());
        } else if(button.getText().equals("Voeg film toe")) {
            AddMovie am = new AddMovie();
            SwingUtilities.invokeLater(am);
        } else {
            panel.changeMovieList(3, null);
        }
    }
}
