package PopUps;

import Application.Properties;
import Data.Repositories.AccountRepository;
import Domain.Account;
import Domain.Profile;
import Presentation.Commons.CustomComponents.ColoredText;
import Presentation.Commons.CustomComponents.HeaderText;

import javax.swing.*;
import java.awt.*;

public class ProfileEditor implements Runnable {
    /*
        Has two functions:
        - Allow the user to add a new profile
        - Allow the user to update a profile
     */

    //Account where the profile should be added to or be updated from
    private Account account;

    //If profile is set, the pop-up updates the database instead of adding a profile
    private Profile profile;

    private JFrame frame;


    public ProfileEditor(Account acc, Profile profile) {
        account = acc;
        this.profile = profile;
    }

    public ProfileEditor(Account acc) {
        this(acc, null);
    }

    @Override
    public void run() {
        frame = new JFrame("Profiel aanpassen / toevoegen");
        frame.setPreferredSize(new Dimension(750, 150));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    // Creates input fields for a profile's data, if a profile is given to the pop-up, the input fields will be filled
    // in with old data
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        JTextField name = new JTextField("profielnaam");
        JTextField birthdate = new JTextField("geboortedatum (YYYY-MM-DD)");
        ColoredText error = new ColoredText("", Color.BLACK, Color.RED);

        if(account.getID() == -1) {
            container.add(new HeaderText("SELECTEER EERST EEN ACCOUNT"));
        } else {
            JPanel holder = new JPanel();
            holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));
            holder.add(name, BorderLayout.NORTH);
            holder.add(birthdate);

            JButton saveButton = new JButton("opslaan");
            saveButton.setBackground(Properties.BUTTONCOLOR);
            saveButton.setSize(new Dimension(name.getSize()));
            saveButton.addActionListener(ae -> {
                if(!name.getText().isEmpty() && !birthdate.getText().isEmpty()) {
                    error.setText("");

                    if(!birthdate.getText().matches("[0-9]{4}-[01][0-9]-[0-3][0-9]")) {
                        error.setText("Vul een juiste geboortedatum in (YYYY-MM-DD)");
                    } else {
                        if(profile != null) {
                            AccountRepository.updateProfile(profile.getProfileID(), account.getID(), name.getText(), birthdate.getText());
                            frame.dispose();
                        } else {
                            AccountRepository.createProfile(account.getID(), name.getText(), birthdate.getText());
                            frame.dispose();
                        }
                    }
                } else {
                    error.setText("Vul een naam en datum in!");
                    container.repaint();
                    container.validate();
                }
            });



            if(profile != null) {
                name.setText(profile.getProfileName());
                birthdate.setText(String.valueOf(profile.getDateOfBirth()));
            }

            container.add(error, BorderLayout.NORTH);
            container.add(holder);
            container.add(saveButton, BorderLayout.SOUTH);
        }
    }

}
