package com.thelegendofbald.model.item;

import com.thelegendofbald.api.inventory.Inventory;

public interface InventoryItem {
    void onCollect(Inventory inventory);
}