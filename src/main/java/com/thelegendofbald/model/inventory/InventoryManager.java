package com.thelegendofbald.model.inventory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.model.weapons.Weapon;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class InventoryManager implements Inventory {

    private final int rows;
    private final int columns;
    private final List<Slot> inventory;
    private Bald bald;

    public InventoryManager(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.inventory = this.getInventory(this.rows * this.columns);
    }

    private List<Slot> getInventory(int slots) {
        return Stream.generate(() -> new Slot(null))
                .limit(slots)
                .collect(Collectors.toList());
    }

    @SuppressFBWarnings(
        value = {"EI2"},
        justification = "This method is designed to set the Bald instance for the InventoryManager."
    )
    @Override
    public void setBald(Bald bald) {
        this.bald = bald;
    }

    @Override
    public void add(GameItem item) {
        inventory.stream()
                .filter(slot -> !slot.getItem().isPresent())
                .findFirst()
                .ifPresentOrElse(slot -> inventory.set(inventory.indexOf(slot), new Slot(item)), 
                () -> { throw new IllegalStateException("Inventory is full"); });
    }

    @Override
    public void set(GameItem item, int row, int column) {
        int index = row * columns + column;
        if (index < 0 || index >= inventory.size()) {
            throw new IndexOutOfBoundsException("Invalid slot index: " + index);
        }

        inventory.set(index, new Slot(item));
    }

    @Override
    public void remove(int row, int column) {
        int index = row * columns + column;
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
    public Slot get(int row, int column) {
        int index = row * columns + column;
        if (index < 0 || index >= inventory.size()) {
            throw new IndexOutOfBoundsException("Invalid row or column: " + row + ", " + column);
        }
        return inventory.get(index);
    }

    private void handleItemSelection(Slot slot) {
        slot.getItem().ifPresent(item -> {
            System.out.println("Selected item: " + item.getName());

            if (item instanceof Weapon weapon) {
                Optional.ofNullable(bald).ifPresent(b -> b.setWeapon(weapon));
            }
        });
    }

    @Override
    public void select(int row, int column) {
        Slot slot = this.get(row, column);
        this.select(slot);
    }

    @Override
    public void select(int index) {
        Slot slot = inventory.get(index);
        this.select(slot);
    }

    @Override
    public void select(Slot slot) {
        this.handleItemSelection(slot);
    }

    @SuppressFBWarnings(
        value = {"EI"},
        justification = "This method is designed to return the list of slots in the inventory without throwing exceptions."
    )
    @Override
    public List<Slot> getSlots() {
        return this.inventory;
    }


}
