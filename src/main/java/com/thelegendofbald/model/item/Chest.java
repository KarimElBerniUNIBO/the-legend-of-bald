package com.thelegendofbald.model.item;

import com.thelegendofbald.api.interactable.Interactable;
import com.thelegendofbald.characters.Bald;

/**
 * Represents a treasure chest in the game that can be opened by the player (Bald).
 * When opened, the chest rewards the player with a set amount of coins.
 * This class extends {@link GameItem} and implements the {@link Interactable} interface.
 */
public class Chest extends GameItem implements Interactable {

    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;
    private static final String ITEM_NAME = "Treasure Chest";
    private static final int REWARD_AMOUNT = 10;

    private boolean isOpen;

    /**
     * Constructs a new Chest instance at the specified coordinates.
     *
     * @param x The x-coordinate of the chest.
     * @param y The y-coordinate of the chest.
     */
    public Chest(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.isOpen = false;
        loadImage("/images/items/chestClosed.png");
    }

    /**
     * Checks if the chest is currently open.
     *
     * @return true if the chest is open, false otherwise.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Defines the interaction behavior when the chest is interacted with by Bald.
     * If the chest is not already open, it will be opened and Bald will receive coins.
     *
     * @param bald The Bald character interacting with the chest.
     */
    @Override
    public void interact(final Bald bald) {
        open(bald);
    }

    /**
     * Opens the chest, changing its state and rewarding Bald with coins.
     * If the chest is already open, this method has no effect.
     *
     * @param bald The Bald character opening the chest.
     */
    public void open(final Bald bald) {
        if (isOpen) {
            return;
        }
        this.isOpen = true;
        loadImage("/images/items/chestOpen.png");
        bald.getWallet().addCoins(REWARD_AMOUNT);
    }
}
