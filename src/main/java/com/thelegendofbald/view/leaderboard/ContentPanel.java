package com.thelegendofbald.view.leaderboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.SwingUtilities;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.model.common.DataManager;
import com.thelegendofbald.model.common.GameRun;
import com.thelegendofbald.model.common.Timer.TimeData;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

final class ContentPanel extends AdapterPanel {

    private static final int MAX_PLAYERS = 10;

    private static final Color GOLD_COLOR = new Color(255, 215, 0);
    private static final Color SILVER_COLOR = new Color(192, 192, 192);
    private static final Color BRONZE_COLOR = new Color(205, 127, 50);
    private static final Color DEFAULT_COLOR = new Color(80, 80, 80);

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<PlayerTimePanel> players = new ArrayList<>(MAX_PLAYERS);
    private final DataManager dataManager = new DataManager();

    ContentPanel(final Dimension size) {
        super(size);
    }

    @Override
    protected void initializeComponents() {
        this.setLayout(new GridBagLayout());
        players.clear();
        this.setPlayersListFromData();
        this.applyRankingColors(players);
        super.initializeComponents();
    }

    private void applyRankingColors(List<PlayerTimePanel> players) {
        players.forEach(p -> {
            int index = players.indexOf(p);
            switch (index) {
                case 0 -> SwingUtilities.invokeLater(() -> p.setBackground(GOLD_COLOR));
                case 1 -> SwingUtilities.invokeLater(() -> p.setBackground(SILVER_COLOR));
                case 2 -> SwingUtilities.invokeLater(() -> p.setBackground(BRONZE_COLOR));
                default -> SwingUtilities.invokeLater(() -> p.setBackground(DEFAULT_COLOR));
            }
        });

    }

    private void setPlayersListFromData() {
        List<GameRun> runs = dataManager.loadGameRuns();
        runs.sort(Comparator.comparing(GameRun::timedata, this::compareTimeData));

        players.addAll(
                Stream.concat(
                        runs.stream()
                                .limit(MAX_PLAYERS)
                                .map(run -> new PlayerTimePanel(run.name(), run.timedata().toString())),
                        Stream.generate(() -> new PlayerTimePanel("???", "hh:mm:ss"))
                                .limit(Math.max(0, MAX_PLAYERS - runs.size())))
                .toList());
    }

    // Confronta TimeData
    private int compareTimeData(TimeData t1, TimeData t2) {
        int h = Integer.compare(t1.hours(), t2.hours());
        if (h != 0)
            return h;
        int m = Integer.compare(t1.minutes(), t2.minutes());
        if (m != 0)
            return m;
        return Integer.compare(t1.seconds(), t2.seconds());
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
