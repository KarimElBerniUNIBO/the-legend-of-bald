package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.mainmenu.model.BackToMainPanel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.mainmenu.view.SidePanel;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SettingsPanel extends AdapterPanel {

    private final Fraction SCREEN_PROPORTION;

    private final int TITLE_WIDTH_PROPORTION;
    private final int TITLE_HEIGHT_PROPORTION;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private JPanel backToMainPanel;
    private JLabel titleLabel;
    private JPanel categoriesPanel;
    private SettingsEditorsManager sem;
    private JPanel rightSidePanel;

    public SettingsPanel(Dimension size) {
        super(size);
        SCREEN_PROPORTION = new Fraction(size.getWidth() / size.getHeight());
        TITLE_WIDTH_PROPORTION = SCREEN_PROPORTION.getDenominator();
        TITLE_HEIGHT_PROPORTION = SCREEN_PROPORTION.getNumerator();

        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new GridBagLayout());

        this.initializeComponents(size);
    }

    private void initializeComponents(Dimension size) {
        this.backToMainPanel = new BackToMainPanel(size);
        this.titleLabel = tlFactory.createTitleLabelWithProportion("SETTINGS", size, new Dimension(TITLE_WIDTH_PROPORTION, TITLE_HEIGHT_PROPORTION), Optional.empty(), Optional.empty());
        this.sem = new SettingsEditorPanel(size);
        this.categoriesPanel = new CategoriesPanel(size, sem);
        this.rightSidePanel = new SidePanel(size, new Pair<>(1.0,1.0));
    }

    public void updateSize(Dimension size) {
        this.backToMainPanel.setMaximumSize(size);
        this.titleLabel.setPreferredSize(size);
        ((SettingsEditorPanel)this.sem).setPreferredSize(size);
        this.categoriesPanel.setPreferredSize(size);
        this.rightSidePanel.setPreferredSize(size);

        this.revalidate();
        this.repaint();
    }

    @Override
    public void addComponentsToPanel() {
        this.updateSize(this.getSize());

        gbc.gridy = 0;
        gbc.gridx = 0;
        this.add(this.backToMainPanel, gbc);

        gbc.gridy = 0;
        gbc.gridx = 1;
        this.add(this.titleLabel, gbc);

        gbc.gridy = 1;
        this.add(this.categoriesPanel, gbc);

        gbc.gridy = 2;
        this.add((JPanel) this.sem, gbc);

        gbc.gridy = 0;
        gbc.gridx = 2;
        this.add(this.rightSidePanel, gbc);
    }

}
