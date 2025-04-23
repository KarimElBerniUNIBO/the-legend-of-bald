package com.thelegendofbald.ui.settingsmenu.api;

import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSlider;

public enum VideoSettings {
    FULLSCREEN("FULLSCREEN", new JCheckBox()),
    RESOLUTION("RESOLUTION", new JComboBox<>(List.of(
            "900x600",
            "Altro").toArray())),
    FPS("FRAMERATE PER SECOND", new JSlider(0, 144, 60));

    private final String text;
    private final JComponent jcomponent;

    VideoSettings(String text, JComponent jcomponent) {
        this.text = text;
        this.jcomponent = jcomponent;
    }

    public String getText() {
        return this.text;
    }

    public JComponent getJcomponent() {
        return this.jcomponent;
    }

    public static int getSize() {
        return (int) Arrays.stream(VideoSettings.values())
                .count();
    }

}
