package com.thelegendofbald.model.item;

import com.thelegendofbald.api.inventory.Inventory;

/**
 * Represents a key item in the game.
 * Keys have a unique ID that can be used to unlock specific chests or doors.
 * This class implements the {@link InventoryItem} interface, allowing it to be
 * added to the player's inventory upon collection.
 */
public class Key extends GameItem implements InventoryItem {
    
    // The key's unique ID, used to match it with a specific lock.
    private final int keyId;

    /**
     * Constructs a new Key instance.
     * * @param x The x-coordinate of the key in the game world.
     * @param y The y-coordinate of the key in the game world.
     * @param keyId The unique ID of this key.
     */
    public Key(int x, int y, int keyId) {
        super(x, y, 32, 32, "Key");
        this.keyId = keyId;
        loadImage("/images/items/key.png");
    }

    /**
     * Retrieves the unique ID of the key.
     * * @return The key's ID.
     */
    public int getKeyId() {
        return keyId;
    }

    /**
     * Handles the collection of the key by adding it to the player's inventory.
     * This method is called when the player interacts with the key in the game world.
     *
     * @param inventory The inventory to which the key will be added.
     */
    @Override
    public void onCollect(Inventory inventory) {
        inventory.add(this);
        System.out.println("Key collected and added to inventory: Key ID " + keyId);
    }
}