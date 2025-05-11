package com.thelegendofbald.ui.api;

/**
 * An interface for UI components that can be resized.
 * <p>
 * Implementing classes should define how they handle resize events
 * and how their internal components adjust their sizes accordingly.
 * </p>
 *
 * <ul>
 *   <li>{@link #onResize()} - Called when the component is resized.</li>
 *   <li>{@link #updateComponentsSize()} - Updates the size of internal components.</li>
 * </ul>
 */
public interface Resizable {

    /**
     * Called when a resize event occurs.
     * Implementing classes should define how they respond to resizing,
     * such as adjusting layout or redrawing components.
     */
    void onResize();

    /**
     * Updates the size of the UI components implementing this interface.
     * <p>
     * This method should be called whenever the parent container is resized
     * or when the components need to adjust their size dynamically.
     * Implementing classes should provide the logic to resize and reposition
     * their child components accordingly.
     */
    void updateComponentsSize();

}
