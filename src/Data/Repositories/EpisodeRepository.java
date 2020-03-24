package Data.Repositories;

import Data.DBConnection;
import Domain.Episode;
import Domain.Series;

import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodeRepository {
    /*
        Holds queries regarding episodes
     */

    //Get all episodes by a given seriesID and account name. If account name is empty, query will base results on all
    //accounts. if account name is filled in, query will base results on accounts that contain the account string
    public static List<Episode> getEpisodesByArgs(int seriesID, String account) {
        if(seriesID < 0) {
            throw new IllegalArgumentException("ID cannot be less than 0");
        }

        if(account == null) {
            throw new IllegalArgumentException("account name cannot be null");
        }

        List<Episode> episodes = new ArrayList<>();

        String query = "SELECT ProgrammeID, EpisodeNumber, DurationInMinutes, Title, MAX(AvgProgress) AS AvgProgress\n" +
                "FROM (\n" +
                "SELECT e.ProgrammeID, e.EpisodeNumber, p.DurationInMinutes, p.Title, AVG(pp.Progress) as AvgProgress\n" +
                "FROM Episode e\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = e.ProgrammeID\n" +
                "INNER JOIN Series s ON s.SeriesID = e.SeriesID\n" +
                "LEFT JOIN Profile_Programme pp ON pp.ProgrammeID = p.ProgrammeID\n" +
                "LEFT JOIN Profile pr ON pr.ProfileID = pp.ProfileID\n" +
                "LEFT JOIN Subscription su ON su.SubscriptionID = pr.SubscriptionID\n" +
                "WHERE s.SeriesID = " + seriesID + " AND (su.SubscriberName LIKE '%" + account + "%' OR su.SubscriberName IS NULL)\n" +
                "GROUP BY e.ProgrammeID, e.EpisodeNumber, p.DurationInMinutes, p.Title, s.Title\n" +
                "\n" +
                "UNION\n" +
                "\n" +
                "SELECT e.ProgrammeID, e.EpisodeNumber, p.DurationInMinutes, p.Title, NULL\n" +
                "FROM Episode e\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = e.ProgrammeID\n" +
                "INNER JOIN Series s ON s.SeriesID = e.SeriesID\n" +
                "LEFT JOIN Profile_Programme pp ON pp.ProgrammeID IS NULL\n" +
                "LEFT JOIN Profile pr ON pr.ProfileID = pp.ProfileID\n" +
                "LEFT JOIN Subscription su ON su.SubscriptionID = pr.SubscriptionID\n" +
                "WHERE s.SeriesID = " + seriesID + " \n" +
                ") AS result\n" +
                "GROUP BY ProgrammeID, EpisodeNumber, DurationInMinutes, Title\n ORDER BY EpisodeNumber";

        DBConnection conn = new DBConnection();
        ResultSet rs = conn.runQuery(query);

        try {
            while(rs.next()) {
                int sID = rs.getInt("ProgrammeID");
                String number = rs.getString("EpisodeNumber");
                String title = rs.getString("Title");
                int duration = rs.getInt("DurationInMinutes");
                int percentageWatched = rs.getInt("AvgProgress");

                episodes.add(new Episode(sID, number, title, duration, percentageWatched));
            }
        } catch(Exception e) {
            System.out.println("Exception parsing ResultSet in class SeriesRepository");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        return episodes;
    }

    //Remove a given episode from the database
    public static void removeEpisodeFromDatabase(Episode e) {
        if(e == null) {
            throw new IllegalArgumentException("Episode cannot be null");
        }
        DBConnection conn = new DBConnection();
        String query = "DELETE FROM Programme WHERE ProgrammeID = " + e.getID();
        conn.runQuery(query);
        conn.closeOperations();
    }

    //Update the database with the given data
    public static void updateEpisodeInDatabase(int programmeID, String number, String title, int duration) {

        if(programmeID < 0) {
            throw new IllegalArgumentException("programmeID cannot be less than 0");
        }

        if(!number.matches("S[0-9][0-9][0-9]?E[0-9][0-9][0-9]?")) {
            throw new IllegalArgumentException("EpisodeNumber should match the form S--E--, S---E--, S--E--- or S---E---");
        }

        if(duration <= 0) {
            throw new IllegalArgumentException("duration cannot be negative or 0");
        }

        Object[] episodeArgs = {number, programmeID};
        Object[] programmeArgs = {duration, title, programmeID};

        String preUpdateEpisode = "UPDATE Episode\n" +
                "SET EpisodeNumber = ''{0}'' WHERE ProgrammeID = {1}";
        String preUpdateProgramme = "UPDATE Programme\n" +
                "SET DurationInMinutes = {0}, Title = ''{1}'' WHERE ProgrammeID = {2}";
        String updateEpisode = new MessageFormat(preUpdateEpisode).format(episodeArgs);
        String updateProgramme = new MessageFormat(preUpdateProgramme).format(programmeArgs);

        DBConnection conn = new DBConnection();
        conn.runQuery(updateProgramme);
        conn.runQuery(updateEpisode);
        conn.closeOperations();
    }

    //Add an episode to the database with the given data
    public static void addEpisodeToDatabase(Series s, String episodeNumber, int duration, String title) {
        if(s == null || s.getSeriesID() < 0) {
            throw new IllegalArgumentException("Series should be valid");
        }

        if(!episodeNumber.matches("S[0-9][0-9][0-9]?E[0-9][0-9][0-9]?")) {
            throw new IllegalArgumentException("EpisodeNumber should match the form S--E--, S---E--, S--E--- or S---E---");
        }

        if(duration <= 0) {
            throw new IllegalArgumentException("Duration cannot be less than or equal to 0");
        }
        Object[] programmeArgs = {duration, title};
        Object[] episodeArgs = {duration, title, s.getSeriesID(), episodeNumber};

        String preInsertProgramme = "INSERT INTO Programme (DurationInMinutes, Title) VALUES \n" +
                "({0}, ''{1}'')";
        String preInsertEpisode = "INSERT INTO Episode (ProgrammeID, SeriesID, EpisodeNumber) VALUES \n" +
                "((SELECT ProgrammeID FROM Programme WHERE DurationInMinutes = {0} AND Title = ''{1}'')," +
                "{2}, ''{3}'')";
        String insertProgramme = new MessageFormat(preInsertProgramme).format(programmeArgs);
        String insertEpisode = new MessageFormat(preInsertEpisode).format(episodeArgs);

        DBConnection conn = new DBConnection();
        conn.runQuery(insertProgramme);
        conn.runQuery(insertEpisode);
        conn.closeOperations();
    }

}
