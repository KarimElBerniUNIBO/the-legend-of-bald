package com.thelegendofbald.view.window;

import java.awt.Dimension;
import java.util.Optional;

import com.thelegendofbald.view.panel.base.Panels;
import com.thelegendofbald.model.config.WindowMode;

/**
 * Represents a generic view component in the UI layer.
 * Implementations of this interface are responsible for displaying content,
 * managing the main panel, handling internal sizing, and updating the view.
 */
public interface MainView {

    /**
     * Displays the view to the user.
     * Implementations should define how the view is rendered or presented.
     */
    void display();

    /**
     * Changes the main panel of the UI to the specified panel.
     *
     * @param panelEnum the panel to switch to, represented by a value of the {@code Panels} enum
     */
    void changeMainPanel(Panels panelEnum);

    /**
     * Returns the current panel being displayed in the UI.
     * 
     * @return the current panel, represented by a value of the {@code Panels} enum
     */
    Panels getCurrentPanel();

    /**
     * Returns the last panel that was displayed before the current one.
     * This can be useful for implementing "back" functionality or tracking navigation history.
     * 
     * @return an {@link Optional} containing the last {@code Panels}, or an empty {@link Optional} if no previous panel exists
     */
    Optional<Panels> getLastPanel();

    /**
     * Returns the internal size of the view.
     * 
     * @return the internal size of the view as a {@link Dimension}
    */
    Dimension getInternalSize();

    /**
     * Sets the internal size of the view.
     * 
     * @param internalSize the new internal size to set, represented as a {@link Dimension}
     */
    void setInternalSize(Dimension internalSize);

    /**
     * Sets the window mode for the view.
     * This can be used to switch between different display modes such as fullscreen, windowed, or borderless window.
     * 
     * @param mode the {@link WindowMode} to set for the view
     */
    void setWindowMode(WindowMode mode);

    /**
     * Sets the frames per second (FPS) for the view.
     * This can be used to control the refresh rate or animation speed of the view.
     * 
     * @param fps
     */
    void setFPS(int fps);

    /**
     * Changes the visibility of the FPS display in the view.
     * This can be used to toggle whether the frames per second (FPS) is shown on the screen.
     * 
     * @param showFPS a boolean indicating whether to show the FPS display; true to show, false to hide
     */
    void toggleViewFps(boolean showFPS);

    /**
     * Changes the visibility of the timer display in the view.
     * This can be used to toggle whether a timer is shown on the screen.
     * 
     * @param showTimer a boolean indicating whether to show the timer display; true to show, false to hide
     */
    void toggleViewTimer(boolean showTimer);

}
