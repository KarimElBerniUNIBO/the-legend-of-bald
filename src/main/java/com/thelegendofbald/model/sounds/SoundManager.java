package com.thelegendofbald.model.sounds;

import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for managing multiple {@link SoundPlayer} instances.
 * <p>
 * Provides static methods to add sound players and close all managed sound players.
 * This class cannot be instantiated.
 * </p>
 */
public final class SoundManager {

    private static final List<SoundPlayer> SOUNDPLAYERS = new LinkedList<>();

    private SoundManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Adds a {@link SoundPlayer} instance to the list of sound players.
     *
     * @param soundPlayer the {@code SoundPlayer} to be added to the collection
     */
    public static void addSoundPlayer(final SoundPlayer soundPlayer) {
        SOUNDPLAYERS.add(soundPlayer);
    }

    /**
     * Closes all active sound players and clears the list of sound players.
     * <p>
     * This method iterates through all {@code SoundPlayer} instances in the {@code SOUNDPLAYERS} collection,
     * invokes their {@code close()} method to release any resources, and then removes all entries from the collection.
     * </p>
     */
    public static void closeAll() {
        SOUNDPLAYERS.forEach(SoundPlayer::close);
        SOUNDPLAYERS.clear();
    }

}
