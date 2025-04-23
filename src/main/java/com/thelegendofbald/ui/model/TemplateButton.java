package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;


public abstract class TemplateButton extends JButton {
    
    protected static final int PROPORTION = 25;

    public TemplateButton(final String text, final Dimension windowSize, final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fontColor);
        this.setFont(new Font(fontName, fontType, (int) windowSize.getWidth() / PROPORTION));
        this.setFocusable(false);
    }

}
