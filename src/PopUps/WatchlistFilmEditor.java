package PopUps;

import Application.Properties;
import Data.Repositories.AccountRepository;
import Domain.Movie;
import Domain.Profile;

import javax.swing.*;
import java.awt.*;

public class WatchlistFilmEditor implements Runnable {
    /*
        Pop-up to edit the progress for a given profile on a movie.

        Has two uses:
        1. When a user clicks on a movie in the AccountPanel, the user can edit the progress that the selected profile
        has mode on the movie.
        2. When a user adds a programme to a profile's watchlist, this pop-up allows the user
     */

    private JFrame frame;
    private Profile profile;

    //If movies is set, the pop-up updates the database instead of adding a new movie
    private Movie movie;

    WatchlistFilmEditor(Profile profile) {
        this.profile = profile;
    }

    public WatchlistFilmEditor(Profile profile, Movie movie) {
        this.profile = profile;
        this.movie = movie;
    }


    @Override
    public void run() {
        frame = new JFrame("Bekeken film percentage aanpassen");
        frame.setPreferredSize(new Dimension(400, 125));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Adds a JSpinner to selected the progress on the left and a JComboBox to select a movie on the right
    //If an existing progress is being updated, the JSpinner's default value will be set to the old progress and the
    //JComboBox will be replaced with a JLabel that states the movie for which the progress is being updated.
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Bekeken film toevoegen voor profiel '" + profile.getProfileName() + "'"),
                BorderLayout.NORTH);


        SpinnerNumberModel model;
        if(movie == null) {
            model = new SpinnerNumberModel(0, 0, 100, 1);
        } else {
            model = new SpinnerNumberModel(movie.getWatchAmount(), 0, 100, 1);
        }
        JSpinner progress = new JSpinner(model);
        container.add(progress);

        JPanel comboBoxHolder = new JPanel();
        comboBoxHolder.setLayout(new BorderLayout());
        comboBoxHolder.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        Movie[] unwatchedMovies = AccountRepository.getUnwatchedMovies(profile);
        JComboBox<Movie> movieOptions = new JComboBox<>(unwatchedMovies);
        comboBoxHolder.add(movieOptions);

        if(movie == null) {
            comboBoxHolder.add(movieOptions);
        } else {
            comboBoxHolder.add(new JLabel(movie.getTitle()));
        }
        container.add(comboBoxHolder, BorderLayout.EAST);

        JButton saveProgress = new JButton("Opslaan");
        saveProgress.setBackground(Properties.BUTTONCOLOR);
        saveProgress.addActionListener(ae -> {
            int selectedProgress = (Integer) progress.getValue();

            if(movie == null) {
                //add movie to the watchlist if the progress is bigger than 0%
                Movie selectedMovie = (Movie) movieOptions.getSelectedItem();
                if(selectedMovie != null && selectedProgress > 0) {
                    AccountRepository.addWatchedProgramme(profile.getProfileID(), selectedProgress, selectedMovie.getID());
                }
            } else {
                //update movie in the watchlist if the progress is not set to 0. If the progress is set to 0, the movie
                //will be removed from the watchlist instead of updated
                if(selectedProgress == 0) {
                    AccountRepository.removeWatchedProgramme(profile.getProfileID(), movie.getID());
                } else {
                    AccountRepository.updateWatchedProgramme(profile.getProfileID(), selectedProgress, movie.getID());
                }
            }

            frame.dispose();
        });
        container.add(saveProgress, BorderLayout.SOUTH);
    }
}
