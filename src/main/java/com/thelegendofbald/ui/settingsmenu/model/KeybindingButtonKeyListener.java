package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.settingsmenu.controller.KeybindingButtonMouseListener;

public class KeybindingButtonKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        var button = (KeybindingButton) e.getSource();
        if (button.isChanging()) {
            var keyCode = e.getKeyCode();
            var keyCodeToString = KeyEvent.getKeyText(keyCode);
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
