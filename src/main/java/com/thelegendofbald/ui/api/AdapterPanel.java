package com.thelegendofbald.ui.api;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.controller.ResizeListener;

public abstract class AdapterPanel extends JPanel {

    protected static final double PROPORTION = 0.85;

    protected Dimension internalSize;
    protected boolean initialized = false;

    public AdapterPanel(Dimension size) {
        this.internalSize = size;
        SwingUtilities.invokeLater(this::initializeSize);
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    private void initializeSize() {
        this.setPreferredSize(this.internalSize);
        this.setMinimumSize(new Dimension((int) (this.internalSize.getWidth()), (int) (this.internalSize.getHeight() * PROPORTION)));
        this.setMaximumSize(this.internalSize);
    }

    private void onResize() {
        if (!initialized && this.getWidth() > 0 && this.getHeight() > 0) {
            initialized = true;
            this.update();
        } else if (initialized) {
            this.removeAll();
            this.update();
        }
    }

    protected abstract void addComponentsToPanel();

    private void update() {
        this.addComponentsToPanel();
        this.revalidate();
        this.repaint();
    }

}
