package com.thelegendofbald.view.panel.leaderboard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.layout.GridBagConstraintsFactory;
import com.thelegendofbald.view.factory.TextLabelFactory;
import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.view.component.TextLabel;
import com.thelegendofbald.view.component.TextLabelFactoryImpl;
import com.thelegendofbald.view.layout.GridBagConstraintsFactoryImpl;

/**
 * A panel displaying a player's time in the leaderboard.
 */
final class PlayerTimePanel extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final transient TextLabelFactory tlFactory = new TextLabelFactoryImpl();

    private final String playerName;
    private final String playerTime;

    private transient Optional<TextLabel> playerNameText = Optional.empty();
    private transient Optional<TextLabel> playerTimeText = Optional.empty();

    PlayerTimePanel(final String playerName, final String playerTime) {
        super();
        this.playerName = playerName;
        this.playerTime = playerTime;
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setLayout(new GridBagLayout());
            this.setOpaque(true);
        });
    }

    @Override
    protected void initializeComponents() {
        this.playerNameText = Optional.of(tlFactory.createTextLabelWithProportion(playerName, this.getSize(),
                Optional.of(Pair.of(0.5, 1.0)), Optional.empty(), Optional.empty(), Optional.empty()));
        this.playerTimeText = Optional.of(tlFactory.createTextLabelWithProportion(playerTime, this.getSize(),
                Optional.of(Pair.of(0.5, 1.0)), Optional.empty(), Optional.empty(), Optional.empty()));
        super.initializeComponents();
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents())
                .forEach(component -> component.setPreferredSize(this.getSize()));
    }

    @Override
    public void addComponentsToPanel() {
        this.gbc.gridx = 0;
        this.playerNameText.ifPresent(name -> {
            this.add(name, gbc);
        });

        this.gbc.gridx = 1;
        this.playerTimeText.ifPresent(time -> {
            this.add(time, gbc);
        });

        this.updateComponentsSize();
    }

}
