package com.thelegendofbald.ui.mainmenu.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.api.Panels;
import com.thelegendofbald.ui.mainmenu.controller.SwitchToOtherPanel;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.view.GameWindow;

class CenterPanel extends JPanel {

    //private static final int PROPORTION = 2;
    private static final int BUTTONS_PADDING_PROPORTION = 15;
    private static final double DEFAULT_ARC_PROPORTION = 0.2;

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

    CenterPanel(final Dimension size) {
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());

        playButton = buttonFactory.createRoundedButton(PLAY_BUTTON_TEXT, size, DEFAULT_ARC_PROPORTION);
        settingsButton = buttonFactory.createRoundedButton(SETTINGS_BUTTON_TEXT, size, DEFAULT_ARC_PROPORTION);
        leaderBoardButton = buttonFactory.createRoundedButton(LEADERBOARD_BUTTON_TEXT, size, DEFAULT_ARC_PROPORTION);

        buttons = new LinkedList<>(List.of(playButton, settingsButton, leaderBoardButton));

        gbc = gbcFactory.createBothGridBagConstraints();
        gbc.insets = new Insets(0, 0, (int) size.getHeight() / BUTTONS_PADDING_PROPORTION, 0);

        this.connectButtonsWithActionListeners();
        this.addButtonsToPanel();
    }

    private void connectButtonsWithActionListeners() {
        Map<JButton, Panels> buttonWithPanel = Map.of(
            playButton, Panels.PLAY_MENU,
            settingsButton, Panels.SETTINGS_MENU,
            leaderBoardButton, Panels.LEADERBOARD_MENU
        );

        this.buttons.stream()
                .forEach(b -> b.addActionListener(e -> {
                    var parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
                    new SwitchToOtherPanel(parent, buttonWithPanel.get(b)).actionPerformed(e);
                }));
    }

    private void addButtonsToPanel() {
        buttons.stream().forEach(b -> {
            gbc.gridy = buttons.indexOf(b);
            this.add(b, gbc);
        });
    }

}
