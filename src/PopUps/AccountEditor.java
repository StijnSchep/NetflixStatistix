package PopUps;

import Application.Properties;
import Data.Repositories.AccountRepository;
import Domain.Account;
import Presentation.Commons.AccountPanel.AccOptionsPanel;
import Presentation.Commons.CustomComponents.HeaderText;

import javax.swing.*;
import java.awt.*;

public class AccountEditor implements Runnable {
    /*
        In AccountPanel, the user has the option to add or update an account. This pop-up is used to get the values
     */

    //If an account is being updated, this variable is not null
    private Account account;

    private JFrame frame;
    //the AccOptionsPanel is specified to update the panel when a new account is added / updated
    private AccOptionsPanel accOptionsPanel;
    private JTextField placeField;

    public AccountEditor(AccOptionsPanel panel, Account acc) {
        account = acc;
        accOptionsPanel = panel;
        placeField = new JTextField();
    }

    public AccountEditor(AccOptionsPanel panel) {
        this(panel, null);
    }


    @Override
    public void run() {
        if(account != null) {
            frame = new JFrame("Account '" + account.getName() + "' aanpassen");
            frame.setPreferredSize(new Dimension(750, 150));
        } else {
            frame = new JFrame("Nieuw account aanmaken");
            frame.setPreferredSize(new Dimension(750, 200));
        }

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    //Creates input fields for the account
    //If the user tries to update an account without selecting one, a message is given instead of input fields
    //If a new account is created, an additional input field is given for the birth date, this is used to automatically
    //create a profile for the new account
    //When updating an account, this birth date field is not shown
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());
        JTextField name = new JTextField();
        JTextField houseNoField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField additiveField = new JTextField();

        if(account != null && account.getID() == -1) {
            container.add(new HeaderText("Selecteer eerst een account!"));
        } else {
            //JPanel for name
            JPanel subscriberName = new JPanel();
            subscriberName.setLayout(new BorderLayout());
            subscriberName.add(new JLabel("Naam van account"), BorderLayout.NORTH);
            subscriberName.add(name);
            container.add(subscriberName, BorderLayout.NORTH);


            JPanel addressAndBirthdate = new JPanel();
            addressAndBirthdate.setLayout(new BoxLayout(addressAndBirthdate, BoxLayout.Y_AXIS));

            //JPanel for street, housenumber, additive, place
            JPanel address = new JPanel();
            address.setLayout(new BoxLayout(address, BoxLayout.X_AXIS));

            //JPanel for street, subcomponent of address
            JPanel street = new JPanel();
            street.setLayout(new BorderLayout());
            street.add(new JLabel("Straat"), BorderLayout.NORTH);
            street.add(streetField);
            address.add(street);

            //JPanel for house number, subcomponent of address
            JPanel houseNo = new JPanel();
            houseNo.setLayout(new BorderLayout());
            houseNo.add(new JLabel("Huisnummer"), BorderLayout.NORTH);
            houseNo.add(houseNoField);
            address.add(houseNo);

            //JPanel for additive, subcomponent of address
            JPanel additive = new JPanel();
            additive.setLayout(new BorderLayout());
            additive.add(new JLabel("toevoeging (optioneel)"), BorderLayout.NORTH);
            additive.add(additiveField);
            address.add(additive);

            //JPanel for place, subcomponent of address
            JPanel place = new JPanel();
            place.setLayout(new BorderLayout());
            place.add(new JLabel("Woonplaats"), BorderLayout.NORTH);
            place.add(placeField);
            address.add(place);

            JPanel birthdate = new JPanel();
            birthdate.setLayout(new BorderLayout());
            birthdate.add(new JLabel("Geboortedatum accounteigenaar (voor hoofdprofiel, YYYY-MM-DD)"), BorderLayout.NORTH);
            JTextField birthdateField = new JTextField();
            birthdate.add(birthdateField);

            addressAndBirthdate.add(address);
            addressAndBirthdate.add(birthdate);

            if(account == null) {
                //A new account is being created, which means that no profile exists yet
                //Include a testfield to get the birthdate for the initial profile
                container.add(addressAndBirthdate);
            } else if(account != null && account.getID() > 0) {
                //An account is being updated, so a main profile already exists
                //The birthdate should not be edited here in this case
                container.add(address);
            }

            JButton saveButton = new JButton("opslaan");
            saveButton.setBackground(Properties.BUTTONCOLOR);
            saveButton.addActionListener(ae -> {
                //houseNo, String name, String street, String additive, String place
                String houseNumber = houseNoField.getText();
                if(!houseNumber.matches("[1-9][0-9]*")) {
                    houseNoField.setText("ONGELDIG");
                    return;
                }
                String subName = name.getText();
                String streetValue = streetField.getText();
                String additiveValue = additiveField.getText();
                String placeValue = placeField.getText();

                if(houseNumber.isEmpty()) {houseNoField.setText("INVULLEN!"); return;}
                if(subName.isEmpty()) {name.setText("INVULLEN!"); return;}
                if(streetValue.isEmpty()) {streetField.setText("INVULLEN!"); return;}
                if(placeValue.isEmpty()) {placeField.setText("INVULLEN!"); return;}

                int houseNoValue = Integer.parseInt(houseNumber);

                //If no existing account is given to the pop-up, the user needs to enter a correct birthdate
                //If no correct birthdate is given, the user will be notified
                String sBirthdate = birthdateField.getText();
                if(!sBirthdate.matches("[0-9]{4}-[01][0-9]-[0-3][0-9]") && account == null) {
                    birthdateField.setText("ONGELDIGE WAARDE");
                    return;
                }


                if(account == null) {
                    //Account is null, so the user is adding an account
                    AccountRepository.createAccount(houseNoValue, subName, streetValue, additiveValue, placeValue);

                    //Go through existing accounts to find the newly created account, add the profile to that account
                    Account[] accounts = AccountRepository.getAllAccounts();
                    for(Account a : accounts) {
                        if(a.getName().equals(subName) && a.getHouseNumber() == houseNoValue &&
                                a.getStreet().equals(streetValue) && a.getPlace().equals(placeValue)) {
                            AccountRepository.createProfile(a.getID(), a.getName(), sBirthdate);
                        }
                    }

                } else {
                    //Account is not null, so the user is changing an existing account
                    AccountRepository.updateAccount(account.getID(), houseNoValue, subName, streetValue,
                            additiveValue, placeValue);
                }

                //repaint the options panel to show changes
                accOptionsPanel.updateOptionsPanel();
                frame.dispose();
            });

            container.add(saveButton, BorderLayout.SOUTH);


            if (account != null && account.getID() > 0) {
                name.setText(account.getName());
                houseNoField.setText(String.valueOf(account.getHouseNumber()));
                streetField.setText(account.getStreet());
                additiveField.setText(account.getAdditive());
                placeField.setText(account.getPlace());
            }
        }
    }
}
