package com.thelegendofbald.ui.api;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public abstract class TemplateButton extends JButton {
    
    protected static final double PROPORTION = 0.05;

    public TemplateButton(final String text, final Dimension windowSize, final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fontColor);
        this.setFont(new Font(fontName, fontType, (int) (windowSize.getWidth() * PROPORTION)));
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.setFocusPainted(false);
    }

    public TemplateButton(final ImageIcon icon, final Dimension windowSize, final Color bgColor, final Color fgColor) {
        this.setIcon(icon);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fgColor);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.setFocusPainted(false);
    }

}
