package com.thelegendofbald.ui.mainmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

class TitleLabel extends JLabel {

    private static final int WIDTH_PROPORTION = 2;
    private static final int HEIGHT_PROPORTION = 3;
    private static final int PROPORTION = (int) Math.pow(WIDTH_PROPORTION, HEIGHT_PROPORTION);

    TitleLabel(final String text, final Dimension size, final Color color, final String fontName) {
        final int width = (int) size.getWidth() / WIDTH_PROPORTION;
        final int height = (int) size.getHeight() / HEIGHT_PROPORTION;

        this.setPreferredSize(new Dimension(width, height));
        this.setText(text);
        this.setForeground(color);
        this.setOpaque(false);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFont(new Font(fontName, Font.BOLD, (int) this.getPreferredSize().getWidth() / PROPORTION));
    }

}
