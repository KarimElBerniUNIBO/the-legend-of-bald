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

    private static final int MAX_SLOTS_PER_ROW = 5;
    private static final int MAX_ROWS = 3;
    private static final int MAX_SLOTS = MAX_SLOTS_PER_ROW * MAX_ROWS;
    
    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<Slot> inventorySlots = this.getInventorySlots();

    InventoryContent(Dimension size) {
        super(size);
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
    }

    private List<Slot> getInventorySlots() {
        return Stream.iterate(0, i -> i + 1).limit(MAX_SLOTS)
                .map(i -> new Slot())
                .toList();
    }

    @Override
    public void updateComponentsSize() {
    }

    @Override
    public void addComponentsToPanel() {
        inventorySlots.forEach(slot -> {
            this.gbc.gridx = inventorySlots.indexOf(slot) % MAX_SLOTS_PER_ROW;
            this.add(slot, this.gbc);
        });
    }

}
