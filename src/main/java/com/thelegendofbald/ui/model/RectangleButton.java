package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;


/**
 * Class created just for testing with a default template button.
 */
public class RectangleButton extends TemplateButton {

    /**
     * Constructor of the default template button.
     * 
     * @param text the text label of the button
     * @param windowSize the size of the window game
     */
    public RectangleButton(final String text, final Dimension windowSize, final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        super(text, windowSize, bgColor, fontName, fontColor, fontType);
    }

    public RectangleButton(final ImageIcon icon, final Dimension windowSize, final Color bgColor, final Color fgColor) {
        super(icon, windowSize, bgColor, fgColor);
    }

}
