package PopUps;

import Application.Properties;
import Data.Repositories.MovieRepository;
import Domain.Movie;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class AddMovie implements Runnable {
    /*
        Has two uses:
        - Add a new movie to the database
        - Update an existing database
     */

    private JFrame frame;

    //If movie is not null, an existing movie is being updated
    private Movie movie;

    public AddMovie() {}

    AddMovie(Movie m) {
        this.movie = m;
    }

    @Override
    public void run() {
        if(movie == null) {
            frame = new JFrame("Voeg film toe");
        } else {
            frame = new JFrame("Film '" + movie.getTitle() + "' aanpassen");
        }
        frame.setMinimumSize(new Dimension(500, 280));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Create a GridLayout with input fields
    //If a movie is being updated, input fields will be filled with existing values
    private void createComponents(Container container) {
        GridLayout layout = new GridLayout(9, 2);
        container.setLayout(layout);

        JLabel title = new JLabel("  Titel: ");
        JTextField titleField = new JTextField();

        JLabel genre = new JLabel("  Genre: ");
        JComboBox<String> genreBox = new JComboBox<>(MovieRepository.getGenres());

        JLabel age = new JLabel("  Leeftijd: ");
        JComboBox<String> ageBox = new JComboBox<>(MovieRepository.getAgeCategories());

        JLabel length = new JLabel("  Duur: ");
        JTextField lengthField = new JTextField();

        JLabel language = new JLabel("  Taal: ");
        JComboBox<String> languageField = new JComboBox<>(MovieRepository.getLanguages());

        JLabel year = new JLabel("  Jaar: ");
        //get the current year
        int cy = Calendar.getInstance().get(Calendar.YEAR);
        //create a new model, default selected value is current year, earliest possible year to choose is
        //current year - 500, maximum value is current year
        SpinnerNumberModel yearModel = new SpinnerNumberModel(cy,cy-500, cy, 1);
        JSpinner yearSpinner = new JSpinner(yearModel);

        JButton addButton;
        if (movie == null) {
            addButton = new JButton("voeg toe");
        } else {
            addButton = new JButton("opslaan");
        }
        addButton.setBackground(Properties.BUTTONCOLOR);
        addButton.addActionListener(ae -> {
            try {
                //int duration, String title, String genre, String language, int year, String ageIndication
                String durationString = lengthField.getText();
                int duration = Integer.parseInt(durationString.replace(".", ""));
                if(duration < 1) {
                    lengthField.setText("ONGELDIGE WAARDE");
                    throw new IllegalArgumentException("Lengte van de film moet een positieve waarde zijn");
                }

                String sTitle = titleField.getText();
                if(titleField.equals("")) {
                    titleField.setText("ONGELDIGE WAARDE");
                    throw new IllegalArgumentException("Titel mag niet leeg zijn");
                }

                String sGenre = (String) genreBox.getSelectedItem();
                String sLanguage = (String) languageField.getSelectedItem();
                int sYear = (Integer) yearSpinner.getValue();
                String ageIndication = (String) ageBox.getSelectedItem();

                if(movie == null) {
                    //create new movie
                    MovieRepository.addMovieToDatabase(duration, sTitle, sGenre, sLanguage, sYear, ageIndication);
                    frame.dispose();
                } else {
                    //update existing movie
                    MovieRepository.updateMovieInDatabase(movie.getID(), duration, sTitle, sGenre, sLanguage, sYear, ageIndication);
                    frame.dispose();
                }
            } catch(NumberFormatException e) {
                lengthField.setText("ONGELDIGE WAARDE");
            }
        });

        container.add(title);
        container.add(titleField);
        container.add(genre);
        container.add(genreBox);
        container.add(age);
        container.add(ageBox);
        container.add(length);
        container.add(lengthField);
        container.add(language);
        container.add(languageField);
        container.add(year);
        container.add(yearSpinner);

        container.add(new JLabel(""));
        container.add(new JLabel(""));
        container.add(new JLabel(""));
        container.add(addButton);

        if(movie != null) {
            titleField.setText(movie.getTitle());
            genreBox.setSelectedItem(movie.getGenre());
            ageBox.setSelectedItem(movie.getAgeIndication());
            lengthField.setText(String.valueOf(movie.getDuration()));
            languageField.setSelectedItem(movie.getLanguage());
            yearSpinner.setValue(movie.getReleaseYear());
        }
    }
}