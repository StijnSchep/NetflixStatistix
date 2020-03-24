package Presentation.Commons.AccountPanel;

import Application.Properties;
import Business.Listeners.AccountSelectionListener;
import Data.Repositories.AccountRepository;
import Domain.Account;
import Domain.Profile;
import Presentation.Commons.CustomComponents.ColoredText;
import PopUps.AccountEditor;
import PopUps.ProfileEditor;
import PopUps.WatchlistFilmOrEpisode;

import javax.swing.*;
import java.awt.*;

public class AccOptionsPanel extends JPanel {
    /*
        Left part of AccountPanel, gives user options regarding account information
        Top part has dropdowns to select accounts, underlying profiles and movies/series that have been watched by the
        selected profile. Accounts and profiles can also be created

        The lower part has options to remove and update selected accounts and profiles. The user can also add watched programmes
        to the profile's watchlist and get an overview of all accounts with only 1 profile
     */


    //Variable is public static for easy access of the selected profile in other classes
    public static JComboBox<Profile> profiles;

    private JComboBox<Account> accounts;
    private AccInformationPanel accInfo;

    AccOptionsPanel(AccInformationPanel accInformation) {
        setBackground(Properties.BGCOLOR);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, getPreferredSize().height));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        profiles = new JComboBox<>();
        accounts = new JComboBox<>();
        accInfo = accInformation;
        add(createDropdowns(), BorderLayout.NORTH);
        add(createLowerButtons(), BorderLayout.SOUTH);
    }

    //Repaint the options panel to show changes after CRUD operation
    public void updateOptionsPanel() {
        accounts.setSelectedIndex(0);
        fillAccountsDropdown();

        repaint();
        validate();

    }

    //create the dropdowns for accounts and profiles, including the "show movies/series" buttons
    private JPanel createDropdowns() {
        JPanel dropdowns = new JPanel();
        dropdowns.setLayout(new BoxLayout(dropdowns, BoxLayout.Y_AXIS));
        dropdowns.setBackground(Properties.BGCOLOR);

        dropdowns.add(createAccountDropdown());
        dropdowns.add(createProfileDropdown());
        dropdowns.add(createShowResultButtons());

        return dropdowns;
    }

    //Creates account dropdown with "+" button to add an account, when user clicks on "+", an AccountEditor pop-up opens
    private JPanel createAccountDropdown() {
        JPanel dropdownHolder = new JPanel();
        dropdownHolder.setLayout(new BorderLayout());
        dropdownHolder.setBackground(Properties.BGCOLOR);

        dropdownHolder.add(new ColoredText("Selecteer account", Color.WHITE, Properties.BGCOLOR), BorderLayout.NORTH);
        fillAccountsDropdown();

        accounts.addActionListener(new AccountSelectionListener(profiles));
        dropdownHolder.add(accounts);

        //add "+" button to account dropdown
        JButton addAccountButton = new JButton("+");
        addAccountButton.addActionListener(ae -> {
            AccountEditor accEditor = new AccountEditor(this);
            SwingUtilities.invokeLater(accEditor);
        });
        addAccountButton.setBackground(Properties.BUTTONCOLOR);
        dropdownHolder.add(addAccountButton, BorderLayout.EAST);

        return dropdownHolder;
    }

    //Fills the JComboBox with accounts, called when the options panel is updated after adding or removing an account
    private void fillAccountsDropdown() {
        accounts.removeAllItems();
        accounts.addItem(new Account(-1, -1, "-- Selecteer Account --", "", "", "" ));
        try {
            for (Account a : AccountRepository.getAllAccounts()) {
                accounts.addItem(a);
            }
        } catch(NullPointerException e) {
            System.out.println("No account found in the database");
            e.printStackTrace();
        }
    }

    //Fills the profile dropdown, by default, no valid profiles are added. When the user selects an account, this dropdown
    //Is updated with profiles from the account
    private JPanel createProfileDropdown() {
        JPanel dropdownHolder = new JPanel();
        dropdownHolder.setBackground(Properties.BGCOLOR);
        dropdownHolder.setLayout(new BorderLayout());

        profiles.removeAllItems();
        dropdownHolder.add(new ColoredText("Selecteer Profiel", Color.WHITE, Properties.BGCOLOR), BorderLayout.NORTH);
        dropdownHolder.add(profiles);


        //add "+" button to profile dropdown
        JButton addProfileButton = new JButton("+");
        addProfileButton.addActionListener(ae -> {
            if(profiles.getModel().getSize() == 5) {
                JOptionPane.showMessageDialog(null, "Een account kan slechts 5 profielen hebben!");
                return;
            }
            ProfileEditor pe = new ProfileEditor((Account) accounts.getSelectedItem());
            SwingUtilities.invokeLater(pe);
            updateOptionsPanel();
        });
        addProfileButton.setBackground(Properties.BUTTONCOLOR);
        dropdownHolder.add(addProfileButton, BorderLayout.EAST);

        return dropdownHolder;
    }

    //Createa a JPanel with two buttons. one button to show what shows a profile has watched
    //and a button to show what movies a profile has watched
    private JPanel createShowResultButtons() {
        JPanel buttonsHolder = new JPanel();
        buttonsHolder.setBackground(Properties.BGCOLOR);
        buttonsHolder.setLayout(new BorderLayout());
        buttonsHolder.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JButton watchedMovies = new JButton("Toon bekeken films");
        watchedMovies.setBackground(Properties.BUTTONCOLOR);

        JButton watchedSeries = new JButton("Toon bekeken series");
        watchedSeries.setBackground(Properties.BUTTONCOLOR);

        buttonsHolder.add(watchedMovies, BorderLayout.NORTH);
        buttonsHolder.add(watchedSeries);

        watchedMovies.addActionListener(ae -> {
            Profile selected = (Profile) profiles.getSelectedItem();
            if(selected == null || selected.getProfileName().equals("-- Selecteer Profiel --")) {
                return;
            }
            accInfo.showWatchedMovies(selected);
        });
        watchedSeries.addActionListener(ae -> {
            Profile selected = (Profile) profiles.getSelectedItem();
            if(selected == null) {
                return;
            }
            accInfo.showWatchedSeries(selected);
        });

        return buttonsHolder;
    }

    //Create the buttons to delete and update accounts/profiles, and buttons to add a watched programem to the watchlist
    private JPanel createLowerButtons() {
        JPanel lowerButtons = new JPanel();
        lowerButtons.setBackground(Properties.BGCOLOR);
        lowerButtons.setLayout(new BorderLayout(0,5));


        lowerButtons.add(createAccountProfileRemovalBtns(), BorderLayout.NORTH);
        lowerButtons.add(createProfileUpdateBtns());
        lowerButtons.add(createSingleProfilesButton(), BorderLayout.SOUTH);
        return lowerButtons;
    }

    //Create a button that makes the informationPanel switch to the "accounts with 1 profile" overview
    private JButton createSingleProfilesButton() {
        JButton singleProfiles = new JButton("Accounts met één profiel");
        singleProfiles.setBackground(Properties.BUTTONCOLOR);
        singleProfiles.addActionListener(ae -> {accInfo.showSingleProfiles();});


        return singleProfiles;
    }

    //Create JPanel with two JButtons to add a watched programme and to update the selected profile
    private JPanel createProfileUpdateBtns() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JButton addWatchedProgramme = new JButton("Voeg bekeken programma toe");
        addWatchedProgramme.setBackground(Properties.BUTTONCOLOR);
        addWatchedProgramme.addActionListener(ae -> {
            Profile selected = (Profile) profiles.getSelectedItem();
            if(selected != null) {
                if (selected.getProfileID() >= 0) {
                    WatchlistFilmOrEpisode wfe = new WatchlistFilmOrEpisode(selected);
                    SwingUtilities.invokeLater(wfe);
                }
            }
        });


        JButton updateProfile = new JButton("Update profiel");
        updateProfile.setBackground(Properties.BUTTONCOLOR);
        updateProfile.addActionListener(ae -> {
            Profile sProfile = (Profile) profiles.getSelectedItem();
            Account sAccount = (Account) accounts.getSelectedItem();

            if(sAccount != null && sProfile != null) {
                if(sProfile.getProfileID() >= 0) {
                    ProfileEditor pe = new ProfileEditor((Account) accounts.getSelectedItem(), (Profile) profiles.getSelectedItem());
                    SwingUtilities.invokeLater(pe);
                    updateOptionsPanel();
                }
            }
        });

        JButton updateAccount = new JButton("Update account");
        updateAccount.setBackground(Properties.BUTTONCOLOR);
        updateAccount.addActionListener(ae -> {
            AccountEditor accEditor = new AccountEditor(this, (Account) accounts.getSelectedItem());
            SwingUtilities.invokeLater(accEditor);
            updateOptionsPanel();
        });

        container.add(updateAccount, BorderLayout.NORTH);
        container.add(addWatchedProgramme);
        container.add(updateProfile, BorderLayout.SOUTH);

        return container;
    }

    //Create a JPanel with two buttons to remove selected accounts and profiles
    private JPanel createAccountProfileRemovalBtns() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JButton removeAccount = new JButton("Verwijder account");
        removeAccount.setBackground(Properties.BUTTONCOLOR);
        removeAccount.addActionListener(ae -> {
            Account selected = (Account) accounts.getSelectedItem();
            if(selected != null) {
                if (selected.getID() > 0) {
                    if(JOptionPane.showConfirmDialog(null,
                            "Weet je zeker dat je het account '" + selected.getName() + "' wil verwijderen?",
                            "Bevestig", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        //User has confirmed the deletion
                        AccountRepository.removeAccount(selected);
                        updateOptionsPanel();
                    }
                }
            }

        });


        JButton removeProfile = new JButton("Verwijder profiel");
        removeProfile.setBackground(Properties.BUTTONCOLOR);
        removeProfile.addActionListener(ae -> {
            Profile selected = (Profile) profiles.getSelectedItem();
            if(selected == null) {
                return;
            }

            if (selected.getProfileID() < 0) {
                return;
            }

            if(profiles.getModel().getSize() == 1) {
                JOptionPane.showMessageDialog(null,
                        "Het laatste profiel van een account kan niet verwijderd worden");
                return;
            }

            //The profile can be deleted, give the user the option to confirm it
            if(JOptionPane.showConfirmDialog(null,
                    "Weet je zeker dat je het profiel '" + selected.getProfileName() + "' wil verwijderen?",
                    "Bevestig", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                //User has confirmed the deletion
                AccountRepository.removeProfile(selected);
                updateOptionsPanel();
            }


        });

        container.add(removeAccount);
        container.add(removeProfile, BorderLayout.SOUTH);

        return container;
    }
}
