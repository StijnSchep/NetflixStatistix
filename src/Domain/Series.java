package Domain;

public class Series {

    /* Holds information for 1 series */

    private int seriesID, averageWatchTime;
    private String title, genre, ageIndication;

    public Series(int ID, String title, String genre, String age, int averageWatchTime) {
        this.seriesID = ID;
        this.title = title;
        this.genre = genre;
        this.ageIndication = age;
        this.averageWatchTime = averageWatchTime;
    }

    public int getSeriesID() { return seriesID; }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getAgeIndication() {
        return ageIndication;
    }

    public int getAverageWatchTime() { return averageWatchTime; }

    public String toString() {
        return title;
    }
}
