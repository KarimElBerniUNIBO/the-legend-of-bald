package com.thelegendofbald.model.sounds;

import java.util.LinkedList;
import java.util.List;

import com.thelegendofbald.api.settingsmenu.AudioSettings;

/**
 * Utility class for managing multiple {@link SoundPlayer} instances.
 * <p>
 * Provides static methods to add sound players and close all managed sound
 * players.
 * This class cannot be instantiated.
 * </p>
 */
public final class SoundManager {

    private static final List<SoundPlayer> SOUNDPLAYERS = new LinkedList<>();
    private static float masterVolume = (int) AudioSettings.MASTER.getValue() / 100f;

    private SoundManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Adds a {@link SoundPlayer} instance to the list of sound players.
     *
     * @param soundPlayer the {@code SoundPlayer} to be added to the collection
     */
    public static void addSoundPlayer(final SoundPlayer soundPlayer) {
        soundPlayer.setVolume(masterVolume);
        SOUNDPLAYERS.add(soundPlayer);
    }

    /**
     * Retrieves the current master volume level.
     *
     * @return the current master volume level, a float between 0 and 1
     */
    public static float getMasterVolume() {
        return masterVolume;
    }

    /**
     * Sets the master volume for all sound players.
     * <p>
     * This method updates the master volume level and applies it to all
     * {@code SoundPlayer} instances
     * currently managed by the {@code SoundManager}.
     * </p>
     *
     * @param volume the new master volume level, a float between 0 and 1
     * @throws IllegalArgumentException if the provided volume is not between 0 and 1
     */
    public static void setMasterVolume(final float volume) {
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0 and 1");
        }
        System.out.println(String.format("Setting master volume to %.0f %%", volume * 100));
        SOUNDPLAYERS.forEach(player -> player.setVolume(volume));
        masterVolume = volume;
    }

    /**
     * Closes all active sound players and clears the list of sound players.
     * <p>
     * This method iterates through all {@code SoundPlayer} instances in the
     * {@code SOUNDPLAYERS} collection,
     * invokes their {@code close()} method to release any resources, and then
     * removes all entries from the collection.
     * </p>
     */
    public static void closeAll() {
        SOUNDPLAYERS.forEach(SoundPlayer::close);
        SOUNDPLAYERS.clear();
    }

}
