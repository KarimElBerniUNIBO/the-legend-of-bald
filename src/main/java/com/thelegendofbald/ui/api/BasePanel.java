package com.thelegendofbald.ui.api;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public abstract class BasePanel extends JPanel implements MenuView {

    private static final double PROPORTION = 0.85;

    protected Dimension internalSize;
    protected boolean initialized = false;

    public BasePanel(Dimension size) {
        super();
        this.internalSize = size;
        this.setOpaque(false);
        this.initializeSize();
    }

    private void initializeSize() {
        this.setPreferredSize(this.internalSize);
        this.setMinimumSize(new Dimension((int) (this.internalSize.getWidth()), (int) (this.internalSize.getHeight() * PROPORTION)));
        this.setMaximumSize(this.internalSize);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!this.initialized) {
            this.initialized = true;
            SwingUtilities.invokeLater(this::initializeComponents);
        }
    }

    protected abstract void initializeComponents();

    @Override
    public void updateView() {
        this.removeAll();
        this.addComponentsToPanel();
        this.revalidate();
        this.repaint();
    }

    @Override
    public abstract void addComponentsToPanel();

}
