package com.thelegendofbald.ui.api;

import java.awt.Dimension;

import javax.swing.JButton;

/**
 * Factory for creating template of buttons for User Interface.
 */
public interface JButtonFactory {

    /**
     * Give a rectangular template button with the specified text and parent size.
     * 
     * @param text the text label of the button
     * @param parentSize the size of the parent component, used to determine the button size
     * @return a JButton with a predefined rectangular shape
     */
    JButton createRectangleButton(String text, Dimension parentSize);

    /**
     * Give a rounded button with the specified text and parent size.
     * 
     * @param text the text label of the button
     * @param parentSize the size of the parent component, used to determine the button size
     * @param arcProportion the proportion [0-1] of how much smooth are the corners
     * @return a JButton with a predefined rectangular shape
     */
    JButton createRoundedButton(String text, Dimension parentSize, double arcProportion);

}
