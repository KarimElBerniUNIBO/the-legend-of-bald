package com.thelegendofbald.ui.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.model.RoundedButton;

public class RoundedButtonMouseListener extends TemplateButtonMouseListener {

    private static final double FACTOR_OF_DARKNESS = 0.8;

    private final Color buttonBGColor;
    private final Color buttonBGHoverColor;

    private final Color buttonFGColor;
    private final Color buttonFGHoverColor;

    public RoundedButtonMouseListener(RoundedButton button) {
        this.buttonBGColor = button.getBackground();
        this.buttonBGHoverColor = this.getDarkenColor(this.buttonBGColor);

        this.buttonFGColor = button.getForeground();
        this.buttonFGHoverColor = this.getDarkenColor(this.buttonFGColor);
    }

    private Color getDarkenColor(Color color) {
        int red = (int) (color.getRed() * FACTOR_OF_DARKNESS);
        int green = (int) (color.getGreen() * FACTOR_OF_DARKNESS);
        int blue = (int) (color.getBlue() * FACTOR_OF_DARKNESS);

        return new Color(red, green, blue);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        SwingUtilities.invokeLater(() -> {
            RoundedButton button = (RoundedButton) e.getSource();

            button.setBackground(this.buttonBGHoverColor);
            button.setForeground(this.buttonFGHoverColor);
        });
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        SwingUtilities.invokeLater(() -> {
            RoundedButton button = (RoundedButton) e.getSource();

            button.setBackground(this.buttonBGColor);
            button.setForeground(this.buttonFGColor);
        });
    }

}
