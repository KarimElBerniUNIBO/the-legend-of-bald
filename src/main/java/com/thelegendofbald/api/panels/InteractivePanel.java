package com.thelegendofbald.api.panels;

/**
 * Represents a panel in the main menu UI that contains interactive buttons.
 * Provides a method to unselect or deselect all buttons within the panel.
 */
public interface InteractivePanel {

    /**
     * Deselects or unselects all interactive buttons within the panel.
     * <p>
     * This method should be called to ensure that no buttons remain in a selected state,
     * typically when resetting the panel or when a new selection is required.
     * </p>
     */
    void unselectAllButtons();

}
