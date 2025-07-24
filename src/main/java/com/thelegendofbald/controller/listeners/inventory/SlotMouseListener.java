package com.thelegendofbald.controller.listeners.inventory;

import java.awt.event.MouseEvent;

import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.controller.listeners.common.TemplateInteractiveComponentMouseListener;
import com.thelegendofbald.utils.ColorUtils;
import com.thelegendofbald.view.inventory.SlotPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class SlotMouseListener extends TemplateInteractiveComponentMouseListener {

    private static final double CHANGING_FACTOR = 0.75;

    private final SlotPanel slotPanel;
    private final Inventory inventoryManager;

    @SuppressFBWarnings(
        value = {"EI2"},
        justification = "This is a listener that needs to be instantiated with the slot panel and inventory manager."
    )
    public SlotMouseListener(SlotPanel slotPanel, Inventory inventoryManager) {
        this.slotPanel = slotPanel;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        slotPanel.setBackground(ColorUtils.getBrightenColor(slotPanel.getBackground(), CHANGING_FACTOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        slotPanel.setBackground(ColorUtils.getDarkenColor(slotPanel.getBackground(), CHANGING_FACTOR));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        inventoryManager.select(slotPanel.getSlot());
    }

}
