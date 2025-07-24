package com.thelegendofbald.view.leaderboard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.common.TextLabelFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.common.TextLabel;
import com.thelegendofbald.view.common.TextLabelFactoryImpl;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

final class PlayerTimePanel extends AdapterPanel {

    private transient final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private transient final TextLabelFactory tlFactory = new TextLabelFactoryImpl();

    private final String playerName;
    private final String playerTime;

    private transient Optional<TextLabel> playerNameText = Optional.empty();
    private transient Optional<TextLabel> playerTimeText = Optional.empty();

    PlayerTimePanel(String playerName, String playerTime) {
        super(new Dimension(0, 0));
        this.setLayout(new GridBagLayout());
        this.playerName = playerName;
        this.playerTime = playerTime;
    }

    @Override
    protected void initializeComponents() {
        this.setOpaque(true);
        this.playerNameText = Optional.of(tlFactory.createTextLabelWithProportion(playerName, this.getSize(), Optional.of(Pair.of(0.5, 1.0)), Optional.empty(), Optional.empty(), Optional.empty()));
        this.playerTimeText = Optional.of(tlFactory.createTextLabelWithProportion(playerTime, this.getSize(), Optional.of(Pair.of(0.5, 1.0)), Optional.empty(), Optional.empty(), Optional.empty()));
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
