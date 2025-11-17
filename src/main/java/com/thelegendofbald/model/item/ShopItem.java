package com.thelegendofbald.model.item;

import java.awt.Image;

/**
 * Represents an item available for purchase in the game's shop.
 *
 * <p>Implementations provide a user-facing name, a short description, a price
 * expressed in the game's currency, and an optional sprite used for UI rendering.</p>
 */
public interface ShopItem {
    /**
     * Returns the display name of the item (shown in shop lists and dialogs).
     *
     * @return the item's display name (not necessarily unique)
     */
    String getDisplayName();

    /**
     * Returns a short description of the item suitable for tooltips or item detail views.
     *
     * @return the item's description (may be empty but should preferably not be {@code null})
     */
    String getDescription();

    /**
     * Returns the purchase price of the item in the game's currency.
     *
     * @return the price (expected to be non-negative)
     */
    int getPrice();

    /**
     * Returns the sprite image used to represent the item in the UI.
     *
     * @return an {@link Image} for the item's sprite, or {@code null} if no sprite is available
     */
    Image getSprite();
}
