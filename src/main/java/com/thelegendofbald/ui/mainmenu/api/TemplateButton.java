package com.thelegendofbald.ui.mainmenu.api;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public abstract class TemplateButton extends JButton {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    private static final String DEFAULT_FONT_NAME = Font.SANS_SERIF;
    private static final int DEFAULT_FONT_TYPE = Font.BOLD;
    private static final int PROPORTION = 20;

    public TemplateButton(String text, Dimension size) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setForeground(DEFAULT_FOREGROUND_COLOR);
        this.setFont(new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_TYPE, (int) size.getWidth() / PROPORTION));
        this.setFocusable(false);
    }

}
