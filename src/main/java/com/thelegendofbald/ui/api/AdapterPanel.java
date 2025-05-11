package com.thelegendofbald.ui.api;

import java.awt.Dimension;

import com.thelegendofbald.ui.controller.ResizeListener;

/**
 * An abstract panel that adapts its layout and components based on resizing events.
 * <p>
 * AdapterPanel extends {@link BasePanel} and implements the {@link Resizable} interface,
 * providing a framework for panels that need to respond to size changes.
 * </p>
 *
 * <ul>
 *   <li>Defines a default proportion constant for layout calculations.</li>
 *   <li>Registers a resize listener to handle component resizing.</li>
 *   <li>Requires subclasses to implement component initialization and addition logic.</li>
 * </ul>
 */
public abstract class AdapterPanel extends BasePanel implements Resizable {

    /**
     * The proportion constant used to determine the relative size or scaling factor
     * for UI components within the AdapterPanel. A value of 0.85 typically means
     * that the component will occupy 85% of the available space.
     */
    protected static final double PROPORTION = 0.85;

    /**
     * Constructs an AdapterPanel with the specified size.
     * Initializes the panel and adds a component listener to handle resize events.
     *
     * @param size the initial size of the panel
     */
    public AdapterPanel(final Dimension size) {
        super(size);
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    @Override
    protected abstract void initializeComponents();

    /**
     * Handles resize events for the panel.
     * <p>
     * Subclasses can override this method to provide custom behavior when the panel
     * is resized. If overridden, ensure that {@code super.onResize()} is called to
     * preserve the default behavior of updating the view when the panel's size changes.
     * </p>
     */
    @Override
    public void onResize() {
        if (this.isInitialized() && this.getWidth() > 0 && this.getHeight() > 0) {
            this.updateView();
        }
    }

    @Override
    public abstract void addComponentsToPanel();

}
