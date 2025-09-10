package com.thelegendofbald.api.item;

import com.thelegendofbald.characters.Bald;

/**
 * An interface for items that can be used by the player to apply a direct effect.
 *
 * This interface defines a contract for any item, such as a potion,
 * that has a specific action or effect when consumed or used.
 */
public interface UsableItem {

    /**
     * Applies the item's unique effect to the player.
     * The implementation of this method will define the specific action
     * that occurs when the item is used.
     *
     * @param player The instance of the player character to which the effect is applied.
     */
    void applyEffect(Bald player);

}
