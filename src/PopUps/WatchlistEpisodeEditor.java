package PopUps;

import Application.Properties;
import Data.Repositories.AccountRepository;
import Data.Repositories.SeriesRepository;
import Domain.Episode;
import Domain.Profile;
import Domain.Series;

import javax.swing.*;
import java.awt.*;

public class WatchlistEpisodeEditor implements Runnable {
    private Profile profile;
    private JFrame frame;

    //This pop-up can either add a new episode to the watchlist, or update the existing progress
    //For updating, these variables are used
    private Episode givenEpisode;    //The episode from which the progress is being editted
    private Series givenSeries;      //The series that the episode belongs to

    WatchlistEpisodeEditor(Profile profile) {
        this(profile, null, null);
    }

    WatchlistEpisodeEditor(Profile profile, Series series, Episode episode) {
        this.profile = profile;
        this.givenSeries = series;
        this.givenEpisode = episode;
    }


    @Override
    public void run() {
        frame = new JFrame("Bekeken aflevering percentage aanpassen");
        frame.setPreferredSize(new Dimension(600, 150));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Creates a JSpinner to select progress and two JComboBoxes. One JComboBox is for selecting the series, the second
    //JComboBox shows episodes from that series that have not yet been watched by the profile
    //If an existing progress is being updated, these combo boxes will be replaced by JLabels that show the series and episode
    //On update, the JSpinner's lower limit will be set to the old progress
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        if(givenSeries == null && givenEpisode == null) {
            container.add(new JLabel("Bekeken aflevering toevoegen voor profiel '" + profile.getProfileName() + "'"),
                    BorderLayout.NORTH);
        } else {
            container.add(new JLabel("Bekeken aflevering aanpassen voor profiel '" + profile.getProfileName() + "'"),
                    BorderLayout.NORTH);
        }

        SpinnerNumberModel model;
        if(givenSeries == null && givenEpisode == null){
            model = new SpinnerNumberModel(0, 0, 100, 1);
        } else {
            //An existing episode's progress is being editted, the existing value can be used in the model to set a
            //base value. The value can still be set lower with a minimum of 0. if progress is set to 0, the episode will
            //not be linked to the profile anymore
            int prog = givenEpisode.getPercentageWatched();
            model = new SpinnerNumberModel(prog, 0, 100, 1);
        }
        JSpinner progress = new JSpinner(model);
        container.add(progress);

        JPanel comboBoxHolder = new JPanel();
        comboBoxHolder.setLayout(new BorderLayout());
        comboBoxHolder.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

        Series[] series = SeriesRepository.getAllSeries();

        //Quickly check if the database has any series in it to begin with, if no series are present, the pop-up is
        //pointless
        if(series.length == 0) {
            container.removeAll();
            container.add(new JLabel("De database bevat geen series om te bekijken!"));
            return;
        }

        JComboBox<Episode> episodeBox = new JComboBox<>(AccountRepository.getUnwatchedEpisodes(profile, series[0]));
        JComboBox<Series> seriesBox = new JComboBox<>(SeriesRepository.getAllSeries());
        seriesBox.addActionListener(ae -> {
            Series selected = (Series) seriesBox.getSelectedItem();
            if(selected != null) {
                episodeBox.setModel(new DefaultComboBoxModel<>(AccountRepository.getUnwatchedEpisodes(profile, selected)));
            }
        });

        if(givenSeries == null && givenEpisode == null) {
            //Pop-up is meant to add a new episode to the watchlist, so the user needs to be able to select episodes
            comboBoxHolder.add(seriesBox, BorderLayout.NORTH);
            comboBoxHolder.add(episodeBox, BorderLayout.SOUTH);
        } else {
            //Pop-up is meant to update the progress on an existing episode in the watchlist, so the user should not
            //be able to select any other episode or series, comboboxes will not be added
            comboBoxHolder.add(new JLabel(givenSeries.getTitle()), BorderLayout.NORTH);
            comboBoxHolder.add(new JLabel(givenEpisode.getTitle()), BorderLayout.SOUTH);
        }

        container.add(comboBoxHolder, BorderLayout.EAST);

        JButton saveButton = new JButton("Opslaan");
        saveButton.setBackground(Properties.BUTTONCOLOR);
        saveButton.addActionListener(ae -> {
            int sProgress = (Integer) progress.getValue();

            if(givenSeries == null && givenEpisode == null) {
                //Pop-up needs to add a new episode to the watchlist, so the user needs to have selected an episode
                //before an action can be performed
                Episode selected = (Episode) episodeBox.getSelectedItem();

                //If progress is 0, the episode will not be added to the watchlist
                if (selected != null) {
                    if(sProgress > 0) {
                        AccountRepository.addWatchedProgramme(profile.getProfileID(), sProgress, selected.getID());
                    }
                    frame.dispose();
                }
            } else {
                //Pop-up is updating an existing episode in the watchlist. If the progress is set to 0, the episode will
                //be removed from the watchlist instead of updated.
                if(sProgress == 0) {
                    AccountRepository.removeWatchedProgramme(profile.getProfileID(), givenEpisode.getID());
                } else {
                    AccountRepository.updateWatchedProgramme(profile.getProfileID(), sProgress, givenEpisode.getID());
                }
                frame.dispose();
            }
        });
        container.add(saveButton, BorderLayout.SOUTH);




    }
}
