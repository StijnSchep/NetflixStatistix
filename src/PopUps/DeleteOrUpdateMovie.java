package PopUps;

import Application.Properties;
import Data.Repositories.MovieRepository;
import Domain.Movie;


import javax.swing.*;
import java.awt.*;


public class DeleteOrUpdateMovie implements Runnable{
    /*
        When a user clicks on a movie in MoviePanel, the user gets the choice to delete or update the movie
     */
    private Movie movie;
    private JFrame frame;

    public DeleteOrUpdateMovie(Movie m) {
        this.movie = m;
    }

    @Override
    public void run() {
        frame = new JFrame("Film updaten of verwijderen");
        frame.setMinimumSize(new Dimension(400, 100));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Creates delete and update buttons. On delete runs delete query in MovieRepository
    //On update, opens an AddMovie pop-up with the given movie. This way, the user can update the movie's values
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Film updaten of verwijderen"), BorderLayout.NORTH);

        JPanel options = new JPanel();
        options.setLayout(new GridLayout(1,2));
        JButton update = new JButton("Updaten");
        update.setBackground(Properties.BUTTONCOLOR);
        update.addActionListener(ae -> {
            AddMovie am = new AddMovie(movie);
            SwingUtilities.invokeLater(am);
            frame.dispose();
        });
        options.add(update);

        JButton delete = new JButton("Verwijderen");
        delete.setBackground(Properties.BUTTONCOLOR);
        delete.addActionListener(ae -> {
            MovieRepository.removeMovieFromDatabase(movie);
            frame.dispose();
        });
        options.add(delete);

        container.add(options);
    }
}
