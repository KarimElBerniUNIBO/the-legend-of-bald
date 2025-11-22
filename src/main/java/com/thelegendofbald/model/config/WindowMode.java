package com.thelegendofbald.model.config;

/**
 * Enum representing different window modes for the game.
 */
public enum WindowMode {
    /**
     * Windowed mode.
     */
    WINDOW("WINDOW"),
    /**
     * Fullscreen mode.
     */
    FULLSCREEN("FULLSCREEN"),
    /**
     * Windowed fullscreen mode.
     */
    WINDOWED_FULLSCREEN("WINDOWED FULLSCREEN");

    private final String text;

    WindowMode(final String text) {
        this.text = text;
    }

    /**
     * Returns the text representation of the window mode.
     *
     * @return the text for this window mode
     */
    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
