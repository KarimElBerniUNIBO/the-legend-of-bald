package com.thelegendofbald.ui.mainmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.api.Panels;
import com.thelegendofbald.ui.mainmenu.api.Buttons;
import com.thelegendofbald.ui.mainmenu.api.InteractivePanel;
import com.thelegendofbald.ui.mainmenu.controller.SwitchToOtherPanel;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.view.GameWindow;

final class CenterPanel extends JPanel implements InteractivePanel {

    // private static final int PROPORTION = 2;
    private static final double BUTTONS_PADDING_PROPORTION = 0.05;
    private static final double DEFAULT_ARC_PROPORTION = 0.2;

    private final JButtonFactory buttonFactory = new JButtonFactoryImpl();
    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();

    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<JButton> buttons;

    CenterPanel(final Dimension size) {
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());

        buttons = this.getListOfButtons(size);

        this.connectButtonsWithActionListeners();
        this.addButtonsToPanel();
    }

    @Override
    public List<JButton> getListOfButtons(Dimension size) {
        return Stream.iterate(0, i -> i <= Buttons.getMaxIndex(), i -> i + 1)
                .map(i -> buttonFactory.createRoundedButton(Buttons.getIndex(i).getName(), size,
                        DEFAULT_ARC_PROPORTION, Optional.empty(), Optional.empty(), Optional.of(Color.BLACK),
                        Optional.empty()))
                .toList();
    }

    @Override
    public void connectButtonsWithActionListeners() {
        final Map<Buttons, Panels> buttonIndexToPanelIndex = Map.of(
                Buttons.PLAY, Panels.PLAY_MENU,
                Buttons.SETTINGS, Panels.SETTINGS_MENU,
                Buttons.LEADERBOARD, Panels.LEADERBOARD_MENU);

        buttonIndexToPanelIndex.forEach((button, panel) -> {
            buttons.get(button.getIndex()).addActionListener(e -> {
                GameWindow parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
                new SwitchToOtherPanel(parent, panel).actionPerformed(e);
            });

        });
    }

    @Override
    public void addButtonsToPanel() {
        buttons.stream().forEach(b -> {
            gbc.gridy = buttons.indexOf(b);
            this.add(b, gbc);
        });
    }

    @Override
    public void unselectAllButtons() {
    }

    @Override
    public void setPreferredSize(Dimension size) {
        this.removeAll();
        gbc.insets = new Insets(0, 0, (int) (size.getHeight() * BUTTONS_PADDING_PROPORTION), 0);
        buttons.forEach(b -> b.setPreferredSize(size));
        this.addButtonsToPanel();
    }

}
