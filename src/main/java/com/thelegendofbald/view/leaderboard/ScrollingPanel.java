package com.thelegendofbald.view.leaderboard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JScrollPane;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.common.BackToMainPanel;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

class ScrollingPanel extends AdapterPanel {

    private static final double WIDTH_INSETS = 0.3;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private Optional<JScrollPane> scrollPane = Optional.empty();
    private Optional<ContentPanel> contentPanel = Optional.empty();

    ScrollingPanel(final Dimension size) {
        super(new Dimension(0, 0));
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
        this.gbc.insets.set(0, (int) (this.getWidth() * WIDTH_INSETS - this.getWidth() * BackToMainPanel.WIDTH_PROPORTION), 0, (int) (this.getWidth() * WIDTH_INSETS));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        this.scrollPane.ifPresent(sp -> this.add(sp, this.gbc));
    }

}
