package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.mainmenu.model.BackToMainPanel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SettingsPanel extends JPanel {

    private final Fraction PROPORTION;

    private final int TITLE_WIDTH_PROPORTION;
    private final int TITLE_HEIGHT_PROPORTION;

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private final JLabel titleLabel;
    private final JPanel categoriesPanel;
    private final SettingsEditorsManager sem;

    public SettingsPanel(Dimension size) {
        PROPORTION = new Fraction(size.getWidth() / size.getHeight());
        TITLE_WIDTH_PROPORTION = PROPORTION.getDenominator();
        TITLE_HEIGHT_PROPORTION = PROPORTION.getNumerator();

        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", size, new Dimension(TITLE_WIDTH_PROPORTION, TITLE_HEIGHT_PROPORTION), Optional.empty(), Optional.empty());
        this.sem = new SettingsEditorPanel(size);
        this.categoriesPanel = new CategoriesPanel(size, sem);

        this.add(new BackToMainPanel(size));
        this.add(titleLabel);
        this.add(categoriesPanel);
        this.add((JPanel) sem);
    }

}
