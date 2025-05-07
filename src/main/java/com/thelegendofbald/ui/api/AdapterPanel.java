package com.thelegendofbald.ui.api;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.thelegendofbald.ui.controller.ResizeListener;

public abstract class AdapterPanel extends JPanel {

    protected static final double PROPORTION = 0.85;

    protected Dimension internalSize;
    protected boolean initialized = false;

    public AdapterPanel(Dimension size) {
        this.internalSize = size;
        this.setPreferredSize(size);
        this.setMinimumSize(new Dimension((int) (size.getWidth()), (int) (size.getHeight() * PROPORTION)));
        this.setMaximumSize(size);
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    public void onResize() {
        if (!initialized && this.getWidth() > 0 && this.getHeight() > 0) {
            initialized = true;
            this.addComponentsToPanel();
        } else if (initialized) {
            this.removeAll();
            this.addComponentsToPanel();
        }
    }

    public void addComponentsToPanel() {
        // Override this method to add components to the panel

        this.revalidate();
        this.repaint();
    }

}
