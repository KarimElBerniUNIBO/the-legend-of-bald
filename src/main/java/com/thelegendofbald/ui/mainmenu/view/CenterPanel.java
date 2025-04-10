package com.thelegendofbald.ui.mainmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;

class CenterPanel extends JPanel {

    private static final String PLAY_BUTTON_TEXT = "PLAY";
    private static final String SETTINGS_BUTTON_TEXT = "SETTINGS";
    private static final String LEADERBOARD_BUTTON_TEXT = "LEADERBOARD";

    private final JButtonFactory buttonFactory = new JButtonFactoryImpl();
    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();

    private final GridBagConstraints gbc;

    private final JButton playButton;
    private final JButton settingsButton;
    private final JButton leaderBoardButton;

    private final List<JButton> buttons;

    CenterPanel(Dimension size) {
        this.setPreferredSize(new Dimension((int) size.getWidth() / 2, (int) size.getHeight() / 2));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());

        playButton = buttonFactory.createSquareButton(PLAY_BUTTON_TEXT, size);
        settingsButton = buttonFactory.createSquareButton(SETTINGS_BUTTON_TEXT, size);
        leaderBoardButton = buttonFactory.createSquareButton(LEADERBOARD_BUTTON_TEXT, size);

        buttons = new LinkedList<>(List.of(playButton, settingsButton, leaderBoardButton));

        gbc = gbcFactory.createBothGridBagConstraints();
        gbc.insets = new Insets(0, 0, (int) size.getHeight() / 15, 0);

        this.addButtonsToPanel();
    }

    private void addButtonsToPanel() {
        buttons.stream().forEach(b -> {
            gbc.gridy = buttons.indexOf(b);
            this.add(b, gbc);
        });
    }

}
