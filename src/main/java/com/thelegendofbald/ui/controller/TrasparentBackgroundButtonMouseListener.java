package com.thelegendofbald.ui.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.mainmenu.api.InteractivePanel;
import com.thelegendofbald.ui.model.TrasparentBackgroundButton;
import com.thelegendofbald.utils.ColorUtils;

public class TrasparentBackgroundButtonMouseListener extends TemplateButtonMouseListener {

    private static final double FACTOR_OF_DARKNESS = 0.6;

    private final Color buttonFGColor;
    private final Color buttonFGHoverColor;
    

    public TrasparentBackgroundButtonMouseListener(TrasparentBackgroundButton button) {
        this.buttonFGColor = button.getForeground();
        this.buttonFGHoverColor = ColorUtils.getDarkenColor(buttonFGColor, FACTOR_OF_DARKNESS);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        var button = (TrasparentBackgroundButton) e.getSource();

        if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
            button.setForeground(buttonFGHoverColor);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        var button = (TrasparentBackgroundButton) e.getSource();

        if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
            button.setForeground(buttonFGColor);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        var button = (TrasparentBackgroundButton) e.getSource();

        if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
            var parent = (InteractivePanel) SwingUtilities.getAncestorOfClass(InteractivePanel.class, button);
            parent.unselectAllButtons();
            button.select();
        }
    }

}
