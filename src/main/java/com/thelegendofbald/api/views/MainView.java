package com.thelegendofbald.api.views;

import java.awt.Dimension;
import java.util.Optional;

import com.thelegendofbald.api.panels.Panels;

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

}
