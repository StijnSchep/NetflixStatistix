package Data.Repositories;

import Domain.Series;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpisodeRepositoryTest {

    @Test
    void testGetEpisodesByArgsThrowsIllegalArgumentException() {
        try {
            EpisodeRepository.getEpisodesByArgs(-1, "invalid");
            fail("Method getEpisodesByArgs should throw IllegalArgumentException with input ID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            EpisodeRepository.getEpisodesByArgs(-1, null);
            fail("Method getEpisodesByArgs should throw IllegalArgumentException with input account = null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testRemoveEpisodeFromDatabaseThrowsIllegalArgumentException() {
        try {
            EpisodeRepository.removeEpisodeFromDatabase(null);
            fail("Method removeEpisodeFromDatabase should throw IllegalArgumentException with input episode = null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testUpdateEpisodeInDatabaseThrowsIllegalArgumentException() {
        try{
            EpisodeRepository.updateEpisodeInDatabase(-1, "S01E01", "invalid", 10);
            fail("Method updateEpisodeInDatbase should throw IllegalArgumentException with input ID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try{
            EpisodeRepository.updateEpisodeInDatabase(1, "S01E1", "invalid", 10);
            fail("Method updateEpisodeInDatbase should throw IllegalArgumentException with input episodeNumber = -S01E1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try{
            EpisodeRepository.updateEpisodeInDatabase(1, "S01E01", "invalid", -10);
            fail("Method updateEpisodeInDatbase should throw IllegalArgumentException with input duration = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testAddEpisodeToDatabaseThrowsIllegalArgumentException() {
        Series invalidSeries = new Series(-1, "invalid", "genre", "age", 5);
        Series validSeries = new Series(1, "valid", "genre", "age", 5);

        try {
            EpisodeRepository.addEpisodeToDatabase(invalidSeries, "S01E01", 5, "invalidseries");
            fail("Method addEpisodeToDatabase should throw IllegalArgumentException when method is given a series with invalid ID");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            EpisodeRepository.addEpisodeToDatabase(validSeries, "S01E1", 5, "invalidEpisodeNumber");
            fail("Method addEpisodeToDatabase should throw IllegalArgumentException when method is given an invalid episode number");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            EpisodeRepository.addEpisodeToDatabase(validSeries, "S01E01", -5, "invalidduration");
            fail("Method addEpisodeToDatabase should throw IllegalArgumentException when method is given an invalid duration");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            EpisodeRepository.addEpisodeToDatabase(null, "S01E01", -5, "invalidSeries");
            fail("Method addEpisodeToDatabase should throw IllegalArgumentException when method is given a series with value null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }
}