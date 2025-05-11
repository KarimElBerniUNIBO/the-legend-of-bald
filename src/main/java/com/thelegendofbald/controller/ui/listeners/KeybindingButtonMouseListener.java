package com.thelegendofbald.controller.ui.listeners;

import java.awt.event.MouseEvent;

import com.thelegendofbald.view.buttons.KeybindingButton;

/**
 * A mouse listener for {@link KeybindingButton} that handles the process of changing keybindings.
 * <p>
 * When the button is clicked, it enters a "changing" state, updates its text to prompt the user,
 * and requests focus to capture the next key press. If the mouse exits the button while in the
 * "changing" state, it cancels the operation and restores the original button text.
 * </p>
 *
 * <p>
 * This class extends {@link RoundedButtonMouseListener} to preserve custom button appearance
 * behaviors, while adding logic specific to keybinding changes.
 * </p>
 *
 * @see KeybindingButton
 * @see RoundedButtonMouseListener
 */
public class KeybindingButtonMouseListener extends RoundedButtonMouseListener {

    private final KeybindingButton button;
    private String originalText;

    /**
     * Constructs a new {@code KeybindingButtonMouseListener} for the specified {@link KeybindingButton}.
     * Initializes the listener by calling the superclass constructor with the button,
     * stores a reference to the button, and saves its original text for later use.
     *
     * @param button the {@code KeybindingButton} this mouse listener is associated with
     */
    public KeybindingButtonMouseListener(final KeybindingButton button) {
        super(button);
        this.button = button;
        this.originalText = button.getText();
    }

    /**
     * Handles mouse click events.
     * <p>
     * Subclasses can override this method to provide custom behavior when the button
     * is clicked. If overridden, ensure that {@code super.mouseClicked(MouseEvent)} is
     * called to preserve the default behavior of entering the "changing" state.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse click
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        super.mouseClicked(e);
        if (!button.isChanging()) {
            button.setChanging(true);
            button.requestFocusInWindow();
            button.setText("Press a key...");
        }
    }

    /**
     * Handles mouse exit events.
     * <p>
     * Subclasses can override this method to provide custom behavior when the mouse
     * exits the button. If overridden, ensure that {@code super.mouseExited(MouseEvent)} is
     * called to preserve the default behavior of canceling the "changing" state.
     * </p>
     *
     * @param e the {@link MouseEvent} triggered by the mouse exit
     */
    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);
        if (button.isChanging()) {
            button.setChanging(false);
            button.setText(originalText);
        }
    }

    /**
     * Sets the original text of the button.
     * <p>
     * Subclasses can override this method to provide custom logic for updating the
     * original text. If overridden, ensure that the new text is consistent with the
     * button's state.
     * </p>
     *
     * @param originalText the new original text to set
     */
    public void setOriginalText(final String originalText) {
        this.originalText = originalText;
    }

}
