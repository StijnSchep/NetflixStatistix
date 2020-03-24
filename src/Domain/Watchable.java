package Domain;

public abstract class Watchable {
    /*
        Abstract class for objects that have a duration and a title. Extended by Episode and Movie
     */

    private int ID, duration;
    private String title;

    Watchable(int ID, int duration, String title) {
        if(ID < 0) {
            throw new IllegalArgumentException("ID cannot be less than 0");
        }

        if(duration <= 0) {
            throw new IllegalArgumentException("Duration cannot be 0 or less");
        }

        if(title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        this.ID = ID;
        this.duration = duration;
        this.title = title;
    }

    //Given a duration in minutes, turns the integer into a string with a runtime, I.E. 90 becomes "01h30m"
    String getRunTime() {
        String hours = String.valueOf(duration / 60);
        String minutes = String.valueOf(duration % 60);

        //Adjust notation to fit the form --h --m
        if(duration / 60 < 10) { hours = 0 + hours; }
        if(duration % 60 < 10) { minutes = 0 + minutes; }
        return hours + "h" + minutes + "m";
    }

    public String getTitle() {
        return title;
    }

    //In MoviePanel, the title is displayed in all upper case
    public String getTitleUpperCase() { return title.toUpperCase();}

    public int getID() {
        return ID;
    }

    public int getDuration() {
        return duration;
    }
}
