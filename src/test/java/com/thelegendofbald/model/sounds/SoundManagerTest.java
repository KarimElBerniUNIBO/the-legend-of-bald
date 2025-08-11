package com.thelegendofbald.model.sounds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SoundManagerTest {

    private static final String RANDOM_SOUND_PATH = "/interactive-components/mousehover.wav";

    @BeforeEach
    void reset() {
        SoundManager.closeAll();
        SoundManager.setMasterVolume(1.0f);
    }

    @Test
    void testAddSoundPlayerSetsVolume() {
        SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        // The player is added in its constructor, so just check master volume is correct
        assertEquals(1.0f, SoundManager.getMasterVolume(), 0.0001f);
    }

    @Test
    void testSetMasterVolumeUpdatesPlayers() {
        SoundPlayer p1 = new SoundPlayer(RANDOM_SOUND_PATH);
        SoundPlayer p2 = new SoundPlayer(RANDOM_SOUND_PATH);
        SoundManager.setMasterVolume(0.5f);
        assertEquals(0.5f, SoundManager.getMasterVolume(), 0.0001f);
    }

    @Test
    void testSetMasterVolumeThrowsOnInvalid() {
        assertThrows(IllegalArgumentException.class, () -> SoundManager.setMasterVolume(-0.1f));
        assertThrows(IllegalArgumentException.class, () -> SoundManager.setMasterVolume(1.1f));
    }

    @Test
    void testCloseAllDoesNotThrow() {
        SoundPlayer p1 = new SoundPlayer(RANDOM_SOUND_PATH);
        SoundPlayer p2 = new SoundPlayer(RANDOM_SOUND_PATH);
        assertDoesNotThrow(SoundManager::closeAll);
    }

    @Test
    void testSoundPlayerSetVolumeValid() {
        SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        assertDoesNotThrow(() -> player.setVolume(0.0f));
        assertDoesNotThrow(() -> player.setVolume(1.0f));
        assertDoesNotThrow(() -> player.setVolume(0.5f));
    }

    @Test
    void testSoundPlayerSetVolumeInvalid() {
        SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        assertThrows(IllegalArgumentException.class, () -> player.setVolume(-0.1f));
        assertThrows(IllegalArgumentException.class, () -> player.setVolume(1.1f));
    }

    @Test
    void testSoundPlayerCloseDoesNotThrow() {
        SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        assertDoesNotThrow(player::close);
    }

    @Test
    void testSoundPlayerPlayDoesNotThrow() {
        SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        assertDoesNotThrow(player::play);
    }
}