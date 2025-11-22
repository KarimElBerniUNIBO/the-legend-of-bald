package com.thelegendofbald.view.panel.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.layout.GridBagConstraintsFactory;
import com.thelegendofbald.view.factory.TextLabelFactory;
import com.thelegendofbald.model.system.Game;
import com.thelegendofbald.model.config.ButtonsSettings;
import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.controller.navigation.SwitchToOtherPanel;
import com.thelegendofbald.view.component.TextLabel;
import com.thelegendofbald.view.component.TextLabelFactoryImpl;
import com.thelegendofbald.view.layout.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.window.GameWindow;

/**
 * The {@code GameOptionsPanel} class represents the options panel in the game.
 * It provides buttons for various game options and handles their actions.
 */
public final class GameOptionsPanel extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    private static final Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 180);
    private static final Pair<Double, Double> TITLE_PROPORTION = Pair.of(1.0, 0.3);

    private static final double WIDTH_INSETS = 0.1;
    private static final double HEIGHT_INSETS = 0.05;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final transient TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private transient Optional<TextLabel> title = Optional.empty();

    private final List<JButton> buttons = this.getButtonsList();

    /**
     * Constructs a new {@code GameOptionsPanel} instance.
     * Initializes the panel with a grid bag layout and sets its visibility to false.
     */
    public GameOptionsPanel() {
        super();
        this.setLayout(new GridBagLayout());
        this.setVisible(false);
        this.setOpaque(false);
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
        return Arrays.stream(ButtonsSettings.values())
                .map(ButtonsSettings::getButton)
                .toList();
    }

    private void connectButtonsWithActionListeners() {
        Arrays.stream(ButtonsSettings.values())
                .forEach(enumButton -> {
                    enumButton.getPanel().ifPresentOrElse(
                            panel -> enumButton.getButton()
                                    .addActionListener(e -> {
                                        if (enumButton == ButtonsSettings.LEAVE) {
                                            ((Game) this.getParent()).stopGame();
                                            this.setVisible(false);
                                        }
                                        new SwitchToOtherPanel((GameWindow) SwingUtilities.getWindowAncestor(this),
                                                panel).actionPerformed(e);
                                    }),
                            () -> enumButton.getButton().addActionListener(e -> {
                                if (enumButton == ButtonsSettings.RESUME) {
                                    ((Game) this.getParent()).resumeGame();
                                }
                                this.setVisible(false);
                            }));
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

    @Override
    public void paintComponent(final Graphics g) {
        g.setColor(DEFAULT_BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
