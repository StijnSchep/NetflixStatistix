package PopUps;

import Application.Properties;
import Data.Repositories.MovieRepository;
import Data.Repositories.SeriesRepository;
import Domain.Series;

import javax.swing.*;
import java.awt.*;

public class AddSeries implements Runnable {
    /*
        Creates a screen where the user can input data for a series, is used to both add new and update existing series
     */

    private JFrame frame;

    //If this variable is not null, the pop-up functions as an updated. If null, it will add the data as new series
    private Series series;

    public AddSeries() {}

    AddSeries(Series series) {
        this.series = series;
    }

    @Override
    public void run() {
        //Change the title of the frame based on if a series is being created or updated
        if(series == null) {
            frame = new JFrame("Voeg serie toe");
        } else {
            frame = new JFrame("Update '" + series.getTitle() + "'");
        }
        frame.setMinimumSize(new Dimension(500, 200));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        createComponents(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    //Creates a gridlayout with input fields and JComboBoxes for genres and age categories
    private void createComponents(Container container) {
        container.setLayout(new GridLayout(5, 2));

        JLabel title = new JLabel("  Titel: ");
        JTextField titleField = new JTextField();

        JLabel genre = new JLabel("  Genre: ");
        JComboBox<String> genreBox = new JComboBox<>(MovieRepository.getGenres());

        JLabel age = new JLabel("  Leeftijdscategorie: ");
        JComboBox<String> ageBox = new JComboBox<>(MovieRepository.getAgeCategories());

        JButton addButton;
        if(series == null) {
            //A new series is being added, so the button has the function of adding
            addButton = new JButton("voeg toe");
        } else {
            //An already existing series is being updated, so the button has the function of saving changes
            addButton = new JButton("opslaan");
        }
        addButton.setBackground(Properties.BUTTONCOLOR);
        addButton.addActionListener(ae -> {
            String sTitle = titleField.getText();

            //The title cannot be empty
            if(sTitle.equals("")) {
                titleField.setText("ONGELDIGE WAARDE");
                throw new IllegalArgumentException("Titel van een serie mag niet leeg zijn!");
            }

            String sGenre = (String) genreBox.getSelectedItem();
            String sAge = (String) ageBox.getSelectedItem();

            //Pass the data to the query
            if(series == null) {
                //a new series is being created, so use the method addSeriesToDatabase
                SeriesRepository.addSeriesToDatabase(sTitle, sGenre, sAge);
            } else {
                //An existing series is being edited, so use the method updateSeriesInDatabase
                SeriesRepository.updateSeriesInDatabase(series.getSeriesID(), sTitle, sGenre, sAge);
            }

            //Close the window
            frame.dispose();
        });

        //If the pop-up holds a series, it means that an existing series, with existing values, is being updated
        //The already existing values can be inserted into the input fields
        if(series != null) {
            titleField.setText(series.getTitle());
            genreBox.setSelectedItem(series.getGenre());
            ageBox.setSelectedItem(series.getAgeIndication());
        }

        container.add(title);
        container.add(titleField);
        container.add(genre);
        container.add(genreBox);
        container.add(age);
        container.add(ageBox);

        //Add some filler
        container.add(new JLabel(""));
        container.add(new JLabel(""));
        container.add(new JLabel(""));

        //Add the save / update button
        container.add(addButton);
    }
}