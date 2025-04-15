package com.thelegendofbald.ui.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.ui.controller.RoundedButtonMouseListener;

public class RoundedButton extends TemplateButton {

    private final double arcProportion;
    private final Dimension windowSize;

    public RoundedButton(final String text, final Dimension windowSize, final double arcProportion, final Color bgColor, final String fontName,
            final Color fontColor, final int fontType) {
        super(text, windowSize, bgColor, fontName, fontColor, fontType);
        this.arcProportion = arcProportion;
        this.windowSize = windowSize;

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
        Pair<Integer, Integer> preferredThicknessInWidth = Pair.of(preferredThickness, preferredWidth);

        int arcValue = this.getArcValue();
        int thickness = (int) (preferredThicknessInWidth.getLeft()
                * (this.windowSize.getWidth() / preferredThicknessInWidth.getRight()));

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getForeground());
        g2.setStroke(new BasicStroke((thickness)));
        g2.drawRoundRect(0, 0, this.getWidth() - thickness / 2, this.getHeight() - thickness / 2, arcValue, arcValue);

        g2.dispose();
    }

}
