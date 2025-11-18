package com.thelegendofbald.model.item;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * Represents a coin item that can be collected by the player.
 * When collected, it adds a fixed amount of coins to the player's wallet.
 */
public class Coin extends GameItem {

    private static final String COIN_NAME = "Coin";
    private static final String COIN_DESCRIPTION = "A shiny gold coin.";
    private static final String IMAGE_PATH = "/images/items/gold.png";
    private static final int COIN_SIZE = 32;
    private static final int COIN_VALUE = 20;

    /**
     * Constructs a new Coin item at the specified position.
     *
     * @param x the x-coordinate of the coin
     * @param y the y-coordinate of the coin
     */
    public Coin(final int x, final int y) {
        super(x, y, COIN_SIZE, COIN_SIZE, COIN_NAME);
        super.setDescription(COIN_DESCRIPTION);
        super.loadImage(IMAGE_PATH);
    }
    /**
     * Adds the coin's value to the specified wallet.
     *
     * @param bald the Bald character whose wallet will be credited
     */
    public void addToWallet(final Bald bald) {
        bald.addCoins(COIN_VALUE);
        LoggerUtils.info(bald.getCoins() + " coins in wallet after collecting a coin.");
    }

}
