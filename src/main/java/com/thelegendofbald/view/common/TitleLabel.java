package com.thelegendofbald.view.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.commons.math3.util.Pair;

/**
 * A custom JLabel for displaying a title in the main menu UI, with dynamic font sizing and preferred size
 * based on the parent container's dimensions and configurable scaling factors.
 * <p>
 * The TitleLabel automatically adjusts its font size and preferred size according to the parent size,
 * maintaining a maximum aspect ratio and a proportion of the text relative to the parent.
 * </p>
 *
 * <ul>
 *   <li><b>MAX_ASPECTRATIO</b>: Limits the aspect ratio used for font scaling.</li>
 *   <li><b>TEXT_PROPORTION</b>: Proportion of the parent area used for text scaling.</li>
 *   <li><b>moltiplicator</b>: Pair of scaling factors for width and height relative to the parent.</li>
 * </ul>
 */
public final class TitleLabel extends JLabel {

    private static final double MAX_ASPECTRATIO = 1.2;
    private static final double TEXT_PROPORTION = 0.08;

    private final Pair<Double, Double> moltiplicator;

    /**
     * Constructs a TitleLabel with the specified text, parent size, scaling factors, color, and font name.
     *
     * @param text          the text to be displayed by the label
     * @param parentSize    the size of the parent component, used to determine the font size
     * @param moltiplicator a pair of scaling factors for width and height
     * @param color         the color of the label's text
     * @param fontName      the name of the font to use for the label's text
     */
    public TitleLabel(final String text, final Dimension parentSize, final Pair<Double, Double> moltiplicator,
            final Color color, final String fontName) {
        this.moltiplicator = moltiplicator;
        this.setText(text);
        this.setForeground(color);
        this.setOpaque(false);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.setFont(new Font(fontName, Font.BOLD, this.getFontSize(parentSize)));
    }

    private Dimension calculatePreferredSize(final Dimension parentSize) {
        return new Dimension((int) (parentSize.getWidth() * this.moltiplicator.getFirst()),
                (int) (parentSize.getHeight() * this.moltiplicator.getSecond()));
    }

    private int getFontSize(final Dimension parentSize) {
        final double width = parentSize.getWidth();
        final double height = parentSize.getHeight();
        final double aspectRatio = Math.min(MAX_ASPECTRATIO, width / height);
        final int scalingFactor = (int) (Math.sqrt(width * height) * aspectRatio * TEXT_PROPORTION);

        return Math.max(1, scalingFactor);
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        final var preferredSize = this.calculatePreferredSize(size);
        super.setPreferredSize(preferredSize);
        this.setFont(this.getFont().deriveFont((float) this.getFontSize(size)));
    }

}
