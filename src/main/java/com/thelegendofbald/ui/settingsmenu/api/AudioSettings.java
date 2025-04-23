package com.thelegendofbald.ui.settingsmenu.api;

import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JSlider;

public enum AudioSettings {
    MASTER("MASTER VOLUME", new JSlider(0, 100, 50)),
    MUSIC("MUSIC VOLUME", new JSlider(0, 100, 50));

    private final String text;
    private final JComponent jcomponent;

    AudioSettings(String text, JComponent jcomponent) {
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
