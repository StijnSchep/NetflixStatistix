package Data.Repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {

    @Test
    void testRemoveMovieFromDatabaseThrowsIllegalArgumentException() {
        try {
            MovieRepository.removeMovieFromDatabase(null);
            fail("Method removeMovieFromDatabase should throw IllegalArgumentException when input is null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testUpdateMovieInDatabaseThrowsIllegalArgumentException() {
        int cYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            MovieRepository.updateMovieInDatabase(-1, 1, "invalidID", "genre", "language", 2000, "age");
            fail("Method updateMovieInDatabase should throw IllegalArgumentException with input movieID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            MovieRepository.updateMovieInDatabase(1, -1, "invalidDuration", "genre", "language", 2000, "age");
            fail("Method updateMovieInDatabase should throw IllegalArgumentException with input duration = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            MovieRepository.updateMovieInDatabase(1, 1, "invalidReleaseYear", "genre", "language", cYear + 5, "age");
            fail("Method updateMovieInDatabase should throw IllegalArgumentException with input releaseYear in the future");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

    }

    @Test
    void testAddMovieToDatabaseThrowsIllegalArgumentException() {
        int cYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            MovieRepository.addMovieToDatabase(-1, "invalidDuration", "genre", "language", 2000, "age");
            fail("Method addMovieToDatabase should throw IllegalArgumentException with input duration = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            MovieRepository.addMovieToDatabase(1, "invalidReleaseYear", "genre", "language", cYear + 5, "age");
            fail("Method addMovieToDatabase should throw IllegalArgumentException with input releaseYear in the future");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }
}