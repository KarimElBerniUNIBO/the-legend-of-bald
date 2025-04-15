package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;

public class SettingsPanel extends JPanel {

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private final JLabel titleLabel;

    public SettingsPanel(Dimension size) {
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", size, new Dimension(2,3), Optional.empty(), Optional.empty());

        this.add(titleLabel);
        this.add(new CategoriesPanel(size));
        this.add(new ConfigPanel(size));
    }

}
