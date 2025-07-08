package com.thelegendofbald.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.common.TextLabelFactory;
import com.thelegendofbald.api.game_options.Buttons;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.controller.common.SwitchToOtherPanel;
import com.thelegendofbald.view.common.TextLabel;
import com.thelegendofbald.view.common.TextLabelFactoryImpl;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.main.GameWindow;

public final class GameOptionsPanel extends AdapterPanel {

    private static final Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 180);
    private static final Pair<Double, Double> TITLE_PROPORTION = Pair.of(1.0, 0.3);

    private static final double WIDTH_INSETS = 0.1;
    private static final double HEIGHT_INSETS = 0.05;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private Optional<TextLabel> title = Optional.empty();

    private final List<JButton> buttons = this.getButtonsList();

    public GameOptionsPanel(final Dimension size) {
        super(new Dimension(0, 0));
        this.setLayout(new GridBagLayout());
        this.setVisible(false);
        SwingUtilities.invokeLater(() -> this.setBackground(DEFAULT_BG_COLOR));
    }

    @Override
    protected void initializeComponents() {
        this.title = Optional.of(this.tlFactory.createTextLabelWithProportion("OPTIONS", this.getSize(),
                Optional.of(TITLE_PROPORTION), Optional.empty(), Optional.empty(), Optional.empty()));
        this.connectButtonsWithActionListeners();
        super.initializeComponents();
    }

    private List<JButton> getButtonsList() {
        return Arrays.stream(Buttons.values())
                .map(Buttons::getButton)
                .toList();
    }

    private void connectButtonsWithActionListeners() {
        Arrays.stream(Buttons.values())
                .forEach(enumButton -> {
                    enumButton.getPanel().ifPresentOrElse(
                            panel -> enumButton.getButton()
                                    .addActionListener(e -> {
                                        new SwitchToOtherPanel((GameWindow) SwingUtilities.getWindowAncestor(this),
                                                panel).actionPerformed(e);
                                        if (enumButton == Buttons.LEAVE) {
                                            ((GamePanel) this.getParent()).stopGame();
                                            this.setVisible(false);
                                        }
                                    }),
                            () -> enumButton.getButton().addActionListener(e -> this.setVisible(false)));
                });
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents())
                .forEach(component -> component.setPreferredSize(this.getSize()));
    }

    @Override
    public void addComponentsToPanel() {
        this.gbc.insets.set(0, (int) (this.getWidth() * WIDTH_INSETS), (int) (this.getHeight() * HEIGHT_INSETS),
                (int) (this.getWidth() * WIDTH_INSETS));

        this.title.ifPresent(t -> {
            this.gbc.gridy = 0;
            this.add(t, this.gbc);
        });
        this.buttons.forEach(button -> {
            this.gbc.gridy = this.buttons.indexOf(button) + 1;
            this.add(button, this.gbc);
        });

        this.updateComponentsSize();
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

}
