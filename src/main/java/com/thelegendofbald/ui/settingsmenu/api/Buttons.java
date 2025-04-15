package com.thelegendofbald.ui.settingsmenu.api;

import java.util.Arrays;

public enum Buttons {
    VIDEO("VIDEO", 0),
    AUDIO("AUDIO", 1),
    KEYBINDS("KEYBINDS", 2);

    private final String name;
    private final int index;

    Buttons(String name, int index) {
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
        return Arrays.stream(Buttons.values())
                    .mapToInt(Buttons::getIndex)
                    .max()
                    .orElse(0);
    }

    public static Buttons getIndex(int index) {
        return Arrays.stream(Buttons.values())
                .filter(b -> b.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}

