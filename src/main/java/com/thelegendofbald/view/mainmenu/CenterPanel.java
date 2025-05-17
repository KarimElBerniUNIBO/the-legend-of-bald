package com.thelegendofbald.view.mainmenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.thelegendofbald.api.buttons.JButtonFactory;
import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.mainmenu.Buttons;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.api.panels.InteractivePanel;
import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.controller.ui.mainmenu.SwitchToOtherPanel;
import com.thelegendofbald.view.buttons.JButtonFactoryImpl;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.main.GameWindow;

final class CenterPanel extends AdapterPanel implements InteractivePanel {

    private static final long serialVersionUID = -5181397653556589255L;

    private static final double BUTTONS_BOTTOM_INSETS_PROPORTION = 0.05;
    private static final double BUTTONS_LEFT_RIGHT_INSETS_PROPORTION = 0.25;

    private static final double DEFAULT_ARC_PROPORTION = 0.2;

    private transient final JButtonFactory buttonFactory = new JButtonFactoryImpl();
    private transient final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<JButton> buttons = this.getListOfButtons();

    CenterPanel(final Dimension size) {
        super(size);
        this.setOpaque(false);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        this.setLayout(new GridBagLayout());
        this.connectButtonsWithActionListeners();
        this.updateComponentsSize();
    }

    private List<JButton> getListOfButtons() {
        return Stream.iterate(0, i -> i <= Buttons.getMaxIndex(), i -> i + 1)
                .map(i -> (JButton) buttonFactory.createRoundedButton(Buttons.getIndex(i).getName(), // NOPMD
                        Optional.empty(), DEFAULT_ARC_PROPORTION, Optional.empty(), Optional.empty(),
                        Optional.of(Color.BLACK), Optional.empty()))
                .toList();
    }
    /*
     * Suppresses the unchecked cast warning because the
     * buttonFactory.createRoundedButton method
     * returns a RoundedButton, which is a subclass of JButton. The cast is
     * necessary to maintain compatibility with the List<JButton> type used in this
     * class.
     */

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

    @Override
    public void unselectAllButtons() {
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        this.updateComponentsSize();
        this.updateView();
    }

    @Override
    public void updateComponentsSize() {
        gbc.insets.set(0, (int) (this.getWidth() * BUTTONS_LEFT_RIGHT_INSETS_PROPORTION),
                (int) (this.getHeight() * BUTTONS_BOTTOM_INSETS_PROPORTION),
                (int) (this.getWidth() * BUTTONS_LEFT_RIGHT_INSETS_PROPORTION));
    }

    @Override
    public void addComponentsToPanel() {
        buttons.stream().forEach(b -> {
            gbc.gridy = buttons.indexOf(b);
            this.add(b, gbc);
        });
    }

}
