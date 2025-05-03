package com.thelegendofbald.ui.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.thelegendofbald.ui.model.RoundedButton;
import com.thelegendofbald.utils.ColorUtils;

public class RoundedButtonMouseListener extends TemplateButtonMouseListener {

    private static final double FACTOR_OF_DARKNESS = 0.6;

    private final Color buttonBGColor;
    private final Color buttonBGHoverColor;

    private final Color buttonFGColor;
    private final Color buttonFGHoverColor;

    public RoundedButtonMouseListener(RoundedButton button) {
        this.buttonBGColor = button.getBackground();
        this.buttonBGHoverColor = ColorUtils.getDarkenColor(this.buttonBGColor, FACTOR_OF_DARKNESS);

        this.buttonFGColor = button.getForeground();
        this.buttonFGHoverColor = ColorUtils.getDarkenColor(this.buttonFGColor, FACTOR_OF_DARKNESS);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        var button = (RoundedButton) e.getSource();

        button.setBackground(this.buttonBGHoverColor);
        button.setForeground(this.buttonFGHoverColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        var button = (RoundedButton) e.getSource();

        button.setBackground(this.buttonBGColor);
        button.setForeground(this.buttonFGColor);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        this.mouseExited(e);
    }

}
