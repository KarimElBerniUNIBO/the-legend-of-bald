package com.thelegendofbald.view.leaderboard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JScrollPane;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.common.BackToPreviousPanel;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

class ScrollingPanel extends AdapterPanel {

    private static final double WIDTH_INSETS = 0.3;
    private static final double BOTTOM_INSET = 0.05;

    private static final double VERTICAL_SCROLLBAR_UNIT_INCREMENT = 0.1;
    private static final double VERTICAL_SCROLLBAR_BLOCK_INCREMENT = 0.25;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private Optional<JScrollPane> scrollPane = Optional.empty();
    private Optional<ContentPanel> contentPanel = Optional.empty();

    ScrollingPanel(final Dimension size) {
        super(size);
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void initializeComponents() {
        this.contentPanel = Optional.of(new ContentPanel(this.getSize()));
        this.scrollPane = Optional.of(new JScrollPane(this.contentPanel.get()));
        this.scrollPane.ifPresent(sp -> {
            sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        });
        super.initializeComponents();
    }

    @Override
    public void updateComponentsSize() {
        var gbcLeft = (int) (this.getWidth() * WIDTH_INSETS - this.getWidth() * BackToPreviousPanel.WIDTH_PROPORTION);
        var gbcRight = (int) (this.getWidth() * WIDTH_INSETS);
        var gbcBottom = (int) (this.getHeight() * BOTTOM_INSET);

        var preferredWith = (int) (this.getWidth() - (gbcRight * 2));
        var preferredHeight = (int) (this.getHeight() - gbcBottom);
        var preferedContentPanelSize = new Dimension(preferredWith, preferredHeight);
        
        this.gbc.insets.set(0, gbcLeft, gbcBottom, gbcRight);
        this.contentPanel.ifPresent(cp -> cp.setPreferredSize(preferedContentPanelSize));
        this.scrollPane.ifPresent(sp -> {
            sp.getVerticalScrollBar().setUnitIncrement((int) (this.getHeight() * VERTICAL_SCROLLBAR_UNIT_INCREMENT));
            sp.getVerticalScrollBar().setBlockIncrement((int) (this.getHeight() * VERTICAL_SCROLLBAR_BLOCK_INCREMENT));
        });
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        this.scrollPane.ifPresent(sp -> this.add(sp, this.gbc));
    }

}
