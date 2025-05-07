package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.commons.math3.util.Pair;

public final class TitleLabel extends JLabel {

    //private static final double HEIGHT_PROPORTION = 0.3;
    private static final double TEXT_PROPORTION = 0.08;

    private final Pair<Double, Double> moltiplicator;

    public TitleLabel(final String text, final Dimension parentSize, final Pair<Double, Double> moltiplicator, final Color color, final String fontName) {
        this.moltiplicator = moltiplicator;
        this.setText(text);
        this.setForeground(color);
        this.setOpaque(false);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.setFont(new Font(fontName, Font.BOLD, this.getFontSize(parentSize)));
    }

    private Dimension calculatePreferredSize(Dimension parentSize) {
        return new Dimension((int) (parentSize.getWidth() * this.moltiplicator.getFirst()), (int) (parentSize.getHeight() * this.moltiplicator.getSecond()));
    }

    private int getFontSize(Dimension parentSize) {
        double width = parentSize.getWidth();
        double height = parentSize.getHeight();
        double aspectRatio = Math.min(1.2, width / height);
        int scalingFactor = (int) (Math.sqrt(width * height) * aspectRatio * TEXT_PROPORTION);
    
        return Math.max(1, scalingFactor);
    }
    

    @Override
    public void setPreferredSize(Dimension size) {
        var preferredSize = this.calculatePreferredSize(size);
        super.setPreferredSize(preferredSize);
        this.setFont(this.getFont().deriveFont((float) this.getFontSize(size)));
    }

}
