package Application;

import Presentation.UserInterface;

import javax.swing.*;

public class NetflixStatistix {

    //Starts the application by creating an instance of UserInterface (presentation package)

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        SwingUtilities.invokeLater(ui);

    }
}
