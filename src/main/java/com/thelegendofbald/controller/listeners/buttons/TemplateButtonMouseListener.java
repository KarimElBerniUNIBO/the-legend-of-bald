package com.thelegendofbald.controller.listeners.buttons;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thelegendofbald.model.sounds.SoundPlayer;


/**
 * An abstract MouseAdapter that provides default sound effects for button mouse events.
 * <p>
 * This listener plays specific sound effects when the mouse enters, is pressed, or is released
 * on a button component. The sound effect files are expected to be located in the "/button" resource path.
 * </p>
 * <ul>
 *   <li>{@code mouseEntered}: Plays a hover sound effect.</li>
 *   <li>{@code mousePressed}: Plays a pressed sound effect.</li>
 *   <li>{@code mouseReleased}: Plays a released sound effect.</li>
 * </ul>
 * 
 * <p>
 * Subclasses can extend this class to add additional behavior to button mouse events
 * while retaining the default sound feedback.
 * </p>
 */
public abstract class TemplateButtonMouseListener extends MouseAdapter {

    /**
     * The base path used for locating button-related resources within the application.
     * This constant serves as the starting directory for button assets or templates.
     */
    protected static final String STARTING_PATH = "/button";

    /**
     * The file path to the sound effect that plays when the mouse hovers over a UI element.
     * This path is constructed by appending "/mousehover.wav" to the base STARTING_PATH.
     */
    protected static final String MOUSE_HOVER_SFX_PATH = STARTING_PATH + "/mousehover.wav";
    /**
     * The file path to the sound effect (SFX) that plays when a mouse button is pressed.
     * This path is constructed by appending "/mousepressed.wav" to the {@code STARTING_PATH}.
     */
    protected static final String MOUSE_PRESSED_SFX_PATH = STARTING_PATH + "/mousepressed.wav";
    /**
     * The file path to the sound effect that plays when the mouse button is released.
     * This path is constructed by appending "/mousereleased.wav" to the STARTING_PATH.
     */
    protected static final String MOUSE_RELEASED_SFX_PATH = STARTING_PATH + "/mousereleased.wav";

    private final SoundPlayer mouseHover = new SoundPlayer(MOUSE_HOVER_SFX_PATH);
    private final SoundPlayer mousePressed = new SoundPlayer(MOUSE_PRESSED_SFX_PATH);
    private final SoundPlayer mouseReleased = new SoundPlayer(MOUSE_RELEASED_SFX_PATH);

    /**
     * Invoked when the mouse enters a component.
     * Plays the mouse hover sound effect to provide audio feedback to the user.
     *
     * @param e the MouseEvent associated with the mouse entering the component
     */
    @Override
    public void mouseEntered(final MouseEvent e) {
        mouseHover.play();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     * Plays the mousePressed sound effect when the mouse is pressed.
     *
     * @param e the MouseEvent associated with the mouse press
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        mousePressed.play();
    }

    /**
     * Invoked when the mouse button has been released on a component.
     * Plays the mouseReleased sound effect.
     *
     * @param e the MouseEvent associated with the mouse release action
     */
    @Override
    public void mouseReleased(final MouseEvent e) {
        mouseReleased.play();
    }

}
