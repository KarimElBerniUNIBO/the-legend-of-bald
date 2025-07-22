package com.thelegendofbald.api.inventory;

import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.model.inventory.Slot;

public interface Inventory {

    void add(GameItem item);

    void set(GameItem item, int row, int column);

    void remove(int row, int column);

    void clear();

    Slot get(int row, int column);

    void select(int row, int column);

}