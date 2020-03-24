package PopUps;

import Application.Properties;
import Data.Repositories.SeriesRepository;
import Domain.Series;

import javax.swing.*;
import java.awt.*;

public class DeleteOrUpdateSeries implements Runnable{
    /*
        When a user clicks on a series' panel in SeriesPanel's information panel, this pop-up gives the user the
        option of either deleting the series or updating it with new values
     */

    //The series that can be updated or deleted
    private Series series;
    private JFrame frame;

    public DeleteOrUpdateSeries(Series series) {
        this.series = series;
    }

    @Override
    public void run() {
        frame = new JFrame("Serie updaten of verwijderen");
        frame.setMinimumSize(new Dimension(400, 100));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Create two buttons, delete or update. On delete, runs the delete query from SeriesRepository
    //On update, opens the AddSeries() pop-up with the given series. This way the user can update the values of the
    //series
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Serie updaten of verwijderen"), BorderLayout.NORTH);

        JPanel options = new JPanel();
        options.setLayout(new GridLayout(1,2));
        JButton update = new JButton("Updaten");
        update.setBackground(Properties.BUTTONCOLOR);
        update.addActionListener(ae -> {
            AddSeries as = new AddSeries(series);
            SwingUtilities.invokeLater(as);
            frame.dispose();
        });
        options.add(update);

        JButton delete = new JButton("Verwijderen");
        delete.setBackground(Properties.BUTTONCOLOR);
        delete.addActionListener(ae -> {
            SeriesRepository.removeSeriesFromDatabase(series);
            frame.dispose();
        });
        options.add(delete);

        container.add(options);
    }
}
