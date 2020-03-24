package Data.Repositories;

import Data.DBConnection;
import Domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountRepository {
    //Repository for fetching information regarding accounts from the database
    //Includes profile queries as well

    //Retrieve a list of all account names in the database
    public static List<String> getAccountNames() {
        List<String> names = new ArrayList<>();

        DBConnection conn = new DBConnection();
        String query = "SELECT SubscriberName FROM Subscription";

        try {
            ResultSet rs = conn.runQuery(query);
            while(rs.next()) {
                names.add(rs.getString("SubscriberName"));
            }
        } catch(Exception e) {
            System.out.println("Failed parsing ResultSet in class AccountRepository, method getAccountNames()");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        return names;
    }

    //Retrieve a list of all accounts in the database
    public static Account[] getAllAccounts() {
        DBConnection conn = new DBConnection();
        String query = "SELECT * FROM Subscription";

        return retrieveAccountsFromResultSet(conn.runQuery(query), conn);
    }

    //Retrieve a list of profiles that belong to a given account name
    public static Profile[] getProfilesByAccName(String arg) {
        if(arg == null) { throw new IllegalArgumentException("The account name cannot be null"); }
        if(arg.equals("")) { throw new IllegalArgumentException("The account name cannot be empty"); }

        DBConnection conn = new DBConnection();
        String query = "SELECT p.*\n" +
                "FROM Profile p\n" +
                "INNER JOIN Subscription s ON s.SubscriptionID = p.SubscriptionID\n" +
                "WHERE SubscriberName = '" + arg + "'";

        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();

            Profile[] profiles = new Profile[rs.getRow() + 1];

            if(rs.first()) {
                int aID = rs.getInt("SubscriptionID");
                profiles[0] = new Profile(-1, aID, "-- alle profielen van account --", new Date());
            }

            rs.beforeFirst();
            int i = 1;
            while(rs.next()) {
                int pID = rs.getInt("ProfileID");
                int aID = rs.getInt("SubscriptionID");
                String name = rs.getString("ProfileName");
                Date birth = rs.getDate("DateOfBirth");

                profiles[i] = new Profile(pID, aID, name, birth);
                i++;
            }

            return profiles;
        } catch(SQLException e) {
            System.out.println("Failed parsing ResultSet in class AccountRepository, method getAccountNames()");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        return new Profile[0];
    }

    //Retrieve a list of series that a given profile has seen
    public static Series[] getWatchedSeries(Profile p) {
        if(p == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        //watched series can either be based on an account as a whole, or on a specific profile.
        //If it's based on the account as a while, the query should be based on the SubscriptionID and not ProfileID
        //If profileID is -1, it means that the query should be based on an account as a whole
        String search = "";
        if(p.getProfileID() == -1) {
            search = "pr.SubscriptionID = " + p.getAccountID();
        } else {
            search = "pr.ProfileID = " + p.getProfileID();
        }

        DBConnection conn = new DBConnection();
        String query = "SELECT SeriesID, Title, Genre, AgeIndication, AVG(Progress) AS Progress\n" +
                "FROM (\n" +
                "SELECT SeriesID, Title, Genre, AgeIndication, eTitle, MAX(Progress) AS Progress\n" +
                "FROM (\n" +
                "SELECT s.SeriesID, s.Title, Genre, AgeIndication, p.Title AS eTitle, pp.ProfileID, pp.Progress\n" +
                "FROM Series s\n" +
                "INNER JOIN Episode e ON e.SeriesID = s.SeriesID\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = e.ProgrammeID\n" +
                "INNER JOIN Profile_Programme pp ON pp.ProgrammeID = p.ProgrammeID\n" +
                "INNER JOIN Profile pr ON pr.ProfileID = pp.ProfileID\n" +
                "WHERE " + search +
                "UNION\n" +
                "SELECT s.SeriesID, s.Title, Genre, AgeIndication, p.Title AS eTitle, NULL, 0\n" +
                "FROM Series s\n" +
                "INNER JOIN Episode e ON e.SeriesID = s.SeriesID\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = e.ProgrammeID\n" +
                ") AS result\n" +
                "GROUP BY SeriesID, Title, Genre, AgeIndication, eTitle\n" +
                ") AS result\n" +
                "GROUP BY SeriesID, Title, Genre, AgeIndication\n" +
                "HAVING AVG(Progress) > 0";

        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();

            Series[] series = new Series[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                int ID = rs.getInt("SeriesID");
                int progress = rs.getInt("Progress");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String age = rs.getString("AgeIndication");

                Series s = new Series(ID, title, genre, age, progress);
                series[i] = s;
                i++;
            }

            return series;
        } catch(SQLException e) {
            System.out.println("Failed parsing ResultSet in class AccountRepository, method getWatchedSeries()");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }


        return new Series[0];
    }

    //Retrieve a list of movies that a given profile has seen
    public static Movie[] getWatchedMovies(Profile p) {
        if(p == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        //watched movies can either be based on an account as a whole, or on a specific profile.
        //If it's based on the account as a while, the query should be based on the SubscriptionID and not ProfileID
        //If profileID is -1, it means that the query should be based on an account as a whole
        String search = "";
        if(p.getProfileID() == -1) {
            search = "pr.SubscriptionID = " + p.getAccountID();
        } else {
            search = "pr.ProfileID = " + p.getProfileID();
        }

        DBConnection conn = new DBConnection();
        String query = "SELECT ProgrammeID, Title, Genre, Language, AgeIndication, ReleaseYear, DurationInMinutes, AVG(Progress) AS Progress\n" +
                "FROM (\n" +
                "SELECT f.ProgrammeID, Title, Genre, Language, AgeIndication, ReleaseYear, DurationInMinutes, Progress\n" +
                "FROM Film f\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = f.ProgrammeID\n" +
                "INNER JOIN Profile_Programme pp ON pp.ProgrammeID = p.ProgrammeID\n" +
                "INNER JOIN Profile pr ON pr.ProfileID = pp.ProfileID\n" +
                "WHERE " + search +
                ") AS Result GROUP BY ProgrammeID, Title, Genre, Language, AgeIndication, ReleaseYear, DurationInMinutes";

        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();

            Movie[] movies = new Movie[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                int ID = rs.getInt("ProgrammeID");
                int duration = rs.getInt("DurationInMinutes");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String language = rs.getString("Language");
                String age = rs.getString("AgeIndication");
                int year = rs.getInt("ReleaseYear");
                int progress = rs.getInt("Progress");

                Movie m = new Movie(ID, duration, title, genre, language, age, year, progress, progress);
                movies[i] = m;
                i++;
            }

            return movies;
        } catch(SQLException e) {
            System.out.println("Failed parsing ResultSet in class AccountRepository, method getWatchedMovies()");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        return new Movie[0];
    }

    //Given a profile and series, get all episodes of a series that have been watched by the profile
    public static List<Episode> getWatchedEpisodes(Profile p, Series s) {
        if(p == null || s == null) {
            throw new IllegalArgumentException("Profile and Series cannot be null");
        }

        DBConnection conn = new DBConnection();
        String query = "SELECT p.ProgrammeID, p.DurationInMinutes, p.Title, e.SeriesID, e.EpisodeNumber, pp.Progress\n" +
                    "FROM Profile_Programme pp JOIN Programme p ON p.ProgrammeID = pp.ProgrammeID\n" +
                    "JOIN Episode e ON e.ProgrammeID = p.ProgrammeID\n" +
                    "JOIN Profile pr ON pr.ProfileID = pp.ProfileID\n" +
                    "WHERE pr.ProfileID = " + p.getProfileID() + " AND SeriesID = " + s.getSeriesID() +
                    " ORDER BY EpisodeNumber";


        List<Episode> episodes = new ArrayList<>();
        try {
            ResultSet rs = conn.runQuery(query);
            while(rs.next()) {
                int ID = rs.getInt("ProgrammeID");
                String number = rs.getString("EpisodeNumber");
                String title = rs.getString("Title");
                int duration = rs.getInt("DurationInMinutes");
                int percentageWatched = rs.getInt("Progress");

                episodes.add(new Episode(ID, number, title, duration, percentageWatched));
            }
        } catch(SQLException e) {
            System.out.println("Could not parse ResultSet in AccountRepository, method getWatchedEpisodes");
            e.printStackTrace();
        }

        return  episodes;
    }

    //Retrieve a list of accounts that only have one profile linked to them
    public static Account[] getAccountsWithOneProfile() {
        DBConnection conn = new DBConnection();
        String query = "SELECT s.*\n" +
                "FROM Subscription s\n" +
                "INNER JOIN Profile p ON p.SubscriptionID = s.SubscriptionID\n" +
                "GROUP BY s.SubscriptionID, SubscriberName, Street, HouseNumber, Additive, Place\n" +
                "HAVING COUNT(*) = 1;";

        return retrieveAccountsFromResultSet(conn.runQuery(query), conn);
    }

    //Turn a given ResultSet with account information into a list of Account objects
    private static Account[] retrieveAccountsFromResultSet(ResultSet rs, DBConnection conn) {
        Account[] accounts = null;
        try {
            rs.last();
            accounts = new Account[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                int ID = rs.getInt("SubscriptionID");
                String name = rs.getString("SubscriberName");
                String street = rs.getString("Street");
                int number = rs.getInt("HouseNumber");
                String additive = rs.getString("Additive");
                String place = rs.getString("Place");

                Account acc = new Account(ID, number, name, street, additive, place);

                accounts[i] = acc;
                i++;
            }
        } catch(Exception e) {
            System.out.println("Failed parsing ResultSet in class AccountRepository, method getAccountNames()");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        if(accounts == null) {
            return new Account[0];
        } else {
            return accounts;
        }
    }

    //Get a list of movies that have not been watched by a given profile
    public static Movie[] getUnwatchedMovies(Profile p) {
        if(p == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        DBConnection conn = new DBConnection();
        String query = "SELECT *\n" +
                "FROM Film JOIN Programme ON Programme.ProgrammeID = Film.ProgrammeID\n" +
                "WHERE Programme.ProgrammeID NOT IN (\n" +
                "\tSELECT ProgrammeID\n" +
                "\tFROM Profile_Programme\n" +
                "\tWHERE ProfileID = " + p.getProfileID() + "\n" +
                ")";

        Movie[] movies = null;
        try {
            ResultSet rs = conn.runQuery(query);

            rs.last();
            movies = new Movie[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                int ID = rs.getInt("ProgrammeID");
                int duration = rs.getInt("DurationInMinutes");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String language = rs.getString("Language");
                String age = rs.getString("AgeIndication");
                int year = rs.getInt("ReleaseYear");

                Movie m = new Movie(ID, duration, title, genre, language, age, year, 0, 0);
                movies[i] = m;
                i++;
            }

        } catch(Exception e) {
            System.out.println("Failed parsing ResultSet in class AccountRepository, method getUnwatchedMovies()");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        return movies;
    }

    //Get a list of episodes that have not been watched by a given profile from a given series
    public static Episode[] getUnwatchedEpisodes(Profile p, Series s) {
        if(p == null || s == null) {
            throw new IllegalArgumentException("Profile or Series cannot be null!");
        }
        DBConnection conn = new DBConnection();
        String query = "SELECT *\n" +
                "FROM Episode e JOIN Programme p ON p.ProgrammeID = e.ProgrammeID\n" +
                "WHERE SeriesID = " + s.getSeriesID() + " AND p.ProgrammeID NOT IN (\n" +
                "SELECT ProgrammeID\n" +
                "FROM Profile_Programme\n" +
                "WHERE ProfileID = " + p.getProfileID() + ") ORDER BY EpisodeNumber";

        Episode[] episodes = null;
        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();
            episodes = new Episode[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                int ID = rs.getInt("ProgrammeID");
                String number = rs.getString("EpisodeNumber");
                String title = rs.getString("Title");
                int duration = rs.getInt("DurationInMinutes");

                episodes[i] = new Episode(ID, number, title, duration, 0);
                i++;
            }
        } catch(SQLException e) {
            System.out.println("Could not parse ResultSet in AccountRepository, method getUnwatchedEpisodes");
            e.printStackTrace();
        }

        return episodes;
    }

    //Add a programme the the given profile's watchlist with the given progress
    public static void addWatchedProgramme(int profileID, int progress, int programmeID) {
        if(profileID < 0) {
            throw new IllegalArgumentException("profileID cannot be less than 0");
        }

        if(progress < 0) {
            throw new IllegalArgumentException("progress cannot be less than 0");
        }

        if(programmeID < 0) {
            throw new IllegalArgumentException("programmeID cannot be less than 0");
        }

        DBConnection conn = new DBConnection();
        String preQuery = "INSERT INTO Profile_Programme (ProfileID, Progress, ProgrammeID) VALUES " +
                "({0}, {1}, {2})";
        Object[] args = {profileID, progress, programmeID};

        String query = new MessageFormat(preQuery).format(args);

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Update a given programme in the given profile's watchlist with the given progress
    public static void updateWatchedProgramme(int profileID, int progress, int programmeID) {
        if(profileID < 0) {
            throw new IllegalArgumentException("profileID cannot be less than 0");
        }

        if(progress < 0) {
            throw new IllegalArgumentException("progress cannot be less than 0");
        }

        if(programmeID < 0) {
            throw new IllegalArgumentException("programmeID cannot be less than 0");
        }

        DBConnection conn = new DBConnection();
        String preQuery = "UPDATE Profile_Programme SET Progress = {0} WHERE ProfileID = {1} AND ProgrammeID = {2}";
        Object[] args = {progress, profileID, programmeID};

        String query = new MessageFormat(preQuery).format(args);

        conn.runQuery(query);
        conn.closeOperations();
    }

    //When the user updates a watched programme with a progress of 0%, the application will see this as the user wanting
    //to remove the programme from the profile's watchlist. This method will remove the link between the profile and the programme
    public static void removeWatchedProgramme(int profileID, int programmeID) {
        DBConnection conn = new DBConnection();
        String query = "DELETE FROM Profile_Programme WHERE ProfileID = " + profileID + " AND ProgrammeID = " + programmeID;

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Remove a given account
    public static void removeAccount(Account a) {
        DBConnection conn = new DBConnection();
        String query = "DELETE FROM Subscription WHERE SubscriptionID = " + a.getID();

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Remove a given profile
    public static void removeProfile(Profile p) {
        DBConnection conn = new DBConnection();
        String query = "DELETE FROM Profile WHERE ProfileID = " + p.getProfileID();

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Update a profile with the given values
    public static void updateProfile(int pID, int aID, String name, String date) {
        DBConnection conn = new DBConnection();
        Object[] args = {aID, name, date, pID};
        String preQuery = "UPDATE Profile SET SubscriptionID = {0}, ProfileName = \''{1}\'', DateOfBirth = \''{2}\'' \n" +
                "WHERE ProfileID = {3}";
        String query = new MessageFormat(preQuery).format(args);

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Update an account with the given values
    public static void updateAccount(int aID, int houseNo, String name, String street, String additive, String place) {
        DBConnection conn = new DBConnection();
        String preQuery = "UPDATE Subscription SET SubscriberName = \''{0}\'', Street = \''{1}\'', HouseNumber = {2}, " +
                "Additive = \''{3}\'', place = \''{4}\'' WHERE SubscriptionID = {5}";

        Object[] args = {name, street, houseNo, additive, place, aID};

        String query = new MessageFormat(preQuery).format(args);

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Create a new profile
    public static void createProfile(int aID, String name, String date) {
        DBConnection conn = new DBConnection();
        String preQuery = "INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth) VALUES " +
                "({0}, \''{1}\'', \''{2}\'')";

        Object[] args = {aID, name, date};
        String query = new MessageFormat(preQuery).format(args);

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Create a new account
    public static void createAccount(int houseNo, String name, String street, String additive, String place) {
        DBConnection conn = new DBConnection();
        String preQuery = "INSERT INTO Subscription (SubscriberName, Street, HouseNumber, Additive, Place) VALUES " +
                "(\''{0}\'', \''{1}\'', {2}, \''{3}\'', \''{4}\'')";

        Object[] args = {name, street, houseNo, additive, place};
        String query = new MessageFormat(preQuery).format(args);

        conn.runQuery(query);
        conn.closeOperations();
    }


}
