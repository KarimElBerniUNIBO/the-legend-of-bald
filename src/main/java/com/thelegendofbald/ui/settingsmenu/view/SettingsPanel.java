package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.mainmenu.model.BackToMainPanel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SettingsPanel extends AdapterPanel {

    private final Fraction SCREEN_PROPORTION;

    private final int TITLE_WIDTH_PROPORTION;
    private final int TITLE_HEIGHT_PROPORTION;

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private JLabel titleLabel;
    private JPanel categoriesPanel;
    private SettingsEditorsManager sem;

    public SettingsPanel(Dimension size) {
        super(size);
        SCREEN_PROPORTION = new Fraction(size.getWidth() / size.getHeight());
        TITLE_WIDTH_PROPORTION = SCREEN_PROPORTION.getDenominator();
        TITLE_HEIGHT_PROPORTION = SCREEN_PROPORTION.getNumerator();

        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public void addComponentsToPanel() {
        this.titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", this.getSize(), new Dimension(TITLE_WIDTH_PROPORTION, TITLE_HEIGHT_PROPORTION), Optional.empty(), Optional.empty());
        this.sem = new SettingsEditorPanel(this.getSize());
        this.categoriesPanel = new CategoriesPanel(this.getSize(), sem);

        //this.add(new BackToMainPanel(this.getSize()));
        this.add(titleLabel);
        this.add(categoriesPanel);
        this.add((JPanel) sem);

        super.addComponentsToPanel();
    }

}
