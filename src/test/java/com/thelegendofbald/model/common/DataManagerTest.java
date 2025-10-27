package com.thelegendofbald.model.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataManagerTest {

    private static final String SAVE_FILE_DIRECTORY = "game_data" + File.separator + "runs";
    private static final String SAVE_FILE_PATH = SAVE_FILE_DIRECTORY + File.separator + "users_data.yml";

    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        dataManager = new DataManager();
        // Clean up the save file before each test
        final File saveFile = new File(SAVE_FILE_PATH);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up the save file after each test
        final File saveFile = new File(SAVE_FILE_PATH);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    @Test
    void testLoadGameRunsReturnsEmptyListIfFileDoesNotExist() {
        final List<GameRun> runs = dataManager.loadGameRuns();
        assertNotNull(runs);
        assertTrue(runs.isEmpty());
    }

    @Test
    void testSaveAndLoadGameRun() throws IOException {
        final GameRun run = new GameRun("TestUser", new Timer.TimeData(1, 2, 3));
        dataManager.saveGameRun(run);

        final List<GameRun> runs = dataManager.loadGameRuns();
        assertNotNull(runs);
        assertFalse(runs.isEmpty());
        final GameRun loaded = runs.get(runs.size() - 1);
        assertEquals("TestUser", loaded.name());
        assertEquals(1, loaded.timedata().hours());
        assertEquals(2, loaded.timedata().minutes());
        assertEquals(3, loaded.timedata().seconds());
    }

    @Test
    void testSaveMultipleGameRuns() throws IOException {
        final int run1Hours = 0;
        final int run1Minutes = 1;
        final int run1Seconds = 2;
        final int run2Hours = 3;
        final int run2Minutes = 4;
        final int run2Seconds = 5;

        final GameRun run1 = new GameRun("User1", new Timer.TimeData(run1Hours, run1Minutes, run1Seconds));
        final GameRun run2 = new GameRun("User2", new Timer.TimeData(run2Hours, run2Minutes, run2Seconds));

        dataManager.saveGameRun(run1);
        dataManager.saveGameRun(run2);

        final List<GameRun> runs = dataManager.loadGameRuns();
        assertNotNull(runs);
        assertTrue(runs.size() >= 2);

        final GameRun last = runs.get(runs.size() - 1);
        assertEquals("User2", last.name());
        assertEquals(run2Hours, last.timedata().hours());
        assertEquals(run2Minutes, last.timedata().minutes());
        assertEquals(run2Seconds, last.timedata().seconds());
    }
}
