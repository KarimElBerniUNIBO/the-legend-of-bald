package com.thelegendofbald.ui.settingsmenu.api;

import java.awt.event.KeyEvent;
import java.util.Arrays;


public enum KeybindsSettings {
    UP("UP", KeyEvent.getKeyText(KeyEvent.VK_UP)),
    DOWN("DOWN", KeyEvent.getKeyText(KeyEvent.VK_DOWN)),
    LEFT("LEFT", KeyEvent.getKeyText(KeyEvent.VK_LEFT)),
    RIGHT("RIGHT", KeyEvent.getKeyText(KeyEvent.VK_RIGHT));

    private final String text;
    private final String buttonText;

    KeybindsSettings(String text, String buttonText) {
        this.text = text;
        this.buttonText = buttonText;
    }

    public String getText() {
        return this.text;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    public static int getSize() {
        return (int) Arrays.stream(VideoSettings.values())
                .count();
    }

}
