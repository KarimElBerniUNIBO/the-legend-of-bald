package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Dimension;

import com.thelegendofbald.ui.controller.TrasparentBackgroundButtonMouseListener;

public class TrasparentBackgroundButton extends TemplateButton {

    public TrasparentBackgroundButton(String text, Dimension windowSize, Color bgColor, String fontName,
            Color fontColor, int fontType) {
        super(text, windowSize, bgColor, fontName, fontColor, fontType);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.addMouseListener(new TrasparentBackgroundButtonMouseListener(this));
    }

}
