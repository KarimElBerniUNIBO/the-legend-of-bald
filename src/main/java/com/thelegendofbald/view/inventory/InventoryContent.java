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

    private final List<Slot> slots;
    private final int columns;
    private final int rows;
    private final int totalSlots;

    InventoryContent(Dimension size, int columns, int rows) {
        super(size);
        this.columns = columns;
        this.rows = rows;
        this.totalSlots = this.columns * this.rows;
        this.slots = this.getSlots();

        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
    }

    private List<Slot> getSlots() {
        return Stream.generate(Slot::new)
                .limit(totalSlots)
                .toList();
    }

    @Override
    public void updateComponentsSize() {
    }

    @Override
    public void addComponentsToPanel() {
        slots.forEach(slot -> {
            this.gbc.gridx = slots.indexOf(slot) % columns;
            this.add(slot, this.gbc);
        });
    }

}
