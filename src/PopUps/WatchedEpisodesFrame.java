package PopUps;

import Application.Properties;
import Data.Repositories.AccountRepository;
import Domain.Episode;
import Domain.Profile;
import Domain.Series;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class WatchedEpisodesFrame implements Runnable {
    /*
        The AccountPanel has an overview of series that a given profile has watched, upon clicking one of these series,
        this pop-up will come up with the watched episodes by the selected profile and given series
     */

    private Series series;   //The series that the episodes should be from
    private Profile profile; //The profile that the episodes are watched by
    private JFrame frame;

    public WatchedEpisodesFrame(Series series, Profile profile) {
        this.series = series;
        this.profile = profile;
    }

    @Override
    public void run() {
        frame = new JFrame("Afleveringen van serie '" + series.getTitle() + "' die " + profile.getProfileName() +
                    " bekeken heeft");

        frame.setMinimumSize(new Dimension(600, 400));
        frame.setPreferredSize(frame.getMinimumSize());

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Creates a JScrollPane with episodes that have been watched from a given series by a given profile
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());

        //scroller to hold the list with episodes
        JScrollPane listHolder = new JScrollPane();

        JPanel episodeList = new JPanel();
        episodeList.setLayout(new BoxLayout(episodeList, BoxLayout.Y_AXIS));
        List<Episode> episodes = AccountRepository.getWatchedEpisodes(profile, series);

        for(Episode e : episodes) {
            episodeList.add(createEpisodePanel(e));
        }

        listHolder.getViewport().add(episodeList);
        container.add(listHolder);
    }

    //Creatse a panel that holds information for 1 episode, on click allows the user to update the progress on the episode
    private JPanel createEpisodePanel(Episode episode) {
        JPanel episodeInformation = new JPanel();
        episodeInformation.setLayout(new BorderLayout());
        episodeInformation.setBorder(BorderFactory.createMatteBorder(1,0,1,0, Properties.BGCOLOR));

        episodeInformation.add(new JLabel(episode.toString()), BorderLayout.WEST);
        episodeInformation.add(new JLabel(episode.getPercentageWatched() + "% bekeken "),
                BorderLayout.EAST);

        episodeInformation.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                episodeInformation.setCursor(new Cursor(Cursor.HAND_CURSOR));
                episodeInformation.setBackground(Properties.HOVERCOLOR);
                episodeInformation.setToolTipText("Kijkpercentage van '" + episode.getTitle() +  "' aanpassen");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                WatchlistEpisodeEditor wee = new WatchlistEpisodeEditor(profile, series, episode);
                SwingUtilities.invokeLater(wee);
                frame.dispose();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                episodeInformation.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                episodeInformation.setBackground(Color.WHITE);
                episodeInformation.setToolTipText("");
            }
        });

        episodeInformation.setPreferredSize(new Dimension(episodeInformation.getPreferredSize().width, 75));
        episodeInformation.setMaximumSize(new Dimension(episodeInformation.getMaximumSize().width, episodeInformation.getPreferredSize().height));
        return episodeInformation;
    }
}
