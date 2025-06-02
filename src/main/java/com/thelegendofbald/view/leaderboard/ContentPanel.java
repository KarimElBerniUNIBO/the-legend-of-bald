package com.thelegendofbald.view.leaderboard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

final class ContentPanel extends AdapterPanel {

    private static final int MAX_PLAYERS = 10;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();
    
    private final List<PlayerTimePanel> players = new ArrayList<>(MAX_PLAYERS);

    ContentPanel(final Dimension size) {
        super(size);
    }

    @Override
    protected void initializeComponents() {
        this.setLayout(new GridBagLayout());
        players.addAll(this.getPlayersList());
        super.initializeComponents();
    }

    private List<PlayerTimePanel> getPlayersList() {
        return Stream.generate(PlayerTimePanel::new)
                .limit(MAX_PLAYERS)
                .toList();
    }

    @Override
    public void updateComponentsSize() {
        this.players.forEach(player -> {
            player.setPreferredSize(this.getSize());
        });
        this.gbc.insets.set(0, 0, (int) (this.getHeight() * 0.01), 0);
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        players.forEach(player -> {
            this.gbc.gridy = players.indexOf(player);
            this.add(player, gbc);
        });
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        var preferredSize = new Dimension((int) size.getWidth(), (int) (size.getHeight() * 2.25));
        super.setPreferredSize(preferredSize);
    }

}
