package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.TemplateButton;


/**
 * A button with a rectangular shape, extending {@link TemplateButton}.
 * Provides constructors to create a button with either text or an icon,
 * allowing customization of size, colors, and font properties.
 *
 * <p>
 * Usage examples:
 * <ul>
 *   <li>Create a text button with custom font and colors.</li>
 *   <li>Create an icon button with background and foreground colors.</li>
 * </ul>
 * </p>
 *
 * @see TemplateButton
 */
public class RectangleButton extends TemplateButton {

    /**
     * Constructs a new {@code RectangleButton} with the specified properties.
     *
     * @param text         the text to be displayed on the button
     * @param windowSize   the size of the window in which the button will be displayed
     * @param moltiplicator a pair of doubles used to scale or position the button relative to the window size
     * @param bgColor      the background color of the button
     * @param fontName     the name of the font to be used for the button text
     * @param fontColor    the color of the button text
     * @param fontType     the style of the font (e.g., {@link java.awt.Font#PLAIN}, {@link java.awt.Font#BOLD})
     */
    public RectangleButton(final String text, final Dimension windowSize, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        super(text, windowSize, moltiplicator, bgColor, fontName, fontColor, fontType);
    }

    /**
     * Constructs a new RectangleButton with the specified icon, window size, scaling factors,
     * background color, and foreground color.
     *
     * @param icon         the ImageIcon to be displayed on the button
     * @param windowSize   the Dimension representing the size of the window
     * @param moltiplicator a Pair of Doubles used as scaling factors for the button's size or position
     * @param bgColor      the background Color of the button
     * @param fgColor      the foreground Color (typically the text or icon color) of the button
     */
    public RectangleButton(final ImageIcon icon, final Dimension windowSize, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final Color fgColor) {
        super(icon, windowSize, moltiplicator, bgColor, fgColor);
    }

}
