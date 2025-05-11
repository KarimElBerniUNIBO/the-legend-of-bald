package com.thelegendofbald.view.buttons;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import com.thelegendofbald.controller.ui.listeners.KeybindingButtonMouseListener;

/**
 * A {@link KeyAdapter} implementation that handles key press events for {@code KeybindingButton} components.
 * <p>
 * When the button is in "changing" mode, this listener captures the pressed key, updates the button's text
 * to display the key's name, and exits the "changing" mode. It also updates the original text in any associated
 * {@link KeybindingButtonMouseListener}.
 * </p>
 */
public class KeybindingButtonKeyListener extends KeyAdapter {

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
        final var button = (KeybindingButton) e.getSource();
        if (button.isChanging()) {
            final var keyCode = e.getKeyCode();
            final var keyCodeToString = KeyEvent.getKeyText(keyCode);
            SwingUtilities.invokeLater(() -> {
                button.setText(keyCodeToString);
                button.setChanging(false);
                Arrays.stream(button.getKeyListeners())
                        .filter(kl -> kl instanceof KeybindingButtonMouseListener)
                        .map(kl -> (KeybindingButtonMouseListener) kl)
                        .findFirst()
                        .ifPresent(listener -> listener.setOriginalText(keyCodeToString));
            });
        }
    }

}
