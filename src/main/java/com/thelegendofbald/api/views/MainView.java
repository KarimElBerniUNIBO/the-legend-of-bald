package com.thelegendofbald.api.views;

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

}
