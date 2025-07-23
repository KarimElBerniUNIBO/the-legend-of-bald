package com.thelegendofbald.view.inventory;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

class InventoryContent extends AdapterPanel {
    
    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<SlotPanel> inventorySlots;
    private final int maxSlotsPerRow;
    private final int maxRows;
    private final int maxSlots;
    private final Inventory inventoryManager;

    InventoryContent(Dimension size, int columns, int rows, Inventory inventoryManager) {
        super(size);
        this.maxSlotsPerRow = columns;
        this.maxRows = rows;
        this.inventoryManager = inventoryManager;
        this.maxSlots = this.maxSlotsPerRow * this.maxRows;
        this.inventorySlots = this.getInventorySlots();

        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
    }

    private List<SlotPanel> getInventorySlots() {
        return inventoryManager.getSlots().stream()
                .map(slot -> new SlotPanel(slot, inventoryManager))
                .toList();
    }

    @Override
    public void updateComponentsSize() {
    }

    @Override
    public void addComponentsToPanel() {
        inventorySlots.forEach(slot -> {
            this.gbc.gridx = inventorySlots.indexOf(slot) % maxSlotsPerRow;
            this.add(slot, this.gbc);
        });
    }

}
