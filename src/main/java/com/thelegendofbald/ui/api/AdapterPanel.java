package com.thelegendofbald.ui.api;

import java.awt.Dimension;

import com.thelegendofbald.ui.controller.ResizeListener;

public abstract class AdapterPanel extends BasePanel implements Resizable {

    protected static final double PROPORTION = 0.85;

    public AdapterPanel(Dimension size) {
        super(size);
        this.internalSize = size;
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    @Override
    protected abstract void initializeComponents();

    @Override
    public void onResize() {
        if (this.initialized && this.getWidth() > 0 && this.getHeight() > 0) {
            this.updateView();
        }
    }

    @Override
    public abstract void addComponentsToPanel();

}
