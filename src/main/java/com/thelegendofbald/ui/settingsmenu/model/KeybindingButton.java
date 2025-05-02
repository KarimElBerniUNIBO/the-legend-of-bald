package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;

import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.model.RoundedButton;
import com.thelegendofbald.ui.settingsmenu.controller.KeybindingButtonMouseListener;

public class KeybindingButton extends RoundedButton {

    private volatile boolean changing = false;

    public KeybindingButton(String text, Dimension windowSize, double arcProportion, Color bgColor, String fontName, Color fontColor, int fontType) {
        super(text, windowSize, arcProportion, bgColor, fontName, fontColor, fontType);
        this.initialize();
    }

    public KeybindingButton(ImageIcon icon, Dimension windowSize, double arcProportion, Color bgColor, Color fgColor) {
        super(icon, windowSize, arcProportion, bgColor, fgColor);
        this.initialize();
    }

    private void initialize() {
        this.addMouseListener(new KeybindingButtonMouseListener(this));
        this.addKeyListener(new KeybindingButtonKeyListener());
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    private void onResize() {
        super.setPreferredSize(this.getSize());
    }

    public void setChanging(boolean changing) {
        this.changing = changing;
    }

    public boolean isChanging() {
        return this.changing;
    }

}
