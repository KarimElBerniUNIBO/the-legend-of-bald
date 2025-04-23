package com.thelegendofbald.ui.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.model.TrasparentBackgroundButton;

public class TrasparentBackgroundButtonMouseListener extends TemplateButtonMouseListener {

    private static final double FACTOR_OF_DARKNESS = 0.6;

    private final Color buttonFGColor;
    private final Color buttonFGHoverColor;

    public TrasparentBackgroundButtonMouseListener(TrasparentBackgroundButton button) {
        this.buttonFGColor = button.getForeground();
        this.buttonFGHoverColor = this.getDarkenColor(buttonFGColor);
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
            var button = (TrasparentBackgroundButton) e.getSource();
            button.setForeground(buttonFGHoverColor);
        });
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        SwingUtilities.invokeLater(() -> {
            var button = (TrasparentBackgroundButton) e.getSource();
            button.setForeground(buttonFGColor);
        });
    }

}
