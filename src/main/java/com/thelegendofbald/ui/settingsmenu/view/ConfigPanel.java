package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JPanel;

class ConfigPanel extends JPanel {

    private final Random random = new Random();

    ConfigPanel(Dimension size) {
        this.setPreferredSize(size);
        this.setBackground(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
    }

}
