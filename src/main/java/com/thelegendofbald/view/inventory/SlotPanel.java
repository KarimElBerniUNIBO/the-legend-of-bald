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

/**
 * A panel representing a single inventory slot.
 * It displays the item contained in the slot, if any.
 */
public final class SlotPanel extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    private static final Color DEFAULT_BG_COLOR = new Color(60, 60, 60, 180);
    private static final Pair<Double, Double> LABEL_SIZE_PROPORTION = Pair.of(0.9, 0.9);
    private static final Pair<Double, Double> LABEL_FONT_MULTIPLICATOR = Pair.of(1.75, 1.75);

    private final transient TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private transient Optional<TextLabel> itemLabel = Optional.empty();

    private final transient Slot slot;

    /**
     * Constructs a SlotPanel for the given slot and inventory manager.
     *
     * @param slot the slot to be represented by this panel
     * @param inventoryManager the inventory manager to handle interactions
     */
    public SlotPanel(final Slot slot, final Inventory inventoryManager) {
        super();
        this.slot = slot;

        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        SwingUtilities.invokeLater(() -> this.setBackground(DEFAULT_BG_COLOR));
        this.addMouseListener(new SlotMouseListener(this, inventoryManager));
    }

    @Override
    protected void initializeComponents() {
        slot.getItem().ifPresent(item -> {
            this.itemLabel = Optional.of(tlFactory.createTextLabelWithProportion(item.getName(), this.getSize(),
                    Optional.of(LABEL_SIZE_PROPORTION), Optional.of(LABEL_FONT_MULTIPLICATOR), Optional.empty(),
                    Optional.empty()));
        });
        super.initializeComponents();
    }

    /**
     * Returns the slot associated with this panel.
     *
     * @return the slot
     */
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
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

}
