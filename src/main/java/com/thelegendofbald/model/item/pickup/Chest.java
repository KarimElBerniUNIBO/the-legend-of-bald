package com.thelegendofbald.model.item.pickup;

import com.thelegendofbald.model.item.Interactable;
import com.thelegendofbald.model.item.GameItem;

/**
 * Represents a treasure chest in the game that can be opened by the player (Bald).
 * When opened, the chest rewards the player with a set amount of coins.
 * This class extends {@link GameItem} and implements the {@link Interactable} interface.
 */
public class Chest extends GameItem implements Interactable {

    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;
    private static final String ITEM_NAME = "Chest";
    private static final String IMAGE_PATH = "/images/items/chestClosed.png";

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
        super.loadImage(IMAGE_PATH);
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
     */
    @Override
    public void interact() {
        open();
    }

    /**
     * Opens the chest, changing its state and rewarding Bald with coins.
     * If the chest is already open, this method has no effect.
     */
    public void open() {
        if (isOpen) {
            return;
        }
        this.isOpen = true;
        super.loadImage("/images/items/chestOpen.png");
    }
}
