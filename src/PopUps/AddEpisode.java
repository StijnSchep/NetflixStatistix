package PopUps;

import Application.Properties;
import Data.Repositories.EpisodeRepository;
import Domain.Episode;
import Domain.Series;

import javax.swing.*;
import java.awt.*;

public class AddEpisode implements Runnable {
    /*
        Creates a pop-up with input fields for information for an episode
        Can be used to create a new or update an existing episode
     */

    private JFrame frame;

    //If this variable is set, an existing episode is being updated
    private Episode episode;

    //The series that the episode will be added to
    private Series series;

    public AddEpisode(Series series) {
        this(series, null);
    }

    AddEpisode(Series series, Episode episode) {
        this.series = series;
        this.episode = episode;
    }

    @Override
    public void run() {
        if(episode == null) {
            //No existing episode is given, so the frame has the function of adding a new episode
            frame = new JFrame("Voeg aflevering toe");
        } else {
            //An existing episode is given, so the frame has the function of updating that episode
            frame = new JFrame("Update '" + episode.getTitle() + "'");
        }
        frame.setPreferredSize(new Dimension());
        frame.setMinimumSize(new Dimension(500, 280));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Creates a gridlayout with input fields, if no series is given, no fields are added and the user gets a message
    //that tells them to first select a series
    private void createComponents(Container container) {
        container.setLayout(new GridLayout(7, 2));

        if(series == null) {
            container.setLayout(new BorderLayout());
            container.add(new JLabel("SELECTEER EERST EEN SERIE OM EEN AFLEVERING AAN TOE TE VOEGEN"));
            return;
        }

        JLabel selectedSeries = new JLabel("  Geselecteerde serie:  ");
        JLabel seriesTitle = new JLabel(series.getTitle());

        JLabel title = new JLabel("  Titel: ");
        JTextField titleField = new JTextField();

        JLabel episodeLabel = new JLabel("  Afleveringnummer: ");
        //Create a spinner to select the episode, range is set from 1 to 300
        SpinnerNumberModel episodeModel = new SpinnerNumberModel(1, 1, 300, 1);
        JSpinner episodeField = new JSpinner(episodeModel);

        JLabel season = new JLabel("  Seizoen: ");
        //Create a spinner to select the season, range is set from 1 to 200
        SpinnerNumberModel seasonModel = new SpinnerNumberModel(1, 1, 200, 1);
        JSpinner seasonField = new JSpinner(seasonModel);

        JLabel length = new JLabel("  Duur in minuten: ");
        JTextField lengthField = new JTextField();


        JButton addButton;

        if(episode == null) {
            //No existing episode is given, so the function of the button is to add
            addButton = new JButton("voeg toe");
        } else {
            //An existing episode is given, so the function of the button is to save changes
            addButton = new JButton("opslaan");
        }
        addButton.setBackground(Properties.BUTTONCOLOR);
        addButton.addActionListener(ae -> {
            try {
                String sTitle = titleField.getText();

                //Title cannot be empty, throw IllegalArgumentException if it is
                if(sTitle.equals("")) {
                    titleField.setText("ONGELDIGE WAARDE");
                    throw new IllegalArgumentException("Titel van een aflevering mag niet leeg zijn!");
                }

                int duration = Integer.parseInt(lengthField.getText());
                //The duration has to be 1 minute or longer, throw IllegalArgumentException if it's not
                if(duration < 1) {
                    lengthField.setText("ONGELDIGE WAARDE");
                    throw new IllegalArgumentException("an episode cannot have a duration of 0 or less");
                }

                int episodeNumber = (Integer) episodeField.getValue();
                int seasonNumber = (Integer) seasonField.getValue();

                //String representation of the episode number in the form of S--E--
                String episodeNumberString = "S";
                if(seasonNumber < 10) { episodeNumberString += "0" + seasonNumber; }
                else { episodeNumberString += seasonNumber; }

                episodeNumberString += "E";
                if(episodeNumber < 10) { episodeNumberString += "0" + episodeNumber;}
                else { episodeNumberString += episodeNumber; }

                if(episode == null) {
                    //No existing episode is given, so a new episode is created for the given series
                    EpisodeRepository.addEpisodeToDatabase(series, episodeNumberString, duration, sTitle);
                } else {
                    //An existing episode is given, so that episode will be updated with the given data
                    EpisodeRepository.updateEpisodeInDatabase(episode.getID(), episodeNumberString, sTitle, duration);
                }

                frame.dispose();
            } catch(NullPointerException e) {
                lengthField.setText("ONGELDIGE WAARDE");
            }
        });

        //If the pop-up has an existing episode, fill in the fields with the existing values
        if(episode != null) {
            String episodeNumberString = episode.getEpisodeNumber();
            String[] parts = episodeNumberString.split("E");
            //parts[0] holds season number, parts[1] holds episode number
            int seasonNumber = Integer.parseInt(parts[0].replace("S", ""));
            int episodeNumber = Integer.parseInt(parts[1]);
            int duration = episode.getDuration();

            titleField.setText(episode.getTitle());
            episodeField.setValue(episodeNumber);
            seasonField.setValue(seasonNumber);
            lengthField.setText(String.valueOf(duration));
        }

        container.add(selectedSeries);
        container.add(seriesTitle);
        container.add(title);
        container.add(titleField);
        container.add(season);
        container.add(seasonField);
        container.add(episodeLabel);
        container.add(episodeField);
        container.add(length);
        container.add(lengthField);

        container.add(new JLabel(""));
        container.add(new JLabel(""));
        container.add(new JLabel(""));
        container.add(addButton);
    }
}