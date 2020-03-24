package Domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class MovieTest {

    @Test
    void testGetRunTimeConvertsDurationCorrectly() {
        Movie[] moviesToTest = {
                new Movie(0, 1, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(1, 10, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(2, 12, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(3, 61, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(4, 70, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(5, 111, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(6, 150, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(7, 200, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(8, 1500, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(9, 1750, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10),
                new Movie(10, 2000, "Movie1", "Actie", "Engels", "12 jaar en ouder", 2000, 10, 10)
        };

        String[] exceptedResults = {
                "00h01m",
                "00h10m",
                "00h12m",
                "01h01m",
                "01h10m",
                "01h51m",
                "02h30m",
                "03h20m",
                "25h00m",
                "29h10m",
                "33h20m"
        };

        int i = 0;
        for(Movie m : moviesToTest) {
            Assertions.assertEquals(exceptedResults[i], m.getRunTime());
            i++;
        }
    }

    @Test
    void testGetPercentageWatchedReturnsCorrectResult() {
        Movie[] testCases = {
                new Movie(0, 1, "m", "a", "a", "a", 2000, 5, 10),
                new Movie(0, 1, "m", "a", "a", "a", 2000, 12, 100),
                new Movie(0, 1, "m", "a", "a", "a", 2000, 0, 0),
                new Movie(0, 1, "m", "a", "a", "a", 2000, 25, 50),
                new Movie(0, 1, "m", "a", "a", "a", 2000, 12, 88)
        };

        String[] exceptedResults = {
                "50,0%",
                "12,0%",
                "0%",
                "50,0%",
                "13,6%"
        };

        int i = 0;
        for(Movie m : testCases) {
            Assertions.assertEquals(exceptedResults[i], m.getPercentageWatched());
            i++;
        }
    }
    @Test
    void testMovieIllegalArgumentsAreHandled() {
        Assertions.assertThrows(IllegalArgumentException.class, this::createMovieWithIllegalArgumentsOne);
        Assertions.assertThrows(IllegalArgumentException.class, this::createMovieWithIllegalArgumentsTwo);
        Assertions.assertThrows(IllegalArgumentException.class, this::createMovieWithIllegalArgumentsThree);
        Assertions.assertThrows(IllegalArgumentException.class, this::createMovieWithIllegalArgumentsFour);
        Assertions.assertThrows(IllegalArgumentException.class, this::createMovieWithIllegalArgumentsFive);
        Assertions.assertThrows(IllegalArgumentException.class, this::createMovieWithIllegalArgumentsSix);
    }

    private void createMovieWithIllegalArgumentsOne() {
        new Movie(0, 1, "m", "a", "a", "a", 2000, 10, 5);
    }

    private void createMovieWithIllegalArgumentsTwo() {
        new Movie(0, -1, "m", "a", "a", "a", 2000, 5, 10);
    }

    private void createMovieWithIllegalArgumentsThree() {
        new Movie(0, 0, "m", "a", "a", "a", 2000, 5, 10);
    }

    private void createMovieWithIllegalArgumentsFour() {
        new Movie(0, 1, "m", "a", "a", "a", 2000, -5, 10);
    }

    private void createMovieWithIllegalArgumentsFive() {
        new Movie(0, 1, "m", "a", "a", "a", 2000, 5, -10);
    }

    private void createMovieWithIllegalArgumentsSix() {
        new Movie(0, 1, "m", "a", "a", "a", 2000, -10, -5);
    }


}