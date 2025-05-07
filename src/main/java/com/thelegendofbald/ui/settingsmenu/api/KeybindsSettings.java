package com.thelegendofbald.ui.settingsmenu.api;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.JComponent;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;


public enum KeybindsSettings implements SettingOption {
    UP("UP", KeyEvent.getKeyText(KeyEvent.VK_UP)),
    DOWN("DOWN", KeyEvent.getKeyText(KeyEvent.VK_DOWN)),
    LEFT("LEFT", KeyEvent.getKeyText(KeyEvent.VK_LEFT)),
    RIGHT("RIGHT", KeyEvent.getKeyText(KeyEvent.VK_RIGHT));

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();

    private final String text;
    private final String buttonText;
    private final JComponent jcomponent;

    KeybindsSettings(String text, String buttonText) {
        this.text = text;
        this.buttonText = buttonText;
        this.jcomponent = jbFactory.createKeybindingButton(text, new Dimension(0,0), Optional.empty(), 0.05, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Override
    public String getText() {
        return this.text;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    @Override
    public JComponent getJcomponent() {
        return this.jcomponent;
    }

    /*public static int getSize() {
        return (int) Arrays.stream(VideoSettings.values())
                .count();
    }*/

}
