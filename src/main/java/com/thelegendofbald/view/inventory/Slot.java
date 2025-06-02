package com.thelegendofbald.view.inventory;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.controller.listeners.common.SlotMouseListener;

public final class Slot extends AdapterPanel {

    private static final Color DEFAULT_BG_COLOR = new Color(60, 60, 60, 180);

    public Slot() {
        super(new Dimension(0, 0));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        SwingUtilities.invokeLater(() -> this.setBackground(DEFAULT_BG_COLOR));
        this.addMouseListener(new SlotMouseListener(this));
    }

    @Override
    public void updateComponentsSize() {
    }

    @Override
    public void addComponentsToPanel() {
    }

}
