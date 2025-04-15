package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

final class TitleLabel extends JLabel {

    private final int fontProportion;

    TitleLabel(final String text, final Dimension size, final Color color, final String fontName, final Dimension proportion) {
        this.fontProportion = (int) Math.pow(proportion.getWidth(), proportion.getHeight());
        final int width = (int) (size.getWidth() / proportion.getWidth());
        final int height = (int) (size.getHeight() / proportion.getHeight());

        this.setPreferredSize(new Dimension(width, height));
        this.setText(text);
        this.setForeground(color);
        this.setOpaque(false);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.setFont(new Font(fontName, Font.BOLD, (int) this.getPreferredSize().getWidth() / this.fontProportion));
    }

}
