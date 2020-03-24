package Business.Listeners;

import Domain.Series;
import Presentation.Commons.SeriesPanel.SeriesPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SeriesResultButtonListener extends MouseAdapter {
    /*
        Listens to the result button in SeriesPanel's options panel
        uses the values of the dropdowns in the database query
     */
    private SeriesPanel seriesPanel;

    public SeriesResultButtonListener(SeriesPanel panel) {
        this.seriesPanel = panel;
    }

    //Getes the series and account that have been selected in SeriesPanel and updates the InformationPanel accordingly
    @Override
    public void mouseClicked(MouseEvent e) {
        //Get the series that the user has chosen
        JPanel optionsPanel = (JPanel) seriesPanel.getComponent(0);
        JPanel upperOptions = (JPanel) optionsPanel.getComponent(0);
        JPanel seriesDropdownHolder = (JPanel) upperOptions.getComponent(0);
        JComboBox seriesDropdown = (JComboBox) seriesDropdownHolder.getComponent(1);
        Series chosenSeries = (Series) seriesDropdown.getSelectedItem();

        //Get the account that the user has chosen
        JPanel accountDropdownHolder = (JPanel) upperOptions.getComponent(1);
        JComboBox accountDropdown = (JComboBox) accountDropdownHolder.getComponent(1);
        String chosenAccount = (String) accountDropdown.getSelectedItem();

        //Checks if the chosen account and series are correct, only then can the information panel be updated
        //Prevents updates with invalid input
        if(chosenAccount != null && chosenSeries != null) {
            if (!chosenAccount.equals("-- alle accounts --")) {
                seriesPanel.updateInformationPanel(chosenSeries, chosenAccount);
            } else {
                seriesPanel.updateInformationPanel(chosenSeries);
            }
        }
    }
}
