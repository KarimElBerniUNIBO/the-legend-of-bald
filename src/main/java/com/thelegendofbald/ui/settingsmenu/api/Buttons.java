package com.thelegendofbald.ui.settingsmenu.api;

import java.util.Arrays;
import java.util.Optional;

public enum Buttons {
    VIDEO("VIDEO", 0, Optional.empty()),
    AUDIO("AUDIO", 1, Optional.empty()),
    KEYBINDS("KEYBINDS", 2, Optional.empty());

    private final String name;
    private final int index;
    private Optional<SettingsEditor> settingsEditor;

    Buttons(String name, int index, Optional<SettingsEditor> settingsEditor) {
        this.name = name;
        this.index = index;
        this.settingsEditor = settingsEditor;
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public SettingsEditor getSettingsEditor() {
        return this.settingsEditor.orElseThrow(() -> new NullPointerException());
    }

    public void setSettingsEditor(SettingsEditor settingsEditor) {
        this.settingsEditor = Optional.of(settingsEditor);
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

