package com.thelegendofbald.view.leaderboard;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import com.thelegendofbald.api.panels.AdapterPanel;

final class PlayerTimePanel extends AdapterPanel {

    private final Random random = new Random();

    PlayerTimePanel(Dimension size) {
        super(size);
    }

    @Override
    protected void initializeComponents() {
        this.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        this.setOpaque(true);
        super.initializeComponents();
    }

    @Override
    public void updateComponentsSize() {
    }

    @Override
    public void addComponentsToPanel() {
    }

}
