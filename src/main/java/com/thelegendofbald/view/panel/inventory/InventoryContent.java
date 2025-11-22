package com.thelegendofbald.view.panel.inventory;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.SwingUtilities;

import com.thelegendofbald.view.layout.GridBagConstraintsFactory;
import com.thelegendofbald.model.inventory.Inventory;
import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.view.layout.GridBagConstraintsFactoryImpl;

class InventoryContent extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<SlotPanel> inventorySlots;
    private final int maxSlotsPerRow;
    private final transient Inventory inventoryManager;

    InventoryContent(final int columns, final Inventory inventoryManager) {
        super();
        this.maxSlotsPerRow = columns;
        this.inventoryManager = inventoryManager;
        this.inventorySlots = this.getInventorySlots();

        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
        });
    }

    private List<SlotPanel> getInventorySlots() {
        return inventoryManager.getSlots().stream()
                .map(slot -> new SlotPanel(slot, inventoryManager))
                .toList();
    }


    @Override
    public void updateComponentsSize() {
        final int slotWidth = this.getWidth() / maxSlotsPerRow;
        final int slotHeight = this.getHeight() / (inventorySlots.size() / maxSlotsPerRow);
        final Dimension slotSize = new Dimension(slotWidth, slotHeight);

        inventorySlots.forEach(slot -> slot.setPreferredSize(slotSize));
    }

    @Override
    public void addComponentsToPanel() {
        inventorySlots.forEach(slot -> {
            this.gbc.gridx = inventorySlots.indexOf(slot) % maxSlotsPerRow;
            this.add(slot, this.gbc);
        });
        this.updateComponentsSize();
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

}
