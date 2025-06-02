package com.thelegendofbald.controller.listeners.common;

import java.awt.event.MouseEvent;

import com.thelegendofbald.utils.ColorUtils;
import com.thelegendofbald.view.inventory.Slot;

public class SlotMouseListener extends TemplateInteractiveComponentMouseListener {

    private static final double CHANGING_FACTOR = 0.75;

    private final Slot slot;

    public SlotMouseListener(Slot slot) {
        this.slot = slot;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        slot.setBackground(ColorUtils.getBrightenColor(slot.getBackground(), CHANGING_FACTOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        slot.setBackground(ColorUtils.getDarkenColor(slot.getBackground(), CHANGING_FACTOR));
    }

}
