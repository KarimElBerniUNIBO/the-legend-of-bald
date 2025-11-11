package com.thelegendofbald.model.item;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.model.common.Wallet;
import com.thelegendofbald.utils.LoggerUtils;

public class Coin extends GameItem {

    private static final String COIN_NAME = "Coin";
    private static final String COIN_DESCRIPTION = "A shiny gold coin.";
    private static final int COIN_PRICE = 1;
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
        this.setDescription(COIN_DESCRIPTION);
        this.setPrice(COIN_PRICE);
        this.loadSprite();
    }

    /**
     * Loads the sprite image for the coin.
     */
    private void loadSprite() {
        try {
            this.setSprite(ImageIO.read(getClass().getResourceAsStream("/images/items/gold.png")));
        } catch (IOException e) {
            LoggerUtils.info("Failed to load coin sprite: " + e.getMessage());
        }
    }
    
    /**
     * Adds the coin's value to the specified wallet.
     *
     * @param wallet the wallet to add the coin's value to
     */
    public void addToWallet(final Bald bald){
        bald.getWallet().addCoins(Coin.COIN_VALUE);
        LoggerUtils.info(bald.getWallet().getCoins() + " coins in wallet after collecting a coin.");
    }

}
