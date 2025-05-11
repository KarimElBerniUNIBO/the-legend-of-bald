package com.thelegendofbald.ui.controller;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * A {@link ComponentAdapter} that executes a specified {@link Runnable}
 * whenever the associated component is resized.
 * <p>
 * This listener can be attached to a Swing component to perform custom actions
 * (such as layout updates or UI refreshes) in response to resize events.
 * </p>
 *
 * <pre>
 * Example usage:
 * component.addComponentListener(new ResizeListener(() -> {
 *     // Code to execute on resize
 * }));
 * </pre>
 */
public class ResizeListener extends ComponentAdapter {

    private final Runnable runnable;

    /**
     * Constructs a new {@code ResizeListener} with the specified {@link Runnable} to be executed
     * when a resize event occurs.
     *
     * @param runnable the {@code Runnable} to execute on resize events
     */
    public ResizeListener(final Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Called when the component is resized.
     * <p>
     * Subclasses can override this method to provide custom behavior when a resize
     * event occurs. If overridden, ensure that {@code super.componentResized(ComponentEvent)}
     * is called to preserve the default behavior of executing the associated {@link Runnable}.
     * </p>
     *
     * @param e the {@link ComponentEvent} triggered by the resize
     */
    @Override
    public void componentResized(final ComponentEvent e) {
        super.componentResized(e);
        runnable.run();
    }

}
