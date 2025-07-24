package com.thelegendofbald.api.settingsmenu;

public enum WindowMode {
    WINDOW("WINDOW"),
    FULLSCREEN("FULLSCREEN"),
    WINDOWED_FULLSCREEN("WINDOWED FULLSCREEN");

    private final String text;

    WindowMode(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
