package com.thelegendofbald.view.leaderboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.TitleLabelFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.common.BackToMainPanel;
import com.thelegendofbald.view.common.TitleLabel;
import com.thelegendofbald.view.common.TitleLabelFactoryImpl;

public final class LeaderBoardPanel extends AdapterPanel {

    private final TitleLabelFactory titleLabelFactory = new TitleLabelFactoryImpl();

    private Optional<BackToMainPanel> backToMainPanel = Optional.empty();
    private Optional<TitleLabel> titleLabel = Optional.empty();
    private Optional<JPanel> scrollingPanel = Optional.empty();

    public LeaderBoardPanel(final Dimension size) {
        super(size);
        this.setPreferredSize(size);
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void initializeComponents() {
        this.backToMainPanel = Optional.of(new BackToMainPanel(this.getSize()));
        this.titleLabel = Optional.of(titleLabelFactory.createTitleLabelWithProportion("LEADERBOARD",
                this.getSize(), Optional.of(Pair.of(1.0, 0.3)),
                Optional.empty(), Optional.empty()));
        this.scrollingPanel = Optional.of(new ScrollingPanel(this.getSize()));
        super.initializeComponents();
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents()).forEach(component -> {
            component.setPreferredSize(this.getSize());
        });
    }

    @Override
    public void addComponentsToPanel() {
        this.backToMainPanel.ifPresent(b -> this.add(b, BorderLayout.WEST));
        this.titleLabel.ifPresent(t -> this.add(t, BorderLayout.NORTH));
        this.scrollingPanel.ifPresent(s -> this.add(s, BorderLayout.CENTER));
        this.updateComponentsSize();
    }

}
