package com.thelegendofbald.view.inventory;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.TextLabelFactory;
import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.controller.listeners.inventory.SlotMouseListener;
import com.thelegendofbald.model.inventory.Slot;
import com.thelegendofbald.view.common.TextLabel;
import com.thelegendofbald.view.common.TextLabelFactoryImpl;

public final class SlotPanel extends AdapterPanel {

    private static final Color DEFAULT_BG_COLOR = new Color(60, 60, 60, 180);

    private final TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private Optional<TextLabel> itemLabel = Optional.empty();

    private final Slot slot;

    public SlotPanel(Slot slot, Inventory inventoryManager) {
        super(new Dimension(0, 0));
        this.slot = slot;

        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        SwingUtilities.invokeLater(() -> this.setBackground(DEFAULT_BG_COLOR));
        this.addMouseListener(new SlotMouseListener(this, inventoryManager));
    }

    @Override
    protected void initializeComponents() {
        slot.getItem().ifPresent(item -> {
            this.itemLabel = Optional.of(tlFactory.createTextLabelWithProportion(item.getName(), this.getSize(), Optional.of(Pair.of(0.5, 0.5)), Optional.of(Pair.of(2.0, 2.0)), Optional.empty(), Optional.empty()));
        });
        super.initializeComponents();
    }

    public Slot getSlot() {
        return slot;
    }

    @Override
    public void updateComponentsSize() {
        this.itemLabel.ifPresent(label -> label.setPreferredSize(this.getSize()));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        this.itemLabel.ifPresent(this::add);
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

}
