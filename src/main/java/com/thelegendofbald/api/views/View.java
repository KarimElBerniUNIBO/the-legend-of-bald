package com.thelegendofbald.api.views;

import java.awt.Dimension;

/**
 * Represents a generic UI view component in the application.
 * <p>
 * Implementations of this interface are responsible for managing their own
 * internal state, rendering, and sizing logic. The interface provides methods
 * to update the view, retrieve its internal size, and set a new internal size.
 * </p>
 *
 * <p>
 * Typical usage involves calling {@link #updateView()} to refresh the UI when
 * underlying data changes, and using {@link #getInternalSize()} and
 * {@link #setInternalSize(Dimension)} to manage the drawable area of the view.
 * </p>
 */
public interface View {

    /**
     * Updates the current state of the menu view.
     * Implementations should refresh or redraw the UI components as needed
     * to reflect any changes in the underlying data or state.
     */
    void updateView();

    /**
     * Returns the internal size of the view as a {@link Dimension} object.
     * This typically represents the drawable or usable area within the view,
     * excluding any borders, padding, or decorations.
     *
     * @return the internal {@code Dimension} of the view
     */
    Dimension getInternalSize();

    /**
     * Sets the internal size of the view component.
     *
     * @param size the new internal {@link Dimension} to set for the view
     */
    void setInternalSize(Dimension size);

}
