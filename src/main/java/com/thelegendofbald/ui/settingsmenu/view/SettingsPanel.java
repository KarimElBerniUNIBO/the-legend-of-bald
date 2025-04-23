package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SettingsPanel extends JPanel {

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private final JLabel titleLabel;
    private final JPanel categoriesPanel;
    private final SettingsEditorsManager sem;

    public SettingsPanel(Dimension size) {
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", size, new Dimension(2,3), Optional.empty(), Optional.empty());
        this.sem = new SettingsEditorPanel(size);
        this.categoriesPanel = new CategoriesPanel(size, sem);

        this.add(titleLabel);
        this.add(categoriesPanel);
        this.add((JPanel) sem);
    }

}
