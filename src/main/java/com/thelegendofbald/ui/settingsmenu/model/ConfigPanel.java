package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;

import java.awt.GridBagConstraints;

public class ConfigPanel extends JPanel {

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createHorizontalGridBagConstraints();

    private final Random random = new Random();

    public ConfigPanel(String text, JComponent values) {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        
        gbc.gridx = 0;
        this.add(new JLabel(text), gbc);

        gbc.gridx = 1;
        this.add(values, gbc);
    }
}
