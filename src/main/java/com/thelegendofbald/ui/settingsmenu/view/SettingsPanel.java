package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SettingsPanel extends AdapterPanel {

    private final Fraction SCREEN_PROPORTION;

    private final int TITLE_WIDTH_PROPORTION;
    private final int TITLE_HEIGHT_PROPORTION;

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private JPanel backToMainPanel;
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

        this.initializeComponents(size);
    }

    private void initializeComponents(Dimension size) {
        //this.backToMainPanel = new BackToMainPanel(size);
        this.titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", size, new Dimension(TITLE_WIDTH_PROPORTION, TITLE_HEIGHT_PROPORTION), Optional.empty(), Optional.empty());
        this.sem = new SettingsEditorPanel(size);
        this.categoriesPanel = new CategoriesPanel(size, sem);
    }

    public void updateSize(Dimension newSize) {
        this.titleLabel.setPreferredSize(newSize);
        ((JPanel)this.sem).setPreferredSize(newSize);
        this.categoriesPanel.setPreferredSize(newSize);

        this.revalidate();
        this.repaint();
    }

    @Override
    public void addComponentsToPanel() {
        //this.backToMainPanel = new BackToMainPanel(this.getSize());
        /*this.titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", this.getSize(), new Dimension(TITLE_WIDTH_PROPORTION, TITLE_HEIGHT_PROPORTION), Optional.empty(), Optional.empty());
        this.sem = new SettingsEditorPanel(this.getSize());
        this.categoriesPanel = new CategoriesPanel(this.getSize(), sem);*/

        this.updateSize(this.getSize());

        //this.add(this.backToMainPanel);
        this.add(this.titleLabel);
        this.add(this.categoriesPanel);
        this.add((JPanel) this.sem);

        super.addComponentsToPanel();
    }

}
