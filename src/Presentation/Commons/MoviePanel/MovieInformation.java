package Presentation.Commons.MoviePanel;

import Business.Listeners.MovieMouseListener;
import Domain.Movie;
import Presentation.Commons.CustomComponents.HeaderText;
import Presentation.Commons.CustomComponents.SubHeaderText;
import Application.Properties;

import javax.swing.*;
import java.awt.*;

public class MovieInformation extends JPanel {
    //Displays information from 1 movie in JPanel form

    private Movie movie;

    MovieInformation(Movie m) {
        this.movie = m;
        GridLayout layout = new GridLayout(2, 2);
        setLayout(layout);
        setBorder(BorderFactory.createLineBorder(Properties.BGCOLOR, 1));
        addMouseListener(new MovieMouseListener(this, movie));

        setMaximumSize(new Dimension(getMaximumSize().width, 100));

        //Uses the custom JLabels 'HeaderText' and 'SubHeaderText', which are JLabels with bigger fonts
        add(new HeaderText(movie.getTitleUpperCase()));
        add(new HeaderText(String.valueOf(movie.getPercentageWatched()), SwingConstants.RIGHT));
        add(new SubHeaderText(movie.getMovieInformation()));
        add(new SubHeaderText("Volledig bekeken door " +
                movie.getWatchAmount() + " / " + movie.getTotalWatchers() + " kijkers", SwingConstants.RIGHT));
    }

    //Used by the MovieMouseListener to display a tooltip with text "update '-title-'"
    public Movie getMovie() {
        return movie;
    }
}


