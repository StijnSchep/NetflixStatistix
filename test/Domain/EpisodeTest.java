package Domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpisodeTest {

    @Test
    void EpisodeCreationThrowsIllegalArgumentException() {
        try {
            Episode e = new Episode(-1, "S01E01", "Invalid", 10, 10);
            fail("Episode constructor should throw IllegalArgumentException with input ID = -1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            Episode e = new Episode(1, "S1E1", "Invalid", 10, 10);
            fail("Episode constructor should throw IllegalArgumentException with input episodeNumber = S1E1");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            Episode e = new Episode(1, "S01E01", null, 10, 10);
            fail("Episode constructor should throw IllegalArgumentException with input title = null");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            Episode e = new Episode(1, "S01E01", "Invalid", -10, 10);
            fail("Episode constructor should throw IllegalArgumentException with input duration = -10");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }

        try {
            Episode e = new Episode(1, "S01E01", "Invalid", 10, -10);
            fail("Episode constructor should throw IllegalArgumentException with input percentageWatched = -10");
        } catch(IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }


}