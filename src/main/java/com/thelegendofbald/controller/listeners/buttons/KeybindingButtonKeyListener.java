package com.thelegendofbald.controller.listeners.buttons;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

import com.thelegendofbald.view.panel.base.Panels;
import com.thelegendofbald.model.config.ControlsSettings;
import com.thelegendofbald.view.component.KeybindingButton;
import com.thelegendofbald.view.panel.game.GamePanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
    @SuppressFBWarnings(
        value = "EI2",
        justification = "KeybindingButtonKeyListener is designed to be used with KeybindingButton instances only."
    )
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
            final var gamePanel = (GamePanel) Panels.GAME_MENU.getPanel();
            button.setChanging(false);
            SwingUtilities.invokeLater(() -> {
                ControlsSettings.setKeyCode(ControlsSettings.getKeybind(button), keyCode);
                gamePanel.refreshKeyBindings();
            });
        }
    }

}
