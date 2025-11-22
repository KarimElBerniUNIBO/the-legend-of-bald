package com.thelegendofbald.view.panel.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.model.config.SettingsEditorsManager;
import com.thelegendofbald.view.component.TextLabel;
import com.thelegendofbald.view.component.TextLabelFactoryImpl;

final class NorthPanel extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    private static final String TITLE_TEXT = "SETTINGS";
    private static final double HEIGHT_PROPORTION = 0.35;

    private final transient TextLabelFactoryImpl tlFactory = new TextLabelFactoryImpl();

    private final SettingsEditorsManager sem;
    private TextLabel titleLabel;
    private JPanel categoriesPanel;

    NorthPanel(final SettingsEditorsManager sem) {
        super();
        this.sem = sem;
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void initializeComponents() {
        this.titleLabel = tlFactory.createTextLabelWithProportion(TITLE_TEXT, this.getSize(),
                Optional.of(Pair.of(1.0, 0.5)), Optional.empty(), Optional.empty(),
                Optional.empty());
        this.categoriesPanel = new CategoriesPanel(this.sem);
        super.initializeComponents();
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
