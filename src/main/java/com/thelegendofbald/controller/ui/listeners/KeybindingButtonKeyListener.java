package com.thelegendofbald.controller.ui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

import com.thelegendofbald.api.settingsmenu.ControlsSettings;
import com.thelegendofbald.view.buttons.KeybindingButton;

/**
 * A {@link KeyAdapter} implementation that handles key press events for {@code KeybindingButton} components.
 * <p>
 * When the button is in "changing" mode, this listener captures the pressed key, updates the button's text
 * to display the key's name, and exits the "changing" mode. It also updates the original text in any associated
 * {@link KeybindingButtonMouseListener}.
 * </p>
 */
public class KeybindingButtonKeyListener extends KeyAdapter {

    private final KeybindingButton button;

    /**
     * Constructs a new {@code KeybindingButtonKeyListener} with the specified {@link KeybindingButton}.
     *
     * @param button the {@link KeybindingButton} associated with this key listener
     */
    public KeybindingButtonKeyListener(final KeybindingButton button) {
        this.button = button;
    }

    /**
     * Handles key press events for {@code KeybindingButton} components.
     * <p>
     * Subclasses can override this method to provide custom behavior when a key is pressed.
     * If overridden, ensure that {@code super.keyPressed(KeyEvent)} is called to preserve
     * the default behavior of handling the "changing" state and updating the button's text.
     * </p>
     *
     * @param e the {@link KeyEvent} triggered by the key press
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        super.keyPressed(e);
        if (button.isChanging()) {
            final var keyCode = e.getKeyCode();
            button.setChanging(false);
            SwingUtilities.invokeLater(() -> {
                ControlsSettings.setKeyCode(ControlsSettings.getKeybind(button), keyCode);
            });
        }
    }

}
