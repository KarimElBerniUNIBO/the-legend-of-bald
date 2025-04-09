package com.thelegendofbald.ui.mainmenu.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    private static final String TITLE_TEXT = "THE LEGEND OF BALD";
    private static final String TITLE_FONT_NAME = "Serif";

    private final JLabel titleLabel;
    private final JPanel centerPanel;

    public MenuPanel(Dimension size) {
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());

        titleLabel = new TitleLabel(
            TITLE_TEXT,
            size,
            Color.WHITE,
            TITLE_FONT_NAME);
        
        centerPanel = new CenterPanel(size);

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

}
