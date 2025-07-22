package com.thelegendofbald.model.inventory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.item.GameItem;

public class InventoryManager implements Inventory {

    private final int rows;
    private final int columns;
    private final List<Slot> inventory;

    public InventoryManager(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.inventory = this.getInventory(rows * columns);
    }

    private List<Slot> getInventory(int slots) {
        return Stream.generate(() -> new Slot(null))
                .limit(slots)
                .collect(Collectors.toList());
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

    @Override
    public void select(int row, int column) {
        Slot slot = this.get(row, column);
        slot.getItem().ifPresent(item -> {
            // Handle item selection logic here
            System.out.println("Selected item: " + item.getName());
        });
    }

}
