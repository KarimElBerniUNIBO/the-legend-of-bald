package com.thelegendofbald.ui.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.mainmenu.api.InteractivePanel;
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
            if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
                button.setForeground(buttonFGHoverColor);
            }
        });
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        SwingUtilities.invokeLater(() -> {
            var button = (TrasparentBackgroundButton) e.getSource();
            if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
                button.setForeground(buttonFGColor);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        SwingUtilities.invokeLater(() -> {
            var button = (TrasparentBackgroundButton) e.getSource();
            if (!button.isSelected() && Optional.ofNullable(button.getIcon()).isEmpty()) {
                var parent = (InteractivePanel) SwingUtilities.getAncestorOfClass(InteractivePanel.class, button);
                parent.unselectAllButtons();
                button.select();
            }
        });
    }

}
