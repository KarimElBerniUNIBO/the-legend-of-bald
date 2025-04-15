package com.thelegendofbald.ui.model;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {

    private static final String STARTING_PATH = "/sounds";
    private final String PATH;
    private Optional<Clip> clip = Optional.empty();

    public SoundPlayer(final String PATH) {
        this.PATH = STARTING_PATH + PATH;
        this.preloadSound();
    }

    private void preloadSound() {
        try (InputStream is = this.getClass().getResourceAsStream(PATH)) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = Optional.of(AudioSystem.getClip());
            clip.get().open(audioStream);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void play() {
        if (clip.isPresent()) {
            clip.get().setFramePosition(0);
            clip.get().start();
        }
    }
}