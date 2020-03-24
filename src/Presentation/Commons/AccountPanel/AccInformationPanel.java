package Presentation.Commons.AccountPanel;

import Application.Properties;
import Business.Listeners.MovieMouseListener;
import Data.Repositories.AccountRepository;
import Domain.Account;
import Domain.Movie;
import Domain.Profile;
import Domain.Series;
import PopUps.WatchedEpisodesFrame;
import Presentation.Commons.CustomComponents.HeaderText;
import Presentation.Commons.CustomComponents.SubHeaderText;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class AccInformationPanel extends JPanel {
    /*
        Right side of AccountPanel, shows information about watched programmes and accounts based on the selected options
        in AccOptionsPanel. Can show watched movies, watched series and accounts with only 1 profile
     */


    //When displaying accounts with only one profile, this JPanel will hold that title
    private JPanel titleBar;
    //When displaying accounts with only one profile, this scrollpane will hold the acounts
    private JScrollPane accountScroller;

    //When displaying watched movies or series, this JPanel will hold information about the chosen profile
    private JPanel profileDisplayer;

    //When displaying watched movies or series, these scrollers show series or movie information
    private JScrollPane seriesScroller;
    private JScrollPane movieScroller;

    AccInformationPanel() {
        titleBar = new JPanel();
        profileDisplayer = new JPanel();
        accountScroller = new JScrollPane();
        seriesScroller = new JScrollPane();
        movieScroller = new JScrollPane();

        titleBar.add(new JLabel("Accounts met maar één profiel"));
        prepareScroller(accountScroller);
        prepareScroller(seriesScroller);
        prepareScroller(movieScroller);
        profileDisplayer.setLayout(new BorderLayout());

        setLayout(new BorderLayout());

        showSingleProfiles();
    }

    //Every scroller gets a BoxLayout, became a separate method to reduce code
    private void prepareScroller(JScrollPane pane) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        pane.getViewport().add(container);
    }

    //Show accounts with only one profile
    void showSingleProfiles() {
        //clear the information panel by removing all components from JPanel
        removeAll();

        //Get the most recent results from database
        updateAccountScroller();

        //Add relevant components to the information panel
        add(titleBar, BorderLayout.NORTH);
        add(accountScroller);

        repaint();
        validate();
    }

    //Show series that a person has watched.
    void showWatchedSeries(Profile p) {
        //clear the information panel by removing all components from JPanel
        removeAll();

        updateProfileDisplayer(p);
        updateSeriesScroller(p);

        add(profileDisplayer, BorderLayout.NORTH);
        add(seriesScroller);

        repaint();
        validate();
    }

    //Show the movies that a person has watched
    void showWatchedMovies(Profile p) {
        //clear the information panel by removing all components from JPanel
        removeAll();

        updateProfileDisplayer(p);
        updateMovieScroller(p);

        add(profileDisplayer, BorderLayout.NORTH);
        add(movieScroller);

        repaint();
        validate();
    }

    //Update the profileDisplayer to show the given profile
    //If the profileID is -1, it means that the screen is displaying info about ALL profiles from the account, String
    //should therefore not display info from the given profile if the ID is -1
    private void updateProfileDisplayer(Profile p) {
        profileDisplayer.removeAll();

        if(p.getProfileID() >= 0) {
            profileDisplayer.add(new SubHeaderText(p.getProfileName() + ", geboren op " + p.getDateOfBirth()));
        } else if(p.getProfileID() == -1) {
            profileDisplayer.add(new SubHeaderText("Dit is wat de profielen van het geselecteerde account hebben bekeken"));
        }
    }

    //AccountScroller shows profiles with 1 profile
    private void updateAccountScroller() {
        JPanel container = (JPanel) accountScroller.getViewport().getComponent(0);
        container.removeAll();

        Account[] accounts = AccountRepository.getAccountsWithOneProfile();
        for(Account a : accounts) {
            JPanel accountHolder = new JPanel();
            accountHolder.setLayout(new BorderLayout());

            accountHolder.add(new HeaderText(a.getName()));
            accountHolder.add(new SubHeaderText(a.getStreet() + " " + a.getHouseNumber() + ", " + a.getPlace()),
                    BorderLayout.SOUTH);

            accountHolder.setBorder(BorderFactory.createLineBorder(Properties.BGCOLOR));
            container.add(accountHolder);
        }
    }

    //SeriesScroller shows the series that the given profile has watched and the progress on that series
    private void updateSeriesScroller(Profile p) {
        JPanel container = (JPanel) seriesScroller.getViewport().getComponent(0);
        container.removeAll();

        Series[] series = AccountRepository.getWatchedSeries(p);
        for(Series s : series) {
            JPanel seriesInformation = new JPanel();
            seriesInformation.setLayout(new BorderLayout());
            seriesInformation.setBorder(BorderFactory.createLineBorder(Properties.BGCOLOR));
            seriesInformation.setMaximumSize(new Dimension(seriesInformation.getMaximumSize().width, 100));

            seriesInformation.add(new HeaderText(s.getTitle()));
            seriesInformation.add(new SubHeaderText(s.getGenre() + " - " + s.getAgeIndication()), BorderLayout.SOUTH);
            seriesInformation.add(new SubHeaderText("Voor " + s.getAverageWatchTime() + "% bekeken"), BorderLayout.EAST);
            container.add(seriesInformation);

            //If the profileID is -1, it means that the series are based on an account as a whole. It shouldn't be possible
            //to click on a series if no specific profile is selected
            if(p.getProfileID() != -1) {
                seriesInformation.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        WatchedEpisodesFrame wef = new WatchedEpisodesFrame(s, p);
                        SwingUtilities.invokeLater(wef);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        seriesInformation.setBackground(Properties.HOVERCOLOR);
                        seriesInformation.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        seriesInformation.setToolTipText("Bekijk bekeken aflevering van '" + s.getTitle() + "'");
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        seriesInformation.setBackground(Color.WHITE);
                        seriesInformation.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        seriesInformation.setToolTipText("");
                    }
                });
            }
        }
        
        if(series.length == 0) {
            container.add(noProgrammesWatched(p));
        }
    }

    //If a profile has not watched any movies or any series, this panel will be shown instead of a list
    private JPanel noProgrammesWatched(Profile p) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setMaximumSize(new Dimension(container.getMaximumSize().width, 100));

        if(p.getProfileID() >= 0) {
            container.add(new HeaderText(p.getProfileName() + " heeft nog niets bekeken!"));
        } else {
            container.add(new HeaderText("De profielen van dit account hebben nog niets bekeken!"));
        }

        return container;
    }

    //MovieScroller shows the movies that the given profile has watched
    private void updateMovieScroller(Profile p) {
        JPanel container = (JPanel) movieScroller.getViewport().getComponent(0);
        container.removeAll();

        Movie[] movies = AccountRepository.getWatchedMovies(p);
        for(Movie m : movies) {
            JPanel movieInformation = new JPanel();
            movieInformation.setLayout(new BorderLayout());
            movieInformation.setBorder(BorderFactory.createLineBorder(Properties.BGCOLOR));
            movieInformation.setMaximumSize(new Dimension(movieInformation.getMaximumSize().width, 100));

            movieInformation.add(new HeaderText(m.getTitleUpperCase()));
            movieInformation.add(new SubHeaderText(m.getReleaseYear() + " - " + m.getLanguage() + " - " +
                    m.getGenre() + " - " + m.getAgeIndication()), BorderLayout.SOUTH);
            movieInformation.add(new SubHeaderText("Film is " + m.getWatchAmount() + "% bekeken"), BorderLayout.EAST);

            //If the profileID is -1, it means that the movies are based on an account as a whole. It shouldn't be possible
            //to click on a movie if no specific profile is selected
            if(p.getProfileID() != -1) {
                movieInformation.addMouseListener(new MovieMouseListener(m, movieInformation));
            }
            container.add(movieInformation);
        }

        if(movies.length == 0) {
            container.add(noProgrammesWatched(p));
        }
    }



}
