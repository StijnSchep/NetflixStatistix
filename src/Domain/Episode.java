package Domain;

public class Episode extends Watchable {
    /*
        Holds information for an episode. Is a watchable object and gets access to the getRunTime() method from Watchable
        An episode can also have a percentage watched, this value holds progress
     */

    private String episodeNumber;
    private int percentageWatched;

    public Episode(int ID, String episodeNumber, String title, int duration, int percentageWatched) {
        super(ID, duration, title);

        //Episode number should match S01E01, S01E100, S100E01 or S100E100
        if(!episodeNumber.matches("S[0-9][0-9][0-9]?E[0-9][0-9][0-9]?")) {
            throw new IllegalArgumentException("EpisodeNumber does not match correct form of S--E--, S---E--, S--E--- or S---E---");
        }

        if(percentageWatched < 0) {
            throw new IllegalArgumentException("Percentage watched cannot be less than 0");
        }
        this.episodeNumber = episodeNumber;
        this.percentageWatched = percentageWatched;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public int getPercentageWatched() { return percentageWatched; }

    public String toString() {
        return episodeNumber + " - " + super.getTitle();
    }
}
