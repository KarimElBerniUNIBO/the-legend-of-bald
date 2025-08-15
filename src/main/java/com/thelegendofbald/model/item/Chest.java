package com.thelegendofbald.model.item;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.api.interactable.Interactable;

/**
 * Represents an interactable chest in the game.
 * It can only be opened by the player if they have the correct key.
 *
 * @see com.thelegendofbald.api.interactable.Interactable
 * @see com.thelegendofbald.model.item.GameItem
 */
public class Chest extends GameItem implements Interactable {

    private boolean isOpen;
    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;
    private static final String ITEM_NAME = "Treasure Chest";
    private static final int REWARD_AMOUNT = 10;

    /**
     * Constructs a new Chest instance.
     * The chest is initially closed and requires a specific key to be opened.
     *
     * @param x The x-position of the chest on the map.
     * @param y The y-position of the chest on the map.
     * @param requiredKeyId The ID of the key needed to open this chest.
     */
    public Chest(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.isOpen = false;
        loadImage("/images/items/chestClosed.png");
    }

    /**
     * Checks if the chest has already been opened.
     *
     * @return true if the chest is open, false otherwise.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Handles the player's interaction with the chest.
     * If the chest is closed, it checks if the player has the correct key.
     * If the key is present, the chest opens, the player receives a reward,
     * and the key is removed from the inventory.
     *
     * @param bald The player instance interacting with the chest.
     * @param inventory The player's inventory.
     */
    @Override
    public void interact(final Bald bald) {
        this.open(bald);
    }

    /**
     * Opens the chest, applies the reward to the player, and removes the key.
     * This method encapsulates the opening logic.
     *
     * @param bald The player instance.
     * @param inventory The player's inventory.
     */
    private void open(final Bald bald) {
        this.isOpen = true;
        loadImage("/images/items/chestOpen.png");
        bald.getWallet().addCoins(REWARD_AMOUNT);
    }
}