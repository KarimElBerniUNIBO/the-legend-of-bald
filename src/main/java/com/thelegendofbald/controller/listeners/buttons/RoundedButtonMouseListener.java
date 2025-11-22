package com.thelegendofbald.controller.listeners.buttons;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.thelegendofbald.controller.listeners.TemplateInteractiveComponentMouseListener;
import com.thelegendofbald.utils.ColorUtils;
import com.thelegendofbald.view.component.RoundedButton;

/**
 * A mouse listener for {@link RoundedButton} components that changes the button's
 * background and foreground colors when the mouse enters or exits the button area.
 * <p>
 * On mouse hover, the button's colors are darkened to provide visual feedback.
 * When the mouse exits, the original colors are restored.
 * </p>
 *
 * @see RoundedButton
 * @see TemplateInteractiveComponentMouseListener
 */
public class RoundedButtonMouseListener extends TemplateInteractiveComponentMouseListener {

    private static final double FACTOR_OF_DARKNESS = 0.6;

    private final Color buttonBGColor;
    private final Color buttonBGHoverColor;

    private final Color buttonFGColor;
    private final Color buttonFGHoverColor;

    /**
     * Constructs a new {@code RoundedButtonMouseListener} for the specified {@link RoundedButton}.
     * <p>
     * Initializes the background and foreground colors for the button, as well as their
     * corresponding hover colors by darkening the original colors using a predefined factor.
     * </p>
     *
     * @param button the {@link RoundedButton} to which this mouse listener will be attached
     */
    public RoundedButtonMouseListener(final RoundedButton button) {
        this.buttonBGColor = button.getBackground();
        this.buttonBGHoverColor = ColorUtils.getDarkenColor(this.buttonBGColor, FACTOR_OF_DARKNESS);

        this.buttonFGColor = button.getForeground();
        this.buttonFGHoverColor = ColorUtils.getDarkenColor(this.buttonFGColor, FACTOR_OF_DARKNESS);
    }

    /**
     * Handles the mouse entering the button area.
     * <p>
     * Subclasses can override this method to provide custom behavior when the mouse
     * enters the button area. If overridden, ensure that {@code super.mouseEntered(MouseEvent)}
     * is called to preserve the default behavior of updating the button's colors.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse entering the button area
     */
    @Override
    public void mouseEntered(final MouseEvent e) {
        super.mouseEntered(e);
        final var button = (RoundedButton) e.getSource();

        button.setBackground(this.buttonBGHoverColor);
        button.setForeground(this.buttonFGHoverColor);
    }

    /**
     * Handles the mouse exiting the button area.
     * <p>
     * Subclasses can override this method to provide custom behavior when the mouse
     * exits the button area. If overridden, ensure that {@code super.mouseExited(MouseEvent)}
     * is called to preserve the default behavior of restoring the button's original colors.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse exiting the button area
     */
    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);
        final var button = (RoundedButton) e.getSource();

        button.setBackground(this.buttonBGColor);
        button.setForeground(this.buttonFGColor);
    }

    /**
     * Handles the mouse press event on the button.
     * <p>
     * Subclasses can override this method to provide custom behavior when the button
     * is pressed. If overridden, ensure that {@code super.mousePressed(MouseEvent)}
     * is called to preserve the default behavior of resetting the button's colors.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse press
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);
        this.mouseExited(e);
    }

}
