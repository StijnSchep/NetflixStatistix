package Data.Repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesRepositoryTest {

    @Test
    void testGetSeriesByArgsThrowsIllegalArgumentException() {
        try {
            SeriesRepository.getSeriesByArgs(-1, "");
            fail("Method getSeriesByArgs should throw IllegalArgumentException when given an invalid ID");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            SeriesRepository.getSeriesByArgs(1, null);
            fail("Method getSeriesByArgs should throw IllegalArgumentException with input account == null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testRemoveSeriesFromDatabaseThrowsIllegalArgumentException() {
        try {
            SeriesRepository.removeSeriesFromDatabase(null);
            fail("Method removeSeriesFromDatabase should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testUpdateSeriesInDatabaseThrowsIllegalArgumentException() {
        try {
            SeriesRepository.updateSeriesInDatabase(-1, "invalidID", "genre", "age");
            fail("Method updateSeriesInDatabase should throw IllegalArgumentException with input ID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            SeriesRepository.updateSeriesInDatabase(1, "", "genre", "age");
            fail("Method updateSeriesInDatabase should throw IllegalArgumentException when title is empty");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            SeriesRepository.updateSeriesInDatabase(1, "invalidGenre", "", "age");
            fail("Method updateSeriesInDatabase should throw IllegalArgumentException when genre is empty");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            SeriesRepository.updateSeriesInDatabase(1, "invalidAgeCategory", "genre", "");
            fail("Method updateSeriesInDatabase should throw IllegalArgumentException when ageCategory is empty");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testAddSeriesToDatabaseThrowsIllegalArgumentException() {
        try {
            SeriesRepository.addSeriesToDatabase("", "genre", "age");
            fail("Method addSeriesToDatabase should throw IllegalArgumentException when title is empty");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            SeriesRepository.addSeriesToDatabase("invalidGenre", "", "age");
            fail("Method addSeriesToDatabase should throw IllegalArgumentException when genre is empty");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            SeriesRepository.addSeriesToDatabase("invalidAgeCategory", "genre", "");
            fail("Method addSeriesToDatabase should throw IllegalArgumentException when ageCategory is empty");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }
}