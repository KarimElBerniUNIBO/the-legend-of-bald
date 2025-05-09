package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.TemplateButton;
import com.thelegendofbald.ui.controller.TrasparentBackgroundButtonMouseListener;

public class TrasparentBackgroundButton extends TemplateButton {

    private final Color buttonFGColor;
    private final Color buttonFGSelectedColor;

    private boolean selected = false;

    public TrasparentBackgroundButton(String text, Dimension windowSize, Pair<Double, Double> moltiplicator, Color bgColor, String fontName,
            Color fontColor, int fontType) {
        super(text, windowSize, moltiplicator, bgColor, fontName, fontColor, fontType);
        this.buttonFGColor = fontColor;
        this.buttonFGSelectedColor = Color.YELLOW;

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.addMouseListener(new TrasparentBackgroundButtonMouseListener(this));
    }

    public TrasparentBackgroundButton(ImageIcon icon, Dimension windowSize, Pair<Double, Double> moltiplicator, Color bgColor, Color fgColor) {
        super(icon, windowSize, moltiplicator, bgColor, fgColor);
        this.buttonFGColor = fgColor;
        this.buttonFGSelectedColor = Color.YELLOW;

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.addMouseListener(new TrasparentBackgroundButtonMouseListener(this));
    }

    public void select() {
        this.selected = true;
        this.setForeground(buttonFGSelectedColor);
        this.repaint();
    }

    public void unselect() {
        this.selected = false;
        this.setForeground(buttonFGColor);
        this.repaint();
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
    }

}
