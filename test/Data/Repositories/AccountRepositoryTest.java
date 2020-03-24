package Data.Repositories;


import Domain.Profile;
import Domain.Series;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.fail;

class AccountRepositoryTest {


    @Test
    void testGetProfilesByAccNameThrowsIllegalArgumentException() {
        try {
            AccountRepository.getProfilesByAccName(null);
            fail("Method getProfilesByAccName should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }

        try {
            AccountRepository.getProfilesByAccName("");
            fail("Method getProfilesByAccName should throw IllegalArgumentException with empty input String");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }
    }

    @Test
    void testGetWatchedSeriesThrowsIllegalArgumentException() {
        try {
            AccountRepository.getWatchedSeries(null);
            fail("Method getWatchedSeries should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }
    }

    @Test
    void testGetWatchedMoviesThrowsIllegalArgumentException() {
        try {
            AccountRepository.getWatchedMovies(null);
            fail("Method getWatchedMovies should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }
    }

    @Test
    void testGetWatchedEpisodesThrowsIllegalArgumentException() {
        Profile validProfile = new Profile(1, 1, "valid", new Date());
        Profile invalidProfile = null;

        Series validSeries = new Series(1, "valid", "genre", "age", 50);
        Series invalidSeries = null;

        try {
            AccountRepository.getWatchedEpisodes(validProfile, invalidSeries);
            fail("Method getWatchedEpisodes should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }

        try {
            AccountRepository.getWatchedEpisodes(invalidProfile, validSeries);
            fail("Method getWatchedEpisodes should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }

        try {
            AccountRepository.getWatchedEpisodes(invalidProfile, invalidSeries);
            fail("Method getWatchedEpisodes should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }

        AccountRepository.getWatchedEpisodes(validProfile, validSeries);
    }

    @Test
    void testGetUnwatchedMoviesThrowsIllegalArgumentException() {
        Profile validProfile = new Profile(1, 1, "valid", new Date());
        Profile invalidProfile = null;

        try {
            AccountRepository.getUnwatchedMovies(invalidProfile);
            fail("Method getUnwatchedMovies should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        AccountRepository.getUnwatchedMovies(validProfile);
    }

    @Test
    void testGetUnwatchedEpisodesThrowsIllegalArgumentException() {
        Profile validProfile = new Profile(1, 1, "valid", new Date());
        Profile invalidProfile = null;

        Series validSeries = new Series(1, "valid", "genre", "age", 50);
        Series invalidSeries = null;

        try {
            AccountRepository.getUnwatchedEpisodes(validProfile, invalidSeries);
            fail("Method getUnwatchedEpisodes should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }

        try {
            AccountRepository.getUnwatchedEpisodes(invalidProfile, validSeries);
            fail("Method getUnwatchedEpisodes should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }

        try {
            AccountRepository.getUnwatchedEpisodes(invalidProfile, invalidSeries);
            fail("Method getUnwatchedEpisodes should throw IllegalArgumentException with input null");
        } catch(IllegalArgumentException e) { Assertions.assertTrue(true); }

        AccountRepository.getUnwatchedEpisodes(validProfile, validSeries);
    }

    @Test
    void testAddWatchedProgrammeThrowsIllegalArgumentException() {
        try {
            AccountRepository.addWatchedProgramme(-1, 10, 10);
            fail("Method addWatchedProgramme should throw IllegalArgumentException with input profileID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            AccountRepository.addWatchedProgramme(1, -1, 10);
            fail("Method addWatchedProgramme should throw IllegalArgumentException with input progress = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            AccountRepository.addWatchedProgramme(1, 10, -1);
            fail("Method addWatchedProgramme should throw IllegalArgumentException with input programmeID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testUpdateWatchedProgrammeThrowsIllegalArgumentException() {
        try {
            AccountRepository.updateWatchedProgramme(-1, 10, 10);
            fail("Method updateWatchedProgramme should throw IllegalArgumentException with input profileID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            AccountRepository.updateWatchedProgramme(1, -1, 10);
            fail("Method updateWatchedProgramme should throw IllegalArgumentException with input progress = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            AccountRepository.updateWatchedProgramme(1, 10, -1);
            fail("Method updateWatchedProgramme should throw IllegalArgumentException with input programmeID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }
}