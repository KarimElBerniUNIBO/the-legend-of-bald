package com.thelegendofbald.model.sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The {@code SoundPlayer} class is responsible for loading and playing audio
 * clips from the application's resources.
 * <p>
 * Each instance of {@code SoundPlayer} is associated with a specific sound
 * file, which is preloaded upon construction.
 * The class provides methods to play the sound from the beginning and to
 * release resources when the sound is no longer needed.
 * <p>
 * Usage example:
 * 
 * <pre>
 * SoundPlayer player = new SoundPlayer("/effect.wav");
 * player.play();
 * player.close();
 * </pre>
 */
public final class SoundPlayer {

    private static final String STARTING_PATH = "/sounds";
    private final String path;
    private Optional<Clip> clip = Optional.empty();

    /**
     * Constructs a new SoundPlayer instance for the specified sound file path.
     * Prepends the STARTING_PATH to the provided path, preloads the sound,
     * and registers this SoundPlayer with the SoundManager.
     *
     * @param path the relative path to the sound file to be played
     */
    public SoundPlayer(final String path) {
        this.path = STARTING_PATH + path;
        this.preloadSound();
        this.setVolume(SoundManager.getMasterVolume());
        SoundManager.addSoundPlayer(this);
    }

    private void preloadSound() {
        try (InputStream is = this.getClass().getResourceAsStream(path)) {
            Optional.ofNullable(is).orElseThrow(() -> new IllegalArgumentException("Sound file not found: " + path));
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = Optional.of(AudioSystem.getClip());
            clip.get().open(audioStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            // TODO Log exception
            e.printStackTrace();
        }
    }

    /**
     * Plays the audio clip from the beginning.
     * <p>
     * If an audio clip is present, this method resets its frame position to the
     * start
     * and begins playback. If no clip is available, this method does nothing.
     */
    public void play() {
        if (clip.isPresent()) {
            clip.get().setFramePosition(0);
            clip.get().start();
        }
    }

    /**
     * Sets the volume of the audio clip.
     * <p>
     * The volume is specified as a float between 0 (silent) and 1 (maximum
     * volume).
     * If the provided volume is outside this range, an IllegalArgumentException
     * is thrown.
     *
     * @param volume the desired volume level, a float between 0 and 1
     * @throws IllegalArgumentException if the provided volume is not between 0 and
     *                                  1
     */
    public void setVolume(final float volume) {
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0 and 1");
        }
        clip.filter(c -> c.isControlSupported(FloatControl.Type.MASTER_GAIN))
                .ifPresent(c -> {
                    final FloatControl gain = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
                    final float dB = (float) (20 * Math.log10(volume)); // Fomula to convert linear volume to dB
                    gain.setValue(dB);
                });
    }

    /**
     * Closes the audio clip if it is present, releasing any system resources
     * associated with it.
     * This method should be called when the sound player is no longer needed to
     * prevent resource leaks.
     */
    public void close() {
        clip.ifPresent(Clip::close);
    }
}
