package com.thelegendofbald.item;

import com.thelegendofbald.characters.Bald;

public class Chest extends GameItem {

    private boolean isOpen;
    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;
    private static final String ITEM_NAME = "Gambling";

    private final Bald bald;

    public Chest(int x, int y, Bald bald) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.bald = bald;
        this.isOpen = false;
        setImagePath("/images/items/chestClosed.png");
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setChestOpen() {
        if (!isOpen) {
            this.isOpen = true;
            setImagePath("/images/items/chestOpen.png");
            bald.getWallet().addCoins(1); // ➤ Aggiunge una moneta
        }
    }
}
