package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

/**
 * An abstract button template that provides default styling and behavior.
 * <p>
 * This class serves as a base for customizable buttons, applying consistent 
 * font, size, and alignment properties.
 * </p>
 */
public abstract class TemplateButton extends JButton {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    private static final String DEFAULT_FONT_NAME = Font.SANS_SERIF;
    private static final int DEFAULT_FONT_TYPE = Font.BOLD;
    private static final int PROPORTION = 25;

    /**
     * Constructs a button with predefined styling and dimensions.
     *
     * @param text The text to be displayed on the button.
     * @param windowSize The size of the parent window, used to determine button font size.
     */
    public TemplateButton(final String text, final Dimension windowSize) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setForeground(DEFAULT_FOREGROUND_COLOR);
        this.setFont(new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_TYPE, (int) windowSize.getWidth() / PROPORTION));
        this.setFocusable(false);
    }

}
