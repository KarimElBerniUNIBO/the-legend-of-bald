package com.thelegendofbald.api.inventory;

import java.util.List;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.model.inventory.Slot;

/**
 * Represents an inventory system for the Bald character.
 * Provides methods to manage items within the inventory.
 */
public interface Inventory {

    /**
     * Sets the Bald character associated with this inventory.
     *
     * @param bald the Bald character to set
     */
    void setBald(Bald bald);

    /**
     * Adds an item to the inventory.
     *
     * @param item the item to add
     */
    void add(GameItem item);

    /**
     * Sets an item at the specified row and column in the inventory.
     *
     * @param item   the item to set
     * @param row    the row index
     * @param column the column index
     */
    void set(GameItem item, int row, int column);

    /**
     * Removes the item at the specified row and column from the inventory.
     *
     * @param row    the row index
     * @param column the column index
     */
    void remove(int row, int column);

    /**
     * Clears the inventory, removing all items.
     */
    void clear();

    /**
     * Gets the item at the specified row and column in the inventory.
     *
     * @param row    the row index
     * @param column the column index
     * @return the item at the specified position, or null if no item is present
     */
    Slot get(int row, int column);

    /**
     * Selects the item at the specified row and column in the inventory.
     *
     * @param row    the row index
     * @param column the column index
     */
    void select(int row, int column);

    /**
     * Selects the item at the specified index in the inventory.
     *
     * @param index the index of the item to select
     */
    void select(int index);

    /**
     * Selects the specified slot in the inventory.
     *
     * @param slot the slot to select
     */
    void select(Slot slot);

    /**
     * Gets the list of slots in the inventory.
     *
     * @return the list of slots
     */
    List<Slot> getSlots();

}
