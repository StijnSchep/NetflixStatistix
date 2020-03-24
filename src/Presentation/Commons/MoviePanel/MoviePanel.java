package Presentation.Commons.MoviePanel;

import Application.Properties;

import javax.swing.*;
import java.awt.*;

public class MoviePanel extends JPanel {
    /*
        Creates a panel to view all movies in the database
        Code is split between 4 classes in the MoviePanel package
        This class handles the logic of changing the list of movies. The list can be "all movies", "all movies on filter"
        or "the longest movie for people under 16 years".

        MoviePanel has two parts: options bar at the BorderLayout's NORTH position and a JScrollPane in the CENTER position
        the options bar is held by the OptionsBar class. The MovieList class is a container for MovieInformation panels.
        Each MovieInformation panel holds information for 1 movie
     */

    public MoviePanel() {
        setLayout(new BorderLayout());
        setBackground(Properties.BGCOLOR);

        //Add the options bar at the top of the panel
        add(new OptionsBar(this), BorderLayout.NORTH);

        addMovieList();
    }

    //Adds the MovieList to the CENTER position
    private void addMovieList() {
        //Add the JScrollPane to scroll though the given movie list
        JScrollPane movieListScroller = new JScrollPane();
        movieListScroller.setBorder(null);
        movieListScroller.getViewport().add(new MovieList());

        add(movieListScroller);
    }

    //The MovieList in the CENTER position can be changed based on the listNumber
    //1: create a list of movies based on a filter
    //2: show all movies
    //3: Show longest movie for people under 16 years
    public void changeMovieList(int listNumber, String input) {
        // Listnumber: 1 = filtered; 2 = unfiltered, all movies, 3 = get longest movie for under 16 years
        remove(1);
        repaint();

        JScrollPane movieListScroller = new JScrollPane();
        movieListScroller.setBorder(null);

        switch(listNumber) {
            case 1:
                movieListScroller.getViewport().add(new MovieList(input));
                break;
            case 2:
                movieListScroller.getViewport().add(new MovieList());
                break;
            case 3:
                movieListScroller.getViewport().add(new MovieList(true));
                break;
        }

        add(movieListScroller);
        repaint();
        validate();
    }
}
