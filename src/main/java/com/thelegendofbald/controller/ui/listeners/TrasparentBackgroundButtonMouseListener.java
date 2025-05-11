package com.thelegendofbald.controller.ui.listeners;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Optional;

import com.thelegendofbald.api.panels.InteractivePanel;
import com.thelegendofbald.utils.ColorUtils;
import com.thelegendofbald.view.buttons.TrasparentBackgroundButton;

/**
 * A mouse listener for {@link TrasparentBackgroundButton} that handles foreground color changes
 * and selection logic based on mouse events. When the mouse enters or exits the button,
 * the foreground color is darkened or restored, respectively, unless the button is selected
 * or has an icon. On mouse press, the button is selected and all other buttons in the parent
 * {@link InteractivePanel} are unselected.
 *
 * <p>
 * This listener uses a darkness factor to compute the hover color and ensures that only
 * one button is selected at a time within its parent panel.
 * </p>
 *
 * @see TrasparentBackgroundButton
 * @see TemplateButtonMouseListener
 * @see InteractivePanel
 */
public class TrasparentBackgroundButtonMouseListener extends TemplateButtonMouseListener {

    private static final double FACTOR_OF_DARKNESS = 0.6;

    private final Color buttonFGColor;
    private final Color buttonFGHoverColor;

    /**
     * Mouse listener for {@link TrasparentBackgroundButton} that manages the button's foreground color
     * on mouse events. Stores the original foreground color and computes a darker color for hover effect.
     *
     * @param button the {@link TrasparentBackgroundButton} to attach the mouse listener to
     */
    public TrasparentBackgroundButtonMouseListener(final TrasparentBackgroundButton button) {
        this.buttonFGColor = button.getForeground();
        this.buttonFGHoverColor = ColorUtils.getDarkenColor(buttonFGColor, FACTOR_OF_DARKNESS);
    }

    /**
     * Handles the mouse entering the button area.
     * <p>
     * Subclasses can override this method to provide custom behavior when the mouse
     * enters the button area. If overridden, ensure that {@code super.mouseEntered(MouseEvent)}
     * is called to preserve the default behavior of updating the button's foreground color.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse entering the button area
     */
    @Override
    public void mouseEntered(final MouseEvent e) {
        super.mouseEntered(e);
        final var button = (TrasparentBackgroundButton) e.getSource();

        if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
            button.setForeground(buttonFGHoverColor);
        }
    }

    /**
     * Handles the mouse exiting the button area.
     * <p>
     * Subclasses can override this method to provide custom behavior when the mouse
     * exits the button area. If overridden, ensure that {@code super.mouseExited(MouseEvent)}
     * is called to preserve the default behavior of restoring the button's original foreground color.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse exiting the button area
     */
    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);
        final var button = (TrasparentBackgroundButton) e.getSource();

        if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
            button.setForeground(buttonFGColor);
        }
    }

    /**
     * Handles the mouse press event on the button.
     * <p>
     * Subclasses can override this method to provide custom behavior when the button
     * is pressed. If overridden, ensure that {@code super.mousePressed(MouseEvent)}
     * is called to preserve the default behavior of managing the button's selection state.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse press
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);
        final var button = (TrasparentBackgroundButton) e.getSource();

        if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
            final var parent = (InteractivePanel) button.getParent();
            parent.unselectAllButtons();
            button.select();
        }
    }

}
