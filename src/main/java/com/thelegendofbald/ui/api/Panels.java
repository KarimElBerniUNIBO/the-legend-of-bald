package com.thelegendofbald.ui.api;

import java.util.Arrays;

public enum Panels {
    MAIN_MENU("main", 0),
    SETTINGS_MENU("settings", 1),
    LEADERBOARD_MENU("leaderboard", 2),
    PLAY_MENU("play", 3);

    private final String name;
    private final int index;

    Panels(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public static int getMaxIndex() {
        return Arrays.stream(Panels.values())
                    .mapToInt(Panels::getIndex)
                    .max()
                    .orElse(0);
    }
}
