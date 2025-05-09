package com.thelegendofbald.ui.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.TemplateButton;
import com.thelegendofbald.ui.controller.RoundedButtonMouseListener;

public class RoundedButton extends TemplateButton {

    private final double arcProportion;
    private final Dimension windowSize;

    public RoundedButton(final String text, final Dimension parentSize, final Pair<Double, Double> moltiplicator, final double arcProportion, final Color bgColor, final String fontName,
            final Color fontColor, final int fontType) {
        super(text, parentSize, moltiplicator, bgColor, fontName, fontColor, fontType);
        this.arcProportion = arcProportion;
        this.windowSize = parentSize;
        this.initialize();
    }

    public RoundedButton(ImageIcon icon, Dimension parentSize, Pair<Double, Double> moltiplicator, double arcProportion, Color bgColor, Color fgColor) {
        super(icon, parentSize, moltiplicator, bgColor, fgColor);
        this.arcProportion = arcProportion;
        this.windowSize = parentSize;
        this.initialize();
    }

    private void initialize() {
        this.setContentAreaFilled(false);
        this.addMouseListener(new RoundedButtonMouseListener(this));
    }

    private int getArcValue() {
        return (int) (this.getWidth() * this.arcProportion);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int arcValue = this.getArcValue();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getBackground());
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), arcValue, arcValue);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        int preferredThickness = 4;
        int preferredWidth = 1280;
        Pair<Integer, Integer> preferredThicknessInWidth = Pair.create(preferredThickness, preferredWidth);

        int arcValue = this.getArcValue();
        int thickness = (int) (preferredThicknessInWidth.getFirst()
                * (this.windowSize.getWidth() / preferredThicknessInWidth.getSecond()));

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getForeground());
        g2.setStroke(new BasicStroke((thickness)));
        g2.drawRoundRect(0, 0, this.getWidth() - thickness / 2, this.getHeight() - thickness / 2, arcValue, arcValue);

        g2.dispose();
    }

}
