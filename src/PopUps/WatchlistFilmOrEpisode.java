package PopUps;

import Application.Properties;
import Domain.Profile;

import javax.swing.*;
import java.awt.*;

public class WatchlistFilmOrEpisode implements Runnable {
    //When pressing the "Voeg bekeken programma toe" button, this pop-up asks if the user wants to add a film or
    //episode to the selected profile's watchlist
    private JFrame frame;
    private Profile profile;

    public WatchlistFilmOrEpisode(Profile p) {
        this.profile = p;
    }

    @Override
    public void run() {
        frame = new JFrame("Maak een keuze");
        frame.setPreferredSize(new Dimension(400, 100));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Create a panel that gives the user a choice between adding an episode and a movie to the profile's watchlist.
    //If the user chooses movie, opens a WatchlistFilmEditor pop-up
    //If the user chooses episode, opens a WatchlistEpisodeEditor pop-up
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Film of Aflevering toevoegen?"), BorderLayout.NORTH);

        JPanel options = new JPanel();
        options.setLayout(new GridLayout(1,2));
        JButton addFilm = new JButton("Film");
        addFilm.setBackground(Properties.BUTTONCOLOR);
        addFilm.addActionListener(ae -> {
            WatchlistFilmEditor wee = new WatchlistFilmEditor(profile);
            SwingUtilities.invokeLater(wee);
            frame.dispose();
        });
        options.add(addFilm);

        JButton addEpisode = new JButton("Aflevering");
        addEpisode.setBackground(Properties.BUTTONCOLOR);
        addEpisode.addActionListener(ae -> {
            WatchlistEpisodeEditor wfe = new WatchlistEpisodeEditor(profile);
            SwingUtilities.invokeLater(wfe);
            frame.dispose();
        });
        options.add(addEpisode);

        container.add(options);
    }
}
