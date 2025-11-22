package com.thelegendofbald.view.panel.leaderboard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.thelegendofbald.view.layout.GridBagConstraintsFactory;
import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.view.component.BackToPreviousPanel;
import com.thelegendofbald.view.layout.GridBagConstraintsFactoryImpl;

class ScrollingPanel extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    private static final double WIDTH_INSETS = 0.25;
    private static final double BOTTOM_INSET = 0.05;

    private static final double VERTICAL_SCROLLBAR_UNIT_INCREMENT = 0.1;
    private static final double VERTICAL_SCROLLBAR_BLOCK_INCREMENT = 0.25;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private transient Optional<JScrollPane> scrollPane = Optional.empty();
    private transient Optional<ContentPanel> contentPanel = Optional.empty();

    ScrollingPanel() {
        super();
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setLayout(new GridBagLayout());
        });
    }

    @Override
    protected void initializeComponents() {
        this.contentPanel = Optional.of(new ContentPanel());
        this.scrollPane = Optional.of(new JScrollPane(this.contentPanel.get()));
        this.scrollPane.ifPresent(sp -> {
            sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        });
        super.initializeComponents();
    }

    @Override
    public void updateComponentsSize() {
        final var gbcLeft = (int) (this.getWidth() * WIDTH_INSETS - this.getWidth() * BackToPreviousPanel.WIDTH_PROPORTION);
        final var gbcRight = (int) (this.getWidth() * WIDTH_INSETS);
        final var gbcBottom = (int) (this.getHeight() * BOTTOM_INSET);

        final var preferredWith = this.getWidth() - (gbcRight * 2);
        final var preferredHeight = this.getHeight() - gbcBottom;
        final var preferedContentPanelSize = new Dimension(preferredWith, preferredHeight);

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
