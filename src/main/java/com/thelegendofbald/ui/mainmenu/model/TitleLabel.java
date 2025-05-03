package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TitleLabel extends JLabel {

    private final int fontProportion;
    private final Dimension proportion;

    public TitleLabel(final String text, final Dimension size, final Color color, final String fontName, final Dimension proportion) {
        final int width = (int) (size.getWidth() / proportion.getWidth());
        final int height = (int) (size.getHeight() / proportion.getHeight());
        final Dimension prefSize = new Dimension(width, height);

        this.proportion = proportion;
        this.fontProportion = (int) Math.pow(proportion.getWidth(), proportion.getHeight());

        this.setText(text);
        this.setForeground(color);
        this.setOpaque(false);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.setFont(new Font(fontName, Font.BOLD, (int) prefSize.getWidth() / this.fontProportion));
    }

    @Override
    public void setPreferredSize(Dimension size) {
        final int width = (int) (size.getWidth() / proportion.getWidth());
        final int height = (int) (size.getHeight() / proportion.getHeight());
        final Dimension prefSize = new Dimension(width, height);
        super.setPreferredSize(prefSize);
        this.setFont(this.getFont().deriveFont((float) (prefSize.getWidth() / this.fontProportion)));
    }

}
