package Presentation.Commons.SeriesPanel;

import Business.Listeners.SeriesResultButtonListener;
import Data.Repositories.AccountRepository;
import Data.Repositories.EpisodeRepository;
import Data.Repositories.SeriesRepository;
import Domain.Episode;
import Domain.Series;
import PopUps.AddEpisode;
import PopUps.AddSeries;
import PopUps.DeleteOrUpdateEpisode;
import PopUps.DeleteOrUpdateSeries;
import Presentation.Commons.CustomComponents.ColoredText;
import Presentation.Commons.CustomComponents.HeaderText;
import Presentation.Commons.CustomComponents.SubHeaderText;
import Application.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SeriesPanel extends JPanel {
    /*
        extends JPanel, contains two parts
        On the left side: an options panel that contains JComboBoxes and buttons to change the displayed information
        On the right side: an information panel with information based on the options panel
     */

    //Combobox seriesDropdown is defined here for easy access throughout the methods
    private static JComboBox<Series> seriesDropdown;

    //Sets BorderLayout, options panel is set to position WEST and information panel is set in position CENTER
    public SeriesPanel() {
        setLayout(new BorderLayout());

        addOptionsPanel();

        Series[] seriesList = SeriesRepository.getAllSeries();
        if(seriesList.length >= 1) {
            //By default, the information panel shows information of the first Series that came from the database,
            //this is also the default selected series in the seriesDropdown JComboBox
            updateInformationPanel(SeriesRepository.getAllSeries()[0]);
        }
    }

    //Creates and directly adds a JPanel 'optionsPanel' to the SeriesPanel's BorderLayout's WEST position
    private void addOptionsPanel() {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BorderLayout());
        optionsPanel.setBackground(Properties.BGCOLOR);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //options panel has an upper and lower part, upper part holds JComboboxes to select a series to show information for.
        // A JComboBox for accounts is available as well to base the results in the information panel on a certain account
        optionsPanel.add(createUpperOptions(), BorderLayout.NORTH);
        optionsPanel.add(createBottomOptions(), BorderLayout.SOUTH);

        add(optionsPanel, BorderLayout.WEST);
    }

    //Creates a panel that holds two JComboboxes and a results button
    private JPanel createUpperOptions() {
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(3,1,5,5));
        upperPanel.setBackground(Properties.BGCOLOR);

        addSeriesDropdown(upperPanel);
        addAccountDropdown(upperPanel);
        addResultsButton(upperPanel);


        return upperPanel;
    }

    //fills the series dropdown with series from the database
    private void addSeriesDropdown(JPanel to) {
        JPanel dropdownHolder = new JPanel();
        dropdownHolder.setLayout(new BoxLayout(dropdownHolder, BoxLayout.Y_AXIS));
        dropdownHolder.setBackground(Properties.BGCOLOR);

        // Add dropdown with series

        seriesDropdown = new JComboBox<>(SeriesRepository.getAllSeries());
        dropdownHolder.add(new ColoredText("Kies een serie", Color.WHITE, Properties.BGCOLOR));
        dropdownHolder.add(seriesDropdown);

        to.add(dropdownHolder);
    }

    //Creates a JComboBox and fills it with accounts from the database, adds it to the given JPanel
    private static void addAccountDropdown(JPanel to) {
        JPanel dropdownHolder = new JPanel();
        dropdownHolder.setLayout(new BoxLayout(dropdownHolder, BoxLayout.Y_AXIS));
        dropdownHolder.setBackground(Properties.BGCOLOR);

        // Add dropdown with account names
        List<String> names = AccountRepository.getAccountNames();
        String[] accountNames = new String[names.size() + 1];

        //The first 'account' is an unusable mock account to allow users to base results on all accounts
        accountNames[0] = "-- alle accounts --";
        for(int i = 1; i <= names.size(); i++) {
            accountNames[i] = names.get(i - 1);
        }

        JComboBox<String> accountDropdown = new JComboBox<>(accountNames);
        dropdownHolder.add(new ColoredText("Kies een account (optioneel)", Color.WHITE, Properties.BGCOLOR));
        dropdownHolder.add(accountDropdown);

        to.add(dropdownHolder);
    }

    //Creates a button with a SeriesResultButtonListener. On click updates the information panel with information based
    //on the values in the accountDropdown and seriesDropdown
    private void addResultsButton(JPanel to) {
        JButton resultButton = new JButton("Toon resultaten");
        resultButton.setBackground(Properties.BUTTONCOLOR);
        resultButton.addMouseListener(new SeriesResultButtonListener(this));

        to.add(resultButton);
    }

    //Creates buttons to add a series and an episode to currently selected series
    private JPanel createBottomOptions() {
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(2, 1));

        //On click, creates a pop-up to add a series
        JButton addSeries = new JButton("Voeg serie toe");
        addSeries.setBackground(Properties.BUTTONCOLOR);
        addSeries.addActionListener(ae -> {
            AddSeries as = new AddSeries();
            SwingUtilities.invokeLater(as);
        });
        container.add(addSeries);

        //On click, creates a pop-up to add a new episode to the selected series
        JButton addEpisode = new JButton("Voeg aflevering toe aan serie");
        addEpisode.setBackground(Properties.BUTTONCOLOR);
        addEpisode.addActionListener(ae -> {
            AddEpisode aep = new AddEpisode((Series) seriesDropdown.getSelectedItem());
            SwingUtilities.invokeLater(aep);
        });
        container.add(addEpisode);

        return container;
    }

    public void updateInformationPanel(Series series) {
        updateInformationPanel(series, "");
    }

    //Creates a panel with two parts
    //Part one: BorderLayout NORTH position, is a panel which holds information about the given series
    //Part two: BorderLayout CENTER position, is a JScrollPane with a list of episodes for the selected series
    //The account string can be used to base the statistics on a certain account. If account is an empty string, the
    //underlying query will see it as "base on all accounts"
    public void updateInformationPanel(Series series, String account) {
        try {
            remove(1); //causes ArrayIndexOutOfBoundsException on first run
        } catch (ArrayIndexOutOfBoundsException e) {
            //ignore
        }

        JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new BorderLayout());

        informationPanel.add(createSeriesInfoPanel(series, account), BorderLayout.NORTH);
        informationPanel.add(createEpisodeList(series, account));

        add(informationPanel);

        repaint();
        validate();
    }

    //Creates a panel with information about the given series. Statistics can be based on given account string
    private JPanel createSeriesInfoPanel(Series series, String account) {
        JPanel seriesInfo = new JPanel();
        seriesInfo.setLayout(new BorderLayout());
        seriesInfo.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Properties.BGCOLOR));
        seriesInfo.setBackground(Properties.HOVERCOLOR);
        Series chosenSeries = SeriesRepository.getSeriesByArgs(series.getSeriesID(), account);


        //Panel with title and series genre - ageindication
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        titlePanel.setBackground(Properties.HOVERCOLOR);

        titlePanel.add(new HeaderText(chosenSeries.getTitle()));
        titlePanel.add(new SubHeaderText(chosenSeries.getGenre() + " - " + chosenSeries.getAgeIndication()));
        seriesInfo.add(titlePanel, BorderLayout.WEST);

        //Panel with percentage watched
        JPanel watchPercentagePanel = new JPanel();
        watchPercentagePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        watchPercentagePanel.setBackground(Properties.HOVERCOLOR);
        watchPercentagePanel.add(new SubHeaderText(
                "van deze serie is gemiddeld " + String.valueOf(chosenSeries.getAverageWatchTime()) + "% bekeken"));
        seriesInfo.add(watchPercentagePanel, BorderLayout.EAST);

        //When clicking on this panel, the user gets the option of either deleting or updating the given series
        seriesInfo.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                seriesInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
                seriesInfo.setToolTipText("update of verwijder '" + chosenSeries.getTitle() +  "'");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                DeleteOrUpdateSeries du = new DeleteOrUpdateSeries(chosenSeries);
                SwingUtilities.invokeLater(du);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                seriesInfo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                seriesInfo.setToolTipText("");
            }
        });

        return seriesInfo;
    }

    //Second part of the information panel, is a scrollpane with a list of episodes
    private JScrollPane createEpisodeList(Series series, String account) {
        List<Episode> episodes = EpisodeRepository.getEpisodesByArgs(series.getSeriesID(), account);

        //This will hold the list with episodes
        JScrollPane scroller = new JScrollPane();

        //This will hold the episodes
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        //For each episode, create a mini panel that displays information of that episode and add it to the list
        //episodeInformationPanel resembles the seriesInfoPanel from the createSeriesInfoPanel() method
        for(Episode e : episodes) {
            listContainer.add(createEpisodeInformationPanel(e));
        }

        scroller.getViewport().add(listContainer);

        return scroller;
    }

    //Create a JPanel with information from the given episode, information includes episode number, title and percentage
    //watched on average by either all accounts or a specified account. On click, opens a pop-up that asks if the user
    //wants to update or delete the episode
    private JPanel createEpisodeInformationPanel(Episode episode) {
        JPanel episodeInformation = new JPanel();
        episodeInformation.setLayout(new BorderLayout());
        episodeInformation.setBorder(BorderFactory.createMatteBorder(1,0,1,0, Properties.BGCOLOR));

        episodeInformation.add(new JLabel(episode.toString()), BorderLayout.WEST);
        episodeInformation.add(new JLabel("Aflevering is gemiddeld " + episode.getPercentageWatched() + "% bekeken "),
                BorderLayout.EAST);

        episodeInformation.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                episodeInformation.setCursor(new Cursor(Cursor.HAND_CURSOR));
                episodeInformation.setBackground(Properties.HOVERCOLOR);
                episodeInformation.setToolTipText("update of verwijder '" + episode.getTitle() +  "'");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Series selected = (Series) seriesDropdown.getSelectedItem();
                DeleteOrUpdateEpisode du = new DeleteOrUpdateEpisode(selected, episode);
                SwingUtilities.invokeLater(du);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                episodeInformation.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                episodeInformation.setBackground(Color.WHITE);
                episodeInformation.setToolTipText("");
            }
        });

        episodeInformation.setPreferredSize(new Dimension(episodeInformation.getPreferredSize().width, 50));
        episodeInformation.setMaximumSize(new Dimension(episodeInformation.getMaximumSize().width, 50));
        return episodeInformation;
    }

}
