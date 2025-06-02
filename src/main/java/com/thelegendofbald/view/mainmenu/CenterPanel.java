package com.thelegendofbald.view.mainmenu;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.mainmenu.Buttons;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.api.panels.InteractivePanel;
import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.controller.common.SwitchToOtherPanel;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.main.GameWindow;

final class CenterPanel extends AdapterPanel implements InteractivePanel {

    private static final long serialVersionUID = -5181397653556589255L;

    private static final double BUTTONS_BOTTOM_INSETS_PROPORTION = 0.05;
    private static final double BUTTONS_LEFT_RIGHT_INSETS_PROPORTION = 0.25;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
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
        return Stream.of(Buttons.values())
                .map(Buttons::getButton)
                .toList();
    }

    private void connectButtonsWithActionListeners() {
        Arrays.stream(Panels.values()).forEach(panel -> {
            panel.getEnumButton().ifPresent(enumButton -> enumButton.getButton().addActionListener(e -> {
                final var parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
                new SwitchToOtherPanel(parent, panel).actionPerformed(e);
            }));
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
