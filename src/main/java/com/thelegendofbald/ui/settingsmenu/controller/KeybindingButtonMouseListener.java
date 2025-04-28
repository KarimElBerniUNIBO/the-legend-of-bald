package com.thelegendofbald.ui.settingsmenu.controller;

import java.awt.event.MouseEvent;

import com.thelegendofbald.ui.controller.RoundedButtonMouseListener;
import com.thelegendofbald.ui.settingsmenu.model.KeybindingButton;

public class KeybindingButtonMouseListener extends RoundedButtonMouseListener {

    private final KeybindingButton button;
    private String originalText;

    public KeybindingButtonMouseListener(KeybindingButton button) {
        super(button);
        this.button = button;
        this.originalText = button.getText();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (!button.isChanging()) {
            button.setChanging(true);
            button.requestFocusInWindow();
            button.setText("Press a key...");
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        if (button.isChanging()) {
            button.setChanging(false);
            button.setText(originalText);
        }
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

}
