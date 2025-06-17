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

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private Optional<TextLabel> playerName = Optional.empty();
    private Optional<TextLabel> playerTime = Optional.empty();

    PlayerTimePanel() {
        super(new Dimension(0, 0));
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void initializeComponents() {
        this.setOpaque(true);
        this.playerName = Optional.of(tlFactory.createTextLabelWithProportion("???", this.getSize(), Optional.of(Pair.of(0.5, 1.0)), Optional.empty(), Optional.empty(), Optional.empty()));
        this.playerTime = Optional.of(tlFactory.createTextLabelWithProportion("hh:mm:ss", this.getSize(), Optional.of(Pair.of(0.5, 1.0)), Optional.empty(), Optional.empty(), Optional.empty()));
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
        this.playerName.ifPresent(name -> {
            this.add(name, gbc);
        });

        this.gbc.gridx = 1;
        this.playerTime.ifPresent(time -> {
            this.add(time, gbc);
        });

        this.updateComponentsSize();
    }

}
