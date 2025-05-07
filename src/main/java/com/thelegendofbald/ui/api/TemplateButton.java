package com.thelegendofbald.ui.api;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.util.Pair;


public abstract class TemplateButton extends JButton {
    
    protected static final double PARENT_FONT_PROPORTION = 0.1;

    private final Pair<Double, Double> moltiplicator;

    public TemplateButton(final String text, final Dimension parentSize, final Pair<Double, Double> moltiplicator, final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        super();
        this.moltiplicator = moltiplicator;
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fontColor);
        this.setFont(new Font(fontName, fontType, this.calculateFontSize(parentSize)));
        this.initialize();
    }

    public TemplateButton(final ImageIcon icon, final Dimension parentSize, final Pair<Double, Double> moltiplicator, final Color bgColor, final Color fgColor) {
        super();
        this.moltiplicator = moltiplicator;
        this.setIcon(icon);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fgColor);
        this.initialize();
    }

    private void initialize() {
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.setFocusPainted(false);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.getText() != null && !this.getText().isEmpty()) {
            SwingUtilities.invokeLater(() -> this.setFont(this.getFont().deriveFont((float) this.calculateFontSize(this.getParent().getSize()))));
        }
    }

    private int calculateFontSize(Dimension size) {
        return (int) (Math.min(size.getWidth() * this.moltiplicator.getFirst(), size.getHeight() * this.moltiplicator.getSecond()) * PARENT_FONT_PROPORTION);
    }

}
