package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;

import java.awt.GridBagConstraints;
import java.util.Optional;

import com.thelegendofbald.ui.view.GameWindow;

public class ConfigPanel extends JPanel {

    private static final int UP_DOWN_INSETS = 2;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createHorizontalGridBagConstraints();

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private final String text;
    private final JComponent values;

    private boolean initialized = false;

    public ConfigPanel(String text, JComponent values) {
        this.text = text;
        this.values = values;

        this.setOpaque(false);
        this.setBorder(new MatteBorder(UP_DOWN_INSETS, 0, UP_DOWN_INSETS, 0, Color.WHITE));
        this.setLayout(new GridBagLayout());
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    public void onResize() {
        if (!initialized && this.getWidth() > 0 && this.getHeight() > 0) {
            this.addComponentsToPanel();
            initialized = true;
        }
    }

    public void addComponentsToPanel() {
        var window = (GameWindow) SwingUtilities.getWindowAncestor(this);
        Fraction proportion = new Fraction(window.getSize().getWidth() / window.getSize().getHeight());
        Dimension tlProportion = new Dimension(proportion.getNumerator(), proportion.getDenominator());

        gbc.gridx = 0;
        this.add(tlFactory.createTitleLabelWithProportion(text, this.getSize(), tlProportion, Optional.empty(),
                Optional.empty()), gbc);

        gbc.gridx = 1;
        this.add(values, gbc);

        this.revalidate();
        this.repaint();
    }

}
