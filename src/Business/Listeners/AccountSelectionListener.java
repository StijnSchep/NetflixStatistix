package Business.Listeners;

import Data.Repositories.AccountRepository;
import Domain.Account;
import Domain.Profile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountSelectionListener implements ActionListener {
    /*
        This actionlistener listeners for a change in the "account" dropdown menu in Account panel > options panel
        The listener will update the "profiles" dropdown menu with profiles of the selected
     */

    private JComboBox<Profile> profileJComboBox;

    public AccountSelectionListener(JComboBox<Profile> profileJComboBox) {
        this.profileJComboBox = profileJComboBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<Account> source = (JComboBox) e.getSource();

        Account a = (Account) source.getSelectedItem();

        if(a != null) {
            Profile[] profileSet = AccountRepository.getProfilesByAccName(a.getName());
            profileJComboBox.setModel(new DefaultComboBoxModel<>(profileSet));
        } else {
            profileJComboBox.setModel(new DefaultComboBoxModel<>());
        }
    }

}
