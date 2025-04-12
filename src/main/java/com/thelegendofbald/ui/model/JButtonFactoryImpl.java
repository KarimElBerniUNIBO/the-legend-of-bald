package com.thelegendofbald.ui.model;

import java.awt.Dimension;

import javax.swing.JButton;

import com.thelegendofbald.ui.api.JButtonFactory;

/**
 * Implementation of JButtonFactory.
 */
public final class JButtonFactoryImpl implements JButtonFactory {

    /**
     * Creates a default template rectangle button with the specified text.
     *
     * @param text The text to display on the button.
     * @param parentSize The size of the parent container.
     * @return An instance of SquareButton.
     */
    @Override
    public JButton createRectangleButton(final String text, final Dimension parentSize) {
        return new RectangleButton(text, parentSize);
    }

    /**
     * Creates a rounded button with the specified text.
     *
     * @param text The text to display on the button.
     * @param parentSize The size of the parent container.
     * @param arcProportion the proportion [0-1] of how much smooth are the corners
     * @return An instance of SquareButton.
     */
    @Override
    public JButton createRoundedButton(String text, Dimension parentSize, double arcProportion) {
        return new RoundedButton(text, parentSize, arcProportion);
    }
}
