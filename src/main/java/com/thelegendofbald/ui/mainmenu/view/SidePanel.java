package com.thelegendofbald.ui.mainmenu.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.apache.commons.math3.util.Pair;


public final class SidePanel extends JPanel {

    //private static final double WIDTH_PROPORTION = 3.5;
    private final Pair<Double, Double> proportion;

    public SidePanel(final Dimension size, final Pair<Double, Double> proportion) {
        this.proportion = proportion;
        //this.setPreferredSize(new Dimension((int) (size.getWidth() / WIDTH_PROPORTION), (int) size.getHeight()));
        this.setPreferredSize(new Dimension((int) (size.getWidth() / proportion.getFirst()), (int) (size.getHeight() / proportion.getSecond())));
        this.setOpaque(false);
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(new Dimension((int) (size.getWidth() / proportion.getFirst()), (int) (size.getHeight() / proportion.getSecond())));
    }

}
