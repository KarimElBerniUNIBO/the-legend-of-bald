package com.thelegendofbald.ui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.model.SoundPlayer;


public abstract class TemplateButtonMouseListener extends MouseAdapter {

    private static final String STARTING_PATH = "/button";

    private static final String MOUSE_HOVER_SFX_PATH = STARTING_PATH + "/mousehover.wav";
    private static final String MOUSE_PRESSED_SFX_PATH = STARTING_PATH + "/mousepressed.wav";
    private static final String MOUSE_RELEASED_SFX_PATH = STARTING_PATH + "/mousereleased.wav";

    private final SoundPlayer mouseHover = new SoundPlayer(MOUSE_HOVER_SFX_PATH);
    private final SoundPlayer mousePressed = new SoundPlayer(MOUSE_PRESSED_SFX_PATH);
    private final SoundPlayer mouseReleased = new SoundPlayer(MOUSE_RELEASED_SFX_PATH);

    @Override
    public void mouseEntered(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            mouseHover.play();
        });
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            mousePressed.play();
        });
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
            mouseReleased.play();
        });
    }

}
