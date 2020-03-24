package Domain;

import java.util.Date;

public class Profile {
    /*
        Holds information for a profile
     */

    private int profileID, accountID;
    private String profileName;
    private Date dateOfBirth;

    public Profile(int pID, int aID, String name, Date birth) {
        this.profileID = pID;
        this.accountID = aID;
        this.profileName = name;
        this.dateOfBirth = birth;
    }

    public int getProfileID() {
        return profileID;
    }

    public int getAccountID() { return accountID; }

    public String getProfileName() {
        return profileName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String toString() {
        return profileName;
    }
}
