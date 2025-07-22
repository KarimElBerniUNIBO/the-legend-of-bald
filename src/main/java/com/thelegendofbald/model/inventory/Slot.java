package com.thelegendofbald.model.inventory;

import java.util.Optional;

import com.thelegendofbald.item.GameItem;

public record Slot(GameItem item) {

    public Optional<GameItem> getItem() {
        return Optional.ofNullable(item);
    }

}
