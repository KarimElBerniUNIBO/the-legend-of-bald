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
    private static final double TEXT_PROPORTION = 0.05;

    private final Pair<Double, Double> moltiplicator;

    public TemplateButton(final String text, final Dimension parentSize, final Pair<Double, Double> moltiplicator, final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        super();
        this.moltiplicator = moltiplicator;
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fontColor);
        this.setFont(new Font(fontName, fontType, this.getFontSize(parentSize)));
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
            SwingUtilities.invokeLater(() -> this.setFont(this.getFont().deriveFont((float) this.getFontSize(this.getParent().getSize()))));
        }
    }

    private int getFontSize(Dimension parentSize) {
        double width = parentSize.getWidth() * this.moltiplicator.getFirst();
        double height = parentSize.getHeight() * this.moltiplicator.getSecond();
        double aspectRatio = Math.min(1.2, width / height);
        int scalingFactor = (int) (Math.sqrt(width * height) * aspectRatio * TEXT_PROPORTION);
    
        return Math.max(1, scalingFactor);
    }

}
