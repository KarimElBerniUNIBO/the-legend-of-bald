package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.thelegendofbald.ui.mainmenu.api.JButtonFactory;

class CenterPanel extends JPanel {

    private static final String PLAY_BUTTON_TEXT = "PLAY";
    private static final String SETTINGS_BUTTON_TEXT = "SETTINGS";

    private final JButtonFactory buttonFactory = new JButtonFactoryImpl();

    private final JButton playButton;
    private final JButton settingsButton;

    CenterPanel(Dimension size) {
        //this.setPreferredSize(new Dimension((int) size.getWidth() / 2, (int) size.getHeight() / 2));
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        playButton = buttonFactory.createSquareButton(PLAY_BUTTON_TEXT, size);
        settingsButton = buttonFactory.createSquareButton(SETTINGS_BUTTON_TEXT, size);

        this.add(playButton);
        this.add(settingsButton);
    }

}
