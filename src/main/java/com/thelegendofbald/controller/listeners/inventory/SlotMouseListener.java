package com.thelegendofbald.controller.listeners.inventory;

import java.awt.event.MouseEvent;

import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.controller.listeners.common.TemplateInteractiveComponentMouseListener;
import com.thelegendofbald.utils.ColorUtils;
import com.thelegendofbald.view.inventory.SlotPanel;

public class SlotMouseListener extends TemplateInteractiveComponentMouseListener {

    private static final double CHANGING_FACTOR = 0.75;

    private final SlotPanel slotPanel;
    private final Inventory inventoryManager;

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
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        SlotPanel slot = (SlotPanel) e.getSource();
        inventoryManager.select(slot.getSlot());
        
    }

}
