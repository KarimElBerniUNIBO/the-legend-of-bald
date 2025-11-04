package com.thelegendofbald.model.sounds;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SoundManagerTest {

    private static final String RANDOM_SOUND_PATH = "/interactive-components/mousehover.wav";
    private static final float DELTA = 0.0001f;
    private static final float INITIAL_MASTER_VOLUME = 1.0f;

    @BeforeEach
    void reset() {
        SoundManager.closeAll();
        SoundManager.setMasterVolume(INITIAL_MASTER_VOLUME);
    }

    @Test
    void testSetMasterVolumeThrowsOnInvalid() {
        final List<Float> invalidVolumes = List.of(-0.1f, 1.1f);
        for (final float volume: invalidVolumes) {
            assertThrows(IllegalArgumentException.class, () -> SoundManager.setMasterVolume(volume));
        }
    }

    @Test
    void testCloseAllDoesNotThrow() {
        new SoundPlayer(RANDOM_SOUND_PATH);
        new SoundPlayer(RANDOM_SOUND_PATH);
        assertDoesNotThrow(SoundManager::closeAll);
    }

    @Test
    void testAddSoundPlayerSetsVolume() {
        SoundManager.setMasterVolume(0.7f);
        assertEquals(0.7f, SoundManager.getMasterVolume(), DELTA);
    }

    @Test
    void testSoundPlayerSetVolumeValid() {
        final List<Float> validVolumes = List.of(0.0f, 1.0f, 0.5f);
        for (final float volume: validVolumes) {
            assertDoesNotThrow(() -> SoundManager.setMasterVolume(volume));
            assertEquals(volume, SoundManager.getMasterVolume(), DELTA);
        }
    }

    @Test
    void testSoundPlayerSetVolumeInvalid() {
        final SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        final List<Float> invalidVolumes = List.of(-0.1f, 1.1f);
        for (final float volume: invalidVolumes) {
            assertThrows(IllegalArgumentException.class, () -> player.setVolume(volume));
        }
    }

    @Test
    void testSoundPlayerCloseDoesNotThrow() {
        final SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        assertDoesNotThrow(player::close);
    }

    @Test
    void testSoundPlayerPlayDoesNotThrow() {
        final SoundPlayer player = new SoundPlayer(RANDOM_SOUND_PATH);
        assertDoesNotThrow(player::play);
    }

    @AfterAll
    static void closeAll() {
        SoundManager.closeAll();
    }
}
