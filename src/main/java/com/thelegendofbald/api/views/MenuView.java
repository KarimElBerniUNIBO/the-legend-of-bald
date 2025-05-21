package com.thelegendofbald.api.views;

/**
 * Represents a view interface for a menu in the user interface.
 * Implementations of this interface are responsible for updating the menu view
 * and adding necessary components to the panel.
 */
public interface MenuView {

    /**
     * Adds the necessary UI components to the panel.
     * <p>
     * This method should be implemented to add all components 
     * (such as buttons, labels, etc.) to the panel for the menu view.
     * </p>
     */
    void addComponentsToPanel();

}
