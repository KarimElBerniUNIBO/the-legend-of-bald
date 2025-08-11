package com.thelegendofbald.item.potions;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.item.GameItem;

public abstract class Potion extends GameItem {

    public Potion(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
    }

    // Ogni pozione deve implementare il suo effetto
    public abstract void applyEffect(Bald bald);

}
