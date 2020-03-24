package Data.Repositories;

import Data.DBConnection;
import Domain.Series;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class SeriesRepository {
    //Class that handles CRUD operations involving the Series table

    //Get series information from one series with a given seriesID and an account string
    //Results include percentage watched, if account name is empty, percentage is based on all accounts
    //If account name is filled in, percentage watched is based on accounts that contain the account String
    public static Series getSeriesByArgs(int ID, String account) {
        if(ID < 0) {
            throw new IllegalArgumentException("seriesID cannot be less than 0");
        }

        if(account == null) {
            throw new IllegalArgumentException("accountname cannot be null");
        }
        DBConnection conn = new DBConnection();
        String query = "SELECT SeriesID, Title, Genre, AgeIndication, AVG(AvgProgress) AS AvgProgress    \n" +
                "FROM (    \n" +
                "     SELECT SeriesID, Title, Genre, AgeIndication, eTitle, MAX(AvgProgress) AS AvgProgress    \n" +
                "     FROM (    \n" +
                "          SELECT s.SeriesID, s.Title, Genre, AgeIndication, p.Title AS eTitle, AVG(pp.Progress) AS AvgProgress    \n" +
                "          FROM Series s    \n" +
                "          INNER JOIN Episode e ON e.SeriesID = s.SeriesID    \n" +
                "          INNER JOIN Programme p ON p.ProgrammeID = e.ProgrammeID    \n" +
                "          INNER JOIN Profile_Programme pp ON pp.ProgrammeID = p.ProgrammeID    \n" +
                "          INNER JOIN Profile pr ON pr.ProfileID = pp.ProfileID    \n" +
                "          INNER JOIN Subscription su ON su.SubscriptionID = pr.SubscriptionID    \n" +
                "          WHERE s.SeriesID = " + ID + " AND su.SubscriberName LIKE '%" + account + "%'    \n" +
                "          GROUP BY s.SeriesID, s.Title, Genre, AgeIndication, p.Title    \n" +
                "                     \n" +
                "          UNION    \n" +
                "                     \n" +
                "          Select s.SeriesID, s.Title, Genre, AgeIndication, p.Title, 0    \n" +
                "          FROM Series s    \n" +
                "          INNER JOIN Episode e ON e.SeriesID = s.SeriesID    \n" +
                "          INNER JOIN Programme p ON p.ProgrammeID = e.ProgrammeID    \n" +
                "          WHERE s.SeriesID = " + ID + "    \n" +
                "\n" +
                "\t\t  UNION\n" +
                "\n" +
                "\t\t  Select s.SeriesID, s.Title, Genre, AgeIndication, null, null\n" +
                "\t\t  FROM Series s\n" +
                "\t\t  WHERE s.SeriesID = " + ID + "\n" +
                "      ) AS result    \n" +
                "      GROUP BY SeriesID, Title, Genre, AgeIndication, eTitle    \n" +
                ") AS result    \n" +
                "GROUP BY SeriesID, Title, Genre, AgeIndication";

        ResultSet rs = conn.runQuery(query);

        return retrieveSeries(rs, conn);
    }

    //Get all series in the database
    public static Series[] getAllSeries() {
        DBConnection conn = new DBConnection();
        String query = "SELECT * FROM Series";

        Series[] series = null;
        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();
            series = new Series[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                int ID = rs.getInt("SeriesID");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String age = rs.getString("AgeIndication");

                series[i] = new Series(ID, title, genre, age, 0);
                i++;
            }
        } catch(SQLException e) {
            System.out.println("Could not parse ResultSet in SeriesRepository, method getAllSeries()");
            e.printStackTrace();
        }

        return series;
    }

    //Used by getSeriesByArgs() to get the Series object from the ResultSet
    private static Series retrieveSeries(ResultSet rs, DBConnection conn) {
        Series series = new Series(0, "Lege serie (geen serie gevonden)", "", "", 0);
        try {
            while (rs.next()) {
                int ID = rs.getInt("SeriesID");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String age = rs.getString("AgeIndication");
                int averageWatchTime = rs.getInt("AvgProgress");

                series = new Series(ID, title, genre, age, averageWatchTime);
            }
        } catch (Exception e) {
            System.out.println("Exception parsing ResultSet in class SeriesRepository");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        return series;
    }

    //Remove the given series from the database
    public static void removeSeriesFromDatabase(Series s) {
        if(s == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        DBConnection conn = new DBConnection();
        String query = "DELETE FROM Series WHERE SeriesID = " + s.getSeriesID();

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Update the database with the given data
    public static void updateSeriesInDatabase(int ID, String title, String genre, String age) {
        if(ID < 0) { throw new IllegalArgumentException("ID cannot be less than 0"); }
        if(title.equals("")) { throw new IllegalArgumentException("Title cannot be empty"); }
        if(genre.equals("")) { throw new IllegalArgumentException("Genre cannot be empty"); }
        if(age.equals("")) { throw new IllegalArgumentException("Age cannot be empty"); }

        Object[] args = {title, genre, age, ID};

        String preQuery = "UPDATE Series \n" +
                "SET Title = \''{0}\'', Genre = \''{1}\'', AgeIndication = \''{2}\''\n" +
                "WHERE SeriesID = {3}";
        String query = new MessageFormat(preQuery).format(args);

        DBConnection conn = new DBConnection();
        conn.runQuery(query);
        conn.closeOperations();
    }

    //Add a series to the database with the given data
    public static void addSeriesToDatabase(String title, String genre, String age) {
        if(title.equals("")) { throw new IllegalArgumentException("Title cannot be empty"); }
        if(genre.equals("")) { throw new IllegalArgumentException("Genre cannot be empty"); }
        if(age.equals("")) { throw new IllegalArgumentException("Age cannot be empty"); }

        Object[] args = {title, genre, age};

        String preQuery = "INSERT INTO Series (Title, Genre, AgeIndication) VALUES \n" +
                "(\''{0}\'', \''{1}\'', \''{2}\'')";
        String query = new MessageFormat(preQuery).format(args);

        DBConnection conn = new DBConnection();
        conn.runQuery(query);
        conn.closeOperations();
    }

}
