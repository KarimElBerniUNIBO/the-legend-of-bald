package com.thelegendofbald.ui.settingsmenu.api;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;

public enum Settings {
    VIDEO(VideoSettings.values(), Optional.empty(), Optional.empty()),
    AUDIO(AudioSettings.values(), Optional.empty(), Optional.empty()),
    KEYBINDS(KeybindsSettings.values(), Optional.empty(), Optional.empty());

    private final SettingOption[] configs;
    private Optional<SettingsEditor> settingsEditor;
    private Optional<JButton> linkedButton;
    
    Settings(SettingOption[] settings, Optional<SettingsEditor> settingsEditor, Optional<JButton> linkedButton) {
        this.configs = settings;
        this.settingsEditor = settingsEditor;
        this.linkedButton = linkedButton;
    }

    public String getName() {
        return this.name();
    }

    public List<SettingOption> getConfigs() {
        return Arrays.stream(configs).toList();
    }

    public SettingsEditor getSettingsEditor() {
        return this.settingsEditor.orElseThrow(() -> new NullPointerException());
    }

    public void setSettingsEditor(SettingsEditor settingsEditor) {
        this.settingsEditor = Optional.of(settingsEditor);
    }

    public JButton getLinkedButton() {
        return this.linkedButton.orElseThrow(() -> new NullPointerException());
    }

    public void setLinkedButton(JButton button) {
        this.linkedButton = Optional.of(button);
    }

    public int getIndex() {
        return this.ordinal();
    }

    public static int getMaxIndex() {
        return Arrays.stream(Settings.values())
                    .mapToInt(Settings::getIndex)
                    .max()
                    .orElse(0);
    }

    public static Settings getSettingByIndex(int index) {
        return Arrays.stream(Settings.values())
                .filter(b -> b.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
