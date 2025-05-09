package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.mainmenu.model.TitleLabel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;

public class ConfigPanel extends AdapterPanel {

    private static final double TITLE_PROPORTION = 0.5;
    private static final int UP_DOWN_INSETS = 2;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createHorizontalGridBagConstraints();

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private Optional<TitleLabel> title = Optional.empty();
    private final String text;
    private final JComponent values;


    public ConfigPanel(String text, JComponent values) {
        super(new Dimension(0,0));
        this.text = text;
        this.values = values;

        this.setOpaque(false);
        this.setBorder(new MatteBorder(UP_DOWN_INSETS, 0, UP_DOWN_INSETS, 0, Color.WHITE));
        this.setLayout(new GridBagLayout());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.title.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                this.initializeComponents();
                this.removeAll();
                this.addComponentsToPanel();
                this.revalidate();
                this.repaint();
            });
        }
    }

    @Override
    protected void initializeComponents() {
        this.title = Optional.of(tlFactory.createTitleLabelWithProportion(this.text, this.getSize(), Optional.of(new Pair<>(1.0,1.0)), Optional.empty(), Optional.empty()));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();

        gbc.gridx = 0;
        this.title.ifPresent(t -> this.add(t, gbc));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        this.add(values, gbc);
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateComponentsSize);
    }

    @Override
    public void updateComponentsSize() {
        this.title.ifPresent(t -> t.setPreferredSize(this.getSize()));
        this.values.setPreferredSize(this.getSize());
    }

}
