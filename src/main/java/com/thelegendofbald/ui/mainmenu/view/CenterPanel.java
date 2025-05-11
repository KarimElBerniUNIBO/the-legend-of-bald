package com.thelegendofbald.ui.mainmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collections;
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

    private static final double BUTTONS_BOTTOM_INSETS_PROPORTION = 0.05;
    private static final double BUTTONS_LEFT_RIGHT_INSETS_PROPORTION = 0.25;

    private static final double DEFAULT_ARC_PROPORTION = 0.2;

    private final JButtonFactory buttonFactory = new JButtonFactoryImpl();
    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();

    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private List<JButton> buttons = Collections.emptyList();

    CenterPanel(final Dimension size) {
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.buttons.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                this.buttons = this.getListOfButtons(this.getSize());
                this.connectButtonsWithActionListeners();
                this.addButtonsToPanel();
            });
        }
    }

    private List<JButton> getListOfButtons(final Dimension size) {
        return Stream.iterate(0, i -> i <= Buttons.getMaxIndex(), i -> i + 1)
                .map(i -> (JButton) buttonFactory.createRoundedButton(Buttons.getIndex(i).getName(), size,
                        Optional.empty(),
                        DEFAULT_ARC_PROPORTION, Optional.empty(), Optional.empty(), Optional.of(Color.BLACK),
                        Optional.empty()))
                .toList();
    }

    private void connectButtonsWithActionListeners() {
        final Map<Buttons, Panels> buttonIndexToPanelIndex = Map.of(
                Buttons.PLAY, Panels.PLAY_MENU,
                Buttons.SETTINGS, Panels.SETTINGS_MENU,
                Buttons.LEADERBOARD, Panels.LEADERBOARD_MENU);

        buttonIndexToPanelIndex.forEach((button, panel) -> {
            buttons.get(button.getIndex()).addActionListener(e -> {
                final var parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
                new SwitchToOtherPanel(parent, panel).actionPerformed(e);
            });

        });
    }

    private void addButtonsToPanel() {
        buttons.stream().forEach(b -> {
            gbc.gridy = buttons.indexOf(b);
            this.add(b, gbc);
        });
    }

    @Override
    public void unselectAllButtons() {
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        this.removeAll();
        gbc.insets.set(0, (int) (size.getWidth() * BUTTONS_LEFT_RIGHT_INSETS_PROPORTION),
                (int) (size.getHeight() * BUTTONS_BOTTOM_INSETS_PROPORTION),
                (int) (size.getWidth() * BUTTONS_LEFT_RIGHT_INSETS_PROPORTION));
        this.addButtonsToPanel();
    }

}
