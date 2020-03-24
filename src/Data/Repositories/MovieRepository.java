package Data.Repositories;

import Data.DBConnection;
import Domain.Movie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MovieRepository {

    //Class that handles CRUD operations involving the Movie table

    //Helper method to run a query, made to prevent duplicate code in getAllMovies() and getMoviesByFilter()
    private static List<Movie> getMoviesFromDatabase(String query) {
        List<Movie> movies = new ArrayList<>();

        DBConnection conn = new DBConnection();

        ResultSet rs = conn.runQuery(query);

        //Make a new Movie object for each row in the ResultSet
        try {
            while (rs.next()) {
                int movieID = rs.getInt("ProgrammeID");
                int duration = rs.getInt("DurationInMinutes");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String language = rs.getString("Language");
                String ageIndication = rs.getString("AgeIndication");
                int releaseYear = rs.getInt("ReleaseYear");
                int watched = rs.getInt("peopleWatched");
                int watchers = rs.getInt("totalWatchers");

                Movie m = new Movie(movieID, duration, title, genre, language, ageIndication, releaseYear, watched, watchers);
                movies.add(m);
            }
        } catch(SQLException e) {
            System.out.println("SQLException parsing ResultSet in class MovieRepository");
            e.printStackTrace();
        } finally {
            conn.closeOperations();
        }

        return movies;
    }

    //Get all movies from the database
    public static List<Movie> getAllMovies() {
        String query = "SELECT DISTINCT p.ProgrammeID, p.Title, p.DurationInMinutes, f.Genre, f.Language, f.AgeIndication, f.ReleaseYear, SUM(CASE WHEN Progress = 100 THEN 1 ELSE 0 END) AS peopleWatched, COUNT(pp.Progress) AS totalWatchers\n" +
                "FROM Film f\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = f.ProgrammeID\n" +
                "LEFT JOIN Profile_Programme pp ON pp.ProgrammeID = p.ProgrammeID\n" +
                "GROUP BY p.ProgrammeID, p.Title, p.DurationInMinutes, f.Genre, f.Language, f.AgeIndication, f.ReleaseYear";

        return getMoviesFromDatabase(query);
    }

    //Get movies that contain the given filter
    public static List<Movie> getMoviesByFilter(String filter) {
        String query = "SELECT p.ProgrammeID, p.Title, p.DurationInMinutes, f.Genre, f.Language, f.AgeIndication, f.ReleaseYear, SUM(CASE WHEN Progress = 100 THEN 1 ELSE 0 END) AS peopleWatched, COUNT(pp.Progress) AS totalWatchers\n" +
                "FROM Film f\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = f.ProgrammeID\n" +
                "LEFT JOIN Profile_Programme pp ON pp.ProgrammeID = p.ProgrammeID\n" +
                "WHERE p.Title LIKE '%" + filter + "%' \n" +
                "GROUP BY p.ProgrammeID, p.Title, p.DurationInMinutes, f.Genre, f.Language, f.AgeIndication, f.ReleaseYear, pp.Progress, pp.ProgrammeID";

        return getMoviesFromDatabase(query);
    }

    //Get the longest movie for people under 16 years old
    public static List<Movie> getLongestMovie() {
        String query = "SELECT TOP 1 p.ProgrammeID, p.Title, p.DurationInMinutes, f.Genre, f.Language, f.AgeIndication, f.ReleaseYear, SUM(CASE WHEN Progress = 100 THEN 1 ELSE 0 END) AS peopleWatched, COUNT(pp.Progress) AS totalWatchers\n" +
                "FROM Film f\n" +
                "INNER JOIN Programme p ON p.ProgrammeID = f.ProgrammeID\n" +
                "LEFT JOIN Profile_Programme pp ON pp.ProgrammeID = p.ProgrammeID\n" +
                "WHERE AgeIndication NOT LIKE '16%' AND AgeIndication NOT LIKE '18%'\n" +
                "GROUP BY p.ProgrammeID, p.Title, p.DurationInMinutes, f.Genre, f.Language, f.AgeIndication, f.ReleaseYear, pp.Progress, pp.ProgrammeID\n" +
                "ORDER BY DurationInMinutes DESC";

        return getMoviesFromDatabase(query);
    }

    //Remove the given movie from the database
    public static void removeMovieFromDatabase(Movie m) {
        if(m == null) {
            throw new IllegalArgumentException("Movie with value null cannot be removed from database");
        }

        DBConnection conn = new DBConnection();
        String query = "DELETE FROM Programme WHERE ProgrammeID = " + m.getID();

        conn.runQuery(query);
        conn.closeOperations();
    }

    //Get the genres from the database, used to fill dropdowns and updating/creating movies/series
    public static String[] getGenres() {
        DBConnection conn = new DBConnection();
        String query = "SELECT * FROM Genre";

        String[] genres = null;
        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();
            genres = new String[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                genres[i] = rs.getString("Genre");
                i++;
            }
        } catch(SQLException e) {
            System.out.println("Could not parse ResultSet in MovieRepository, method getGenres()");
        }

        return genres;
    }

    //Get the languages from the database, used to fill dropdowns and updating/creating movies/series
    public static String[] getLanguages() {
        DBConnection conn = new DBConnection();
        String query = "SELECT * FROM Language";

        String[] languages = null;
        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();
            languages = new String[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                languages[i] = rs.getString("Language");
                i++;
            }
        } catch(SQLException e) {
            System.out.println("Could not parse ResultSet in MovieRepository, method getLanguages()");
        }

        return languages;
    }

    //Get the age categories from the database, used to fill dropdowns and updating/creating movies/series
    public static String[] getAgeCategories() {
        DBConnection conn = new DBConnection();
        String query = "SELECT * FROM AgeIndication";

        String[] categories = null;
        try {
            ResultSet rs = conn.runQuery(query);
            rs.last();
            categories = new String[rs.getRow()];

            rs.beforeFirst();
            int i = 0;
            while(rs.next()) {
                categories[i] = rs.getString("AgeIndication");
                i++;
            }
        } catch(SQLException e) {
            System.out.println("Could not parse ResultSet in MovieRepository, method getAgeCategories()");
        }

        return categories;
    }

    //Update the database with the given data
    public static void updateMovieInDatabase(int movieID, int duration, String title, String genre, String language, int releaseYear, String ageIndication) {
        int cYear = Calendar.getInstance().get(Calendar.YEAR);
        if(movieID < 0) {
            throw new IllegalArgumentException("movieID cannot be less than 0");
        }

        if(duration <= 0) {
            throw new IllegalArgumentException("duration cannot be less than or equal to 0");
        }

        if(releaseYear > cYear) {
            throw new IllegalArgumentException("Release year cannot be in the future");
        }
        DBConnection conn = new DBConnection();
        String updateProgramme = "UPDATE Programme\n" +
                "SET DurationInMinutes = " + duration + " , Title = '" + title + "'\n" +
                "WHERE ProgrammeID = " + movieID;

        String updateMovie = "UPDATE Film\n" +
                "SET Genre = '" + genre + "', ReleaseYear = " + releaseYear + ", Language = '" + language + "', AgeIndication = '" + ageIndication + "'\n" +
                "WHERE ProgrammeID = " + movieID;

        conn.runQuery(updateProgramme);
        conn.runQuery(updateMovie);
        conn.closeOperations();
    }

    //Create a new movie with the given data
    public static void addMovieToDatabase(int duration, String title, String genre, String language, int year, String ageIndication) {
        int cYear = Calendar.getInstance().get(Calendar.YEAR);

        if(duration <= 0) {
            throw new IllegalArgumentException("duration cannot be less than or equal to 0");
        }

        if(year > cYear) {
            throw new IllegalArgumentException("Release year cannot be in the future");
        }
        DBConnection conn = new DBConnection();
        Object[] programmeArgs = {title};
        Object[] movieArgs = {title, genre, language, ageIndication};

        String preInsertProgramme = "INSERT INTO Programme (DurationInMinutes, Title) VALUES \n" +
                "(" + duration + ", \''{0}\'')";

        String preInsertMovie = "INSERT INTO Film (ProgrammeID, Genre, Language, ReleaseYear, AgeIndication) VALUES \n" +
                "((SELECT ProgrammeID FROM Programme WHERE Title = \''{0}\'' AND DurationInMinutes = " + duration + "), \n" +
                "\''{1}\'', \''{2}\'', " + year + ",\''{3}\'')";

        String insertProgramme = new MessageFormat(preInsertProgramme).format(programmeArgs);
        String insertMovie = new MessageFormat(preInsertMovie).format(movieArgs);

        conn.runQuery(insertProgramme);
        conn.runQuery(insertMovie);
        conn.closeOperations();
   }
}
