package com.thelegendofbald.view.leaderboard;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.TextLabelFactory;
import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.view.common.BackToPreviousPanel;
import com.thelegendofbald.view.common.TextLabel;
import com.thelegendofbald.view.common.TextLabelFactoryImpl;

public final class LeaderBoardPanel extends MenuPanel {

    private transient final TextLabelFactory titleLabelFactory = new TextLabelFactoryImpl();

    private transient Optional<BackToPreviousPanel> backToMainPanel = Optional.empty();
    private transient Optional<TextLabel> titleLabel = Optional.empty();
    private transient Optional<JPanel> scrollingPanel = Optional.empty();

    @Override
    protected void initializeComponents() {
        this.backToMainPanel = Optional.of(new BackToPreviousPanel(this.getSize()));
        this.titleLabel = Optional.of(titleLabelFactory.createTextLabelWithProportion("LEADERBOARD",
                this.getSize(), Optional.of(Pair.of(1.0, 0.3)), Optional.empty(),
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
