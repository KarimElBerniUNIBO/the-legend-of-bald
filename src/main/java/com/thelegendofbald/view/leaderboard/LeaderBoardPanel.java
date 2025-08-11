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

/**
 * A panel displaying the leaderboard.
 */
public final class LeaderBoardPanel extends MenuPanel {

    private static final long serialVersionUID = 1L;

    private static final Pair<Double, Double> TITLE_PROPORTION = Pair.of(1.0, 0.3);

    private final transient TextLabelFactory titleLabelFactory = new TextLabelFactoryImpl();

    private transient Optional<BackToPreviousPanel> backToMainPanel = Optional.empty();
    private transient Optional<TextLabel> titleLabel = Optional.empty();
    private transient Optional<JPanel> scrollingPanel = Optional.empty();

    @Override
    protected void initializeComponents() {
        this.backToMainPanel = Optional.of(new BackToPreviousPanel());
        this.titleLabel = Optional.of(titleLabelFactory.createTextLabelWithProportion("LEADERBOARD",
                this.getSize(), Optional.of(TITLE_PROPORTION), Optional.empty(),
                Optional.empty(), Optional.empty()));
        this.scrollingPanel = Optional.of(new ScrollingPanel());
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
