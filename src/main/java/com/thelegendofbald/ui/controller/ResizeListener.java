package com.thelegendofbald.ui.controller;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ResizeListener extends ComponentAdapter {

    private final Runnable runnable;

    public ResizeListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        runnable.run();
    }

}
