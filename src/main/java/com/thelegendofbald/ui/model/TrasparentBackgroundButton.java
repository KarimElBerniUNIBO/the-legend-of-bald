package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.TemplateButton;
import com.thelegendofbald.ui.controller.TrasparentBackgroundButtonMouseListener;

/**
 * A custom button with a transparent background and selectable foreground
 * color.
 * <p>
 * The {@code TrasparentBackgroundButton} extends {@link TemplateButton} and
 * provides
 * a button that does not fill its content area or paint its border, making it
 * appear
 * transparent. It supports selection state, changing its foreground color when
 * selected.
 * </p>
 *
 * <ul>
 * <li>Foreground color changes to yellow when selected.</li>
 * <li>Supports both text and icon-based constructors.</li>
 * <li>Mouse listener is added for custom interaction handling.</li>
 * </ul>
 */
public class TrasparentBackgroundButton extends TemplateButton {

    private final Color buttonFGColor;
    private final Color buttonFGSelectedColor;

    private boolean selected;

    /**
     * Constructs a button with text and custom properties.
     *
     * @param text         the text to display on the button
     * @param windowSize   the size of the window
     * @param moltiplicator the scaling factor for the button
     * @param bgColor      the background color of the button
     * @param fontName     the name of the font
     * @param fontColor    the color of the font
     * @param fontType     the type of the font (e.g., bold, italic)
     */
    public TrasparentBackgroundButton(final String text, final Dimension windowSize, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final String fontName,
            final Color fontColor, final int fontType) {
        super(text, windowSize, moltiplicator, bgColor, fontName, fontColor, fontType);
        this.buttonFGColor = fontColor;
        this.buttonFGSelectedColor = Color.YELLOW;

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.addMouseListener(new TrasparentBackgroundButtonMouseListener(this));
    }

    /**
     * Constructs a button with an icon and custom properties.
     *
     * @param icon         the icon to display on the button
     * @param windowSize   the size of the window
     * @param moltiplicator the scaling factor for the button
     * @param bgColor      the background color of the button
     * @param fgColor      the foreground color of the button
     */
    public TrasparentBackgroundButton(final ImageIcon icon, final Dimension windowSize, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final Color fgColor) {
        super(icon, windowSize, moltiplicator, bgColor, fgColor);
        this.buttonFGColor = fgColor;
        this.buttonFGSelectedColor = Color.YELLOW;

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.addMouseListener(new TrasparentBackgroundButtonMouseListener(this));
    }

    /**
     * Selects the button, changing its foreground color to the selected color.
     */
    public void select() {
        this.selected = true;
        this.setForeground(buttonFGSelectedColor);
        this.repaint();
    }

    /**
     * Unselects the button, restoring its original foreground color.
     */
    public void unselect() {
        this.selected = false;
        this.setForeground(buttonFGColor);
        this.repaint();
    }

    /**
     * Checks if the button is selected.
     *
     * @return {@code true} if the button is selected, {@code false} otherwise
     */
    @Override
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Sets the preferred size of the button.
     *
     * @param size the preferred size of the button
     */
    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
    }

}
