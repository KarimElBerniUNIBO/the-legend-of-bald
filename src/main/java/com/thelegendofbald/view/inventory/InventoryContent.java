package com.thelegendofbald.view.inventory;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.stream.Stream;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

class InventoryContent extends AdapterPanel {
    
    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<Slot> inventorySlots;
    private final int maxSlotsPerRow;
    private final int maxRows;
    private final int maxSlots;

    InventoryContent(Dimension size, int maxSlotsPerRow, int maxRows) {
        super(size);
        this.maxSlotsPerRow = maxSlotsPerRow;
        this.maxRows = maxRows;
        this.maxSlots = this.maxSlotsPerRow * this.maxRows;
        this.inventorySlots = this.getInventorySlots();

        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
    }

    private List<Slot> getInventorySlots() {
        return Stream.generate(Slot::new)
                .limit(maxSlots)
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
