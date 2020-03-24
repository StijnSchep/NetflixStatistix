package PopUps;

import Application.Properties;
import Data.Repositories.EpisodeRepository;
import Domain.Episode;
import Domain.Series;

import javax.swing.*;
import java.awt.*;

public class DeleteOrUpdateEpisode implements Runnable {
    /*
        When a user clicks on an episode in SeriesPanel, this pop-up opens to give the option of either deleting or
        updating that episode with new values
     */
    private Series series;
    private Episode episode;
    private JFrame frame;

    public DeleteOrUpdateEpisode(Series series, Episode episode) {
        this.series = series;
        this.episode = episode;
    }

    @Override
    public void run() {
        frame = new JFrame("Aflevering updaten of verwijderen");
        frame.setMinimumSize(new Dimension(400, 100));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Creates update and delete buttons. On delete, runs the delete query from EpisodeRepository.
    //On update opens a AddEpisode pop-up with the given episode. This way, the user can update the values in the new pop-up
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Aflevering updaten of verwijderen?"), BorderLayout.NORTH);

        JPanel options = new JPanel();
        options.setLayout(new GridLayout(1,2));
        JButton update = new JButton("Updaten");
        update.setBackground(Properties.BUTTONCOLOR);
        update.addActionListener(ae -> {
            AddEpisode aep = new AddEpisode(series, episode);
            SwingUtilities.invokeLater(aep);
            frame.dispose();
        });
        options.add(update);

        JButton delete = new JButton("Verwijderen");
        delete.setBackground(Properties.BUTTONCOLOR);
        delete.addActionListener(ae -> {
            EpisodeRepository.removeEpisodeFromDatabase(episode);
            frame.dispose();
        });
        options.add(delete);

        container.add(options);
    }
}
