package com.thelegendofbald.view.settingsmenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.api.settingsmenu.SettingsEditorsManager;
import com.thelegendofbald.view.common.TitleLabel;
import com.thelegendofbald.view.common.TitleLabelFactoryImpl;

class NorthPanel extends AdapterPanel {

    private static final String TITLE_TEXT = "SETTINGS";
    private static final double HEIGHT_PROPORTION = 0.35;

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private final SettingsEditorsManager sem;
    private TitleLabel titleLabel;
    private JPanel categoriesPanel;

    NorthPanel(final Dimension size, final SettingsEditorsManager sem) {
        super(size);
        this.sem = sem;
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void initializeComponents() {
        this.titleLabel = tlFactory.createTitleLabelWithProportion(TITLE_TEXT, this.getSize(),
                Optional.of(new Pair<>(1.0, 0.5)), Optional.empty(),
                Optional.empty());
        this.categoriesPanel = new CategoriesPanel(this.getSize(), this.sem);

    }

    @Override
    public void addComponentsToPanel() {
        this.add(this.titleLabel, BorderLayout.NORTH);
        this.add(this.categoriesPanel, BorderLayout.CENTER);
        this.updateComponentsSize();
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(new Dimension((int) size.getWidth(), (int) (size.getHeight() * HEIGHT_PROPORTION)));
        SwingUtilities.invokeLater(this::updateComponentsSize);
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(this.getSize()));
    }

}
