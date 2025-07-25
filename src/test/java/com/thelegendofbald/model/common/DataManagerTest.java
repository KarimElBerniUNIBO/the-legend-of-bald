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

    private DataManager dataManager;
    private static final String SAVE_FILE_DIRECTORY = "game_data" + File.separator + "runs";
    private static final String SAVE_FILE_PATH = SAVE_FILE_DIRECTORY + File.separator + "users_data.yml";

    @BeforeEach
    void setUp() {
        dataManager = new DataManager();
        // Clean up the save file before each test
        File saveFile = new File(SAVE_FILE_PATH);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up the save file after each test
        File saveFile = new File(SAVE_FILE_PATH);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    @Test
    void testLoadGameRunsReturnsEmptyListIfFileDoesNotExist() {
        List<GameRun> runs = dataManager.loadGameRuns();
        assertNotNull(runs);
        assertTrue(runs.isEmpty());
    }

    @Test
    void testSaveAndLoadGameRun() throws IOException {
        GameRun run = new GameRun("TestUser", new Timer.TimeData(1, 2, 3));
        dataManager.saveGameRun(run);

        List<GameRun> runs = dataManager.loadGameRuns();
        assertNotNull(runs);
        assertFalse(runs.isEmpty());
        GameRun loaded = runs.get(runs.size() - 1);
        assertEquals("TestUser", loaded.name());
        assertEquals(1, loaded.timedata().hours());
        assertEquals(2, loaded.timedata().minutes());
        assertEquals(3, loaded.timedata().seconds());
    }

    @Test
    void testSaveMultipleGameRuns() throws IOException {
        GameRun run1 = new GameRun("User1", new Timer.TimeData(0, 1, 2));
        GameRun run2 = new GameRun("User2", new Timer.TimeData(3, 4, 5));
        dataManager.saveGameRun(run1);
        dataManager.saveGameRun(run2);

        List<GameRun> runs = dataManager.loadGameRuns();
        assertNotNull(runs);
        assertTrue(runs.size() >= 2);

        GameRun last = runs.get(runs.size() - 1);
        assertEquals("User2", last.name());
        assertEquals(3, last.timedata().hours());
        assertEquals(4, last.timedata().minutes());
        assertEquals(5, last.timedata().seconds());
    }
}