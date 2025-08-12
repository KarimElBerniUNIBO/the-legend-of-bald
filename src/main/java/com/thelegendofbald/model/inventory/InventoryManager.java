package com.thelegendofbald.model.inventory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.item.Key;
import com.thelegendofbald.model.weapons.Weapon;
import com.thelegendofbald.view.inventory.InventoryPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Manages the inventory of the game, allowing for adding, removing, and selecting items.
 * This class implements the Inventory interface and provides methods to manipulate the inventory slots.
 */
public final class InventoryManager implements Inventory {

    private final int rows;
    private final int columns;
    private final List<Slot> inventory;
    private Bald bald;

    /**
     * Constructs an InventoryManager with the specified number of rows and columns.
     *
     * @param rows    the number of rows in the inventory
     * @param columns the number of columns in the inventory
     */
    public InventoryManager(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;
        this.inventory = this.getInventory(this.rows * this.columns);
    }

    private List<Slot> getInventory(final int slots) {
        return Stream.generate(() -> new Slot(null))
                .limit(slots)
                .collect(Collectors.toList());
    }

    @SuppressFBWarnings(
        value = "EI2",
        justification = "This method is designed to set the Bald instance for the InventoryManager."
    )
    @Override
    public void setBald(final Bald bald) {
        this.bald = bald;
    }

    @Override
    public void add(final GameItem item) {
        inventory.stream()
                .filter(slot -> !slot.getItem().isPresent())
                .findFirst()
                .ifPresent(slot -> inventory.set(inventory.indexOf(slot), new Slot(item)));
    }

    @Override
    public void set(final GameItem item, final int row, final int column) {
        final int index = row * columns + column;
        if (index < 0 || index >= inventory.size()) {
            throw new IndexOutOfBoundsException("Invalid slot index: " + index);
        }

        inventory.set(index, new Slot(item));
    }

    @Override
    public void remove(final int row, final int column) {
        final int index = row * columns + column;
        if (index < 0 || index >= inventory.size()) {
            throw new IndexOutOfBoundsException("Invalid slot index: " + index);
        }

        inventory.set(index, new Slot(null));
    }

    @Override
    public void clear() {
        inventory.replaceAll(slot -> new Slot(null));
    }

    @Override
    public Slot get(final int row, final int column) {
        final int index = row * columns + column;
        if (index < 0 || index >= inventory.size()) {
            throw new IndexOutOfBoundsException("Invalid row or column: " + row + ", " + column);
        }
        return inventory.get(index);
    }

    private void handleItemSelection(final Slot slot) {
        slot.getItem().ifPresent(item -> {
            if (item instanceof Weapon weapon) {
                Optional.ofNullable(bald).ifPresent(b -> b.setWeapon(weapon));
            }
        });
    }

    @Override
    public void select(final int row, final int column) {
        final Slot slot = this.get(row, column);
        this.select(slot);
    }

    @Override
    public void select(final int index) {
        final Slot slot = inventory.get(index);
        this.select(slot);
    }

    @Override
    public void select(final Slot slot) {
        this.handleItemSelection(slot);
    }

    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is designed to return the list of slots in the inventory without throwing exceptions."
    )
    @Override
    public List<Slot> getSlots() {
        return this.inventory;
    }

    @Override
    public boolean hasKey(final int keyId) {
        return this.inventory.stream()
            .anyMatch(slot -> slot.getItem()
                    .filter(item -> item instanceof Key && ((Key) item).getKeyId() == keyId)
                    .isPresent());
    }

    @Override
    public void removeKey(final int keyId) {
        Optional<Slot> slotToRemove = this.inventory.stream()
            .filter(slot -> slot.getItem()
                    .filter(item -> item instanceof Key && ((Key) item).getKeyId() == keyId)
                    .isPresent())
            .findFirst();
        
        slotToRemove.ifPresent(slot -> {
            int index = this.inventory.indexOf(slot);
            this.inventory.set(index, new Slot(null));
        });
    }
}