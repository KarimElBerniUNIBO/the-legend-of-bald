package com.thelegendofbald.api.settingsmenu;

public enum WindowMode {
    FULLSCREEN("FULLSCREEN"),
    WINDOWED_FULLSCREEN("WINDOWED FULLSCREEN"),
    WINDOW("WINDOW");

    private final String text;

    WindowMode(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
