package com.thelegendofbald.model.inventory;

import java.util.Optional;

import com.thelegendofbald.item.GameItem;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(
    value = {"EI", "EI2"},
    justification = "This record is intended to be used for initializing the Slot instance without throwing exceptions."
)
public record Slot(GameItem item) {

    public Optional<GameItem> getItem() {
        return Optional.ofNullable(item);
    }

}
