package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.model.RoundedButton;
import com.thelegendofbald.ui.settingsmenu.controller.KeybindingButtonMouseListener;

public class KeybindingButton extends RoundedButton {

    private volatile boolean changing = false;

    public KeybindingButton(String text, Dimension windowSize, Pair<Double, Double> moltiplicator, double arcProportion, Color bgColor, String fontName, Color fontColor, int fontType) {
        super(text, windowSize, moltiplicator, arcProportion, bgColor, fontName, fontColor, fontType);
        this.initialize();
    }

    public KeybindingButton(ImageIcon icon, Dimension windowSize, Pair<Double, Double> moltiplicator, double arcProportion, Color bgColor, Color fgColor) {
        super(icon, windowSize, moltiplicator, arcProportion, bgColor, fgColor);
        this.initialize();
    }

    private void initialize() {
        this.addMouseListener(new KeybindingButtonMouseListener(this));
        this.addKeyListener(new KeybindingButtonKeyListener());
    }

    public void setChanging(boolean changing) {
        this.changing = changing;
    }

    public boolean isChanging() {
        return this.changing;
    }

}
