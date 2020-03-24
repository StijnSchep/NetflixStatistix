package Business;

import Presentation.Commons.AccountPanel.AccountPanel;
import Presentation.Commons.MainPanel.MainPanel;
import Presentation.Commons.MoviePanel.MoviePanel;
import Presentation.Commons.SeriesPanel.SeriesPanel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

class PanelSwitcherTest {
    private static Container container;

    @BeforeAll
    static void setUp() {
        //Set up container with two components for the panelSwitcher to work with
        container = new Container();
        container.add(new JPanel());

        //This panel should remain in place with invalid input
        JPanel panelInSwitchPosition = new JPanel();
        container.add(panelInSwitchPosition);
    }

    @Test
    void testSwitchToSwitchesCorrectlyToMainPanel() {
        PanelSwitcher testCase = new PanelSwitcher(container);

        //invalid name, should be "Serie Statistieken"
        testCase.switchTo(PanelValue.MAIN);
        Component currentPanel = container.getComponent(1);
        Assertions.assertSame(currentPanel.getClass(), MainPanel.class);
    }

    @Test
    void testSwitchToSwitchesCorrectlyToSeriesPanel() {
        PanelSwitcher testCase = new PanelSwitcher(container);

        //invalid name, should be "Serie Statistieken"
        testCase.switchTo(PanelValue.SERIES);
        Component currentPanel = container.getComponent(1);
        Assertions.assertSame(currentPanel.getClass(), SeriesPanel.class);
    }

    @Test
    void testSwitchToSwitchesCorrectlyToMoviePanel() {
        PanelSwitcher testCase = new PanelSwitcher(container);

        //invalid name, should be "Serie Statistieken"
        testCase.switchTo(PanelValue.FILM);
        Component currentPanel = container.getComponent(1);
        Assertions.assertSame(currentPanel.getClass(), MoviePanel.class);
    }

    @Test
    void testSwitchToSwitchesCorrectlyToAccountPanel() {
        PanelSwitcher testCase = new PanelSwitcher(container);

        //invalid name, should be "Serie Statistieken"
        testCase.switchTo(PanelValue.ACCOUNT);
        Component currentPanel = container.getComponent(1);
        Assertions.assertSame(currentPanel.getClass(), AccountPanel.class);
    }

}