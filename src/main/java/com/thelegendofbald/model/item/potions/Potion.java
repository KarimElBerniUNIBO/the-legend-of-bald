package com.thelegendofbald.model.item.potions;

import com.thelegendofbald.api.item.UsableItem;
import com.thelegendofbald.model.item.GameItem;

/**
 * An abstract base class for all potions in the game.
 * This class extends {@link GameItem} and provides the common structure
 * for potions, forcing subclasses to implement their specific effects.
 */
public abstract class Potion extends GameItem implements UsableItem {

    /**
     * Constructs a new Potion instance.
     *
     * @param x The x-coordinate of the potion.
     * @param y The y-coordinate of the potion.
     * @param width The width of the potion's bounding box.
     * @param height The height of the potion's bounding box.
     * @param name The name of the potion.
     */
    protected Potion(final int x, final int y, final int width, final int height, final String name) {
        super(x, y, width, height, name);
    }
}
