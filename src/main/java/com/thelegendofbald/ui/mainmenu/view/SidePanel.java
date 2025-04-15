package com.thelegendofbald.ui.mainmenu.view;

import java.awt.Dimension;

import javax.swing.JPanel;

final class SidePanel extends JPanel {

    private static final double WIDTH_PROPORTION = 3.5;

    SidePanel(final Dimension size) {
        this.setPreferredSize(new Dimension((int) (size.getWidth() / WIDTH_PROPORTION), (int) size.getHeight()));
        this.setOpaque(false);
    }

}
