package com.thelegendofbald.model.item;

import java.awt.Image;

public interface ShopItem {
    String getDisplayName();
    String getDescription();
    int getPrice();
    Image getSprite();
}