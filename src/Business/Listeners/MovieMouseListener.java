package Business.Listeners;

import Domain.Movie;
import Domain.Profile;
import Presentation.Commons.AccountPanel.AccOptionsPanel;
import Presentation.Commons.MoviePanel.MovieInformation;
import Application.Properties;
import PopUps.DeleteOrUpdateMovie;
import PopUps.WatchlistFilmEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MovieMouseListener extends MouseAdapter {
    /*
        Has two uses:
        1. listens to a movie's panel in MoviePanel. Allows users to click and update/delete a movie
        2. listens to a movie's panel in AccInformationPanel. Allows users to update the progress that a profile has
        made on the movie
     */

    //For use 1:
    private MovieInformation movie;

    //For use 2:
    private Movie selectedMovie; //The movie for which the progress will be edited
    private JPanel movieHolder;  //The panel that holds the user information, given to listener to add hover effect

    public MovieMouseListener(MovieInformation m, Movie selectedMovie) {
        movie = m;
        this.selectedMovie = selectedMovie;
    }

    public MovieMouseListener(Movie m, JPanel movieHolder) {
        selectedMovie = m;
        this.movieHolder = movieHolder;
    }

    //Add hover effect to the panel that the mouse is hovering over
    @Override
    public void mouseEntered(MouseEvent e) {
        if(movie != null) {
            movie.setCursor(new Cursor(Cursor.HAND_CURSOR));
            movie.setBackground(Properties.HOVERCOLOR);
            movie.setToolTipText("Update Or delete '" + movie.getMovie().getTitle() + "'");
        } else {
            movieHolder.setCursor(new Cursor(Cursor.HAND_CURSOR));
            movieHolder.setBackground(Properties.HOVERCOLOR);
            movieHolder.setToolTipText("Kijkpercentage van  '" + selectedMovie.getTitle() + "' aanpassen");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(movie != null) {
            //MovieInformation is given, so this listener applies to MoviesPanel. When a user clicks on a movie in
            //MoviesPanel, the user should get the choice of deleting or updating the movie, which is done through
            //DeleteOrUpdateMovie pop-up
            DeleteOrUpdateMovie choicePopup = new DeleteOrUpdateMovie(selectedMovie);
            SwingUtilities.invokeLater(choicePopup);
        } else {
            //User clicked on a movie's JPanel in AccountPanel
            //Open a pop-up to edit the progress of the selected profile with the given movie
            Profile selectedProfile = (Profile) AccOptionsPanel.profiles.getSelectedItem();
            WatchlistFilmEditor wfe = new WatchlistFilmEditor(selectedProfile, selectedMovie);
            SwingUtilities.invokeLater(wfe);
        }
    }

    //Remove hover effect
    @Override
    public void mouseExited(MouseEvent e) {
        if(movie != null) {
            movie.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            movie.setBackground(Color.WHITE);
            movie.setToolTipText("");
        } else {
            movieHolder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            movieHolder.setBackground(Color.WHITE);
            movieHolder.setToolTipText("");
        }
    }
}
