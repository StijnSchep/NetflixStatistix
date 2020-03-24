package Domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Movie extends Watchable {
    /*
        Holds information for a movie
        Variables amountOfTimesWatched and totalWatchers are used by MoviePanel to display statistics
        amountOfTimesWatched: The amount of people that have a progress of 100% on the movie
        totalWatchers: Total amount of people that have watched the movie at any progress

        Also used by AccountPanel, but amountOfTimesWatched and totalWatchers are only used to store progress
     */

    private int releaseYear, amountOfTimesWatched, totalWatchers;
    private String genre, language, ageIndication;

    public Movie(int ID, int duration, String title, String genre, String language,
                 String age, int releaseYear, int watched, int watchers)
    {
        super(ID, duration, title);
        this.genre = genre;
        this.language = language;
        this.ageIndication = age;
        this.releaseYear = releaseYear;
        this.amountOfTimesWatched = watched;
        this.totalWatchers = watchers;

        //Watched holds the amount of people that have watched the movie for 100%
        //Watchers holds the amount of people that have watched the movie at any percentage
        if(watched > watchers) {
            throw new IllegalArgumentException("Total watchers cannot be smaller than the amount of people that have watched the movie for 100%");
        }

        if(duration <= 0) {
            throw new IllegalArgumentException("The duration of a movie cannot be negative or zero");
        }

        if(watchers < 0 || watched < 0) {
            throw new IllegalArgumentException("The amount of watchers cannot be negative");
        }
    }

    public String getPercentageWatched() {
        if(totalWatchers == 0) {
            return "0%";
        }

        double result = (double) amountOfTimesWatched / totalWatchers;
        result *= 100;
        NumberFormat formatter = new DecimalFormat("#0.0");
        return formatter.format(result) + "%";
    }

    public String getMovieInformation() {
        return releaseYear + "    " + getRunTime() + "    " + language + "    " + ageIndication + "    " + genre;
    }

    public int getWatchAmount() {
        return amountOfTimesWatched;
    }

    public int getTotalWatchers() { return totalWatchers; }

    public String getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public String getAgeIndication() {
        return ageIndication;
    }

    public int getReleaseYear() { return releaseYear; }

    public String toString() {
        return super.getTitle();
    }
}