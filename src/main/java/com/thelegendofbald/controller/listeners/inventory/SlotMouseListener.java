package com.thelegendofbald.controller.listeners.inventory;

import java.awt.event.MouseEvent;

import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.controller.listeners.common.TemplateInteractiveComponentMouseListener;
import com.thelegendofbald.utils.ColorUtils;
import com.thelegendofbald.view.inventory.SlotPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Mouse listener for inventory slot panels.
 * Changes the background color on mouse enter and exit,
 * and selects the slot when pressed.
 */
public final class SlotMouseListener extends TemplateInteractiveComponentMouseListener {

    private static final double CHANGING_FACTOR = 0.75;

    private final SlotPanel slotPanel;
    private final Inventory inventoryManager;

    /**
     * Constructs a SlotMouseListener with the specified slot panel and inventory manager.
     *
     * @param slotPanel the slot panel to listen to
     * @param inventoryManager the inventory manager to interact with
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "This is a listener that needs to be instantiated with the slot panel and inventory manager."
    )
    public SlotMouseListener(final SlotPanel slotPanel, final Inventory inventoryManager) {
        this.slotPanel = slotPanel;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        super.mouseEntered(e);
        slotPanel.setBackground(ColorUtils.getBrightenColor(slotPanel.getBackground(), CHANGING_FACTOR));
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);
        slotPanel.setBackground(ColorUtils.getDarkenColor(slotPanel.getBackground(), CHANGING_FACTOR));
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);
        inventoryManager.select(slotPanel.getSlot());
    }

}
