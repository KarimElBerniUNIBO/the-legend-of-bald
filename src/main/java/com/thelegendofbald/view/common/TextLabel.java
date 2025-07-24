package com.thelegendofbald.view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import org.apache.commons.lang3.tuple.Pair;

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
public final class TextLabel extends JLabel {

    private static final long serialVersionUID = 8944443862403400839L;

    private static final double MAX_ASPECTRATIO = 1.2;
    private static final double TEXT_PROPORTION = 0.08;

    private final Pair<Double, Double> proportion;
    private final Pair<Double, Double> moltiplicator;

    /**
     * Constructs a TitleLabel with the specified text, parent size, scaling factors, color, and font name.
     *
     * @param text          the text to be displayed by the label
     * @param parentSize    the size of the parent component, used to determine the font size
     * @param proportion    a pair of scaling factors for width and height relative to the parent size
     * @param moltiplicator a pair of scaling factors for width and height for the font size calculation
     * @param color         the color of the label's text
     * @param fontName      the name of the font to use for the label's text
     */
    public TextLabel(final String text, final Dimension parentSize, final Pair<Double, Double> proportion,
            final Pair<Double, Double> moltiplicator, final Color color, final String fontName) {
        this.proportion = proportion;
        this.moltiplicator = moltiplicator;
        this.setText(text);
        this.setForeground(color);
        this.setOpaque(false);
        this.setHorizontalAlignment(CENTER);
        this.setAlignmentX(CENTER_ALIGNMENT);
        this.setAlignmentY(CENTER_ALIGNMENT);
        this.setFont(new Font(fontName, Font.BOLD, this.getFontSize(parentSize)));
    }

    private Dimension calculatePreferredSize(final Dimension parentSize) {
        return new Dimension((int) (parentSize.getWidth() * this.proportion.getLeft()),
                (int) (parentSize.getHeight() * this.proportion.getRight()));
    }

    private int getFontSize(final Dimension parentSize) {
        final double width = parentSize.getWidth() * this.moltiplicator.getLeft();
        final double height = parentSize.getHeight() * this.moltiplicator.getRight();
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
