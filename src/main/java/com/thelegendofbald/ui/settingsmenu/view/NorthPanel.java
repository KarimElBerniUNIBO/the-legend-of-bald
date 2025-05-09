package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

class NorthPanel extends AdapterPanel {

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private final SettingsEditorsManager sem;
    private TitleLabel titleLabel;
    private JPanel categoriesPanel;

    public NorthPanel(Dimension size, SettingsEditorsManager sem) {
        super(size);
        this.sem = sem;
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void initializeComponents() {
        this.titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", this.getSize(), Optional.of(new Pair<>(1.0, 0.5)), Optional.empty(),
                Optional.empty());
        this.categoriesPanel = new CategoriesPanel(this.getSize(), this.sem);
            
    }

    /*private void updateSize() {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(this.getSize()));
    }*/

    @Override
    public void addComponentsToPanel() {
        this.add(this.titleLabel, BorderLayout.NORTH);
        this.add(this.categoriesPanel, BorderLayout.CENTER);
        this.updateComponentsSize();
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(new Dimension((int) size.getWidth(), (int) (size.getHeight() * 0.35)));
        SwingUtilities.invokeLater(this::updateComponentsSize);
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(this.getSize()));
    }

}
