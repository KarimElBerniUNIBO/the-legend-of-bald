package com.thelegendofbald.model.inventory;

import java.util.Optional;

import com.thelegendofbald.model.item.GameItem;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents a slot in the inventory that can hold a GameItem.
 * This record encapsulates the item and provides methods to access it.
 * 
 * @param item The GameItem held in this slot, or null if the slot is empty.
 */
@SuppressFBWarnings(
    value = {"EI", "EI2"},
    justification = "This record is intended to be used for initializing the Slot instance without throwing exceptions."
)
public record Slot(GameItem item) {

    /**
     * Returns the GameItem held in this slot, if present.
     *
     * @return an Optional containing the GameItem if it exists, or an empty Optional otherwise
     */
    public Optional<GameItem> getItem() {
        return Optional.ofNullable(item);
    }

}
