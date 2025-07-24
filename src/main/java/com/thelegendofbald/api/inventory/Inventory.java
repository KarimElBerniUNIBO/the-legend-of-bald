package com.thelegendofbald.api.inventory;

import java.util.List;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.model.inventory.Slot;

public interface Inventory {

    void setBald(Bald bald);

    void add(GameItem item);

    void set(GameItem item, int row, int column);

    void remove(int row, int column);

    void clear();

    Slot get(int row, int column);

    void select(int row, int column);

    void select(int index);

    void select(Slot slot);

    List<Slot> getSlots();

}