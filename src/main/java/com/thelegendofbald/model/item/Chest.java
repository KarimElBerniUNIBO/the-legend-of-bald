package com.thelegendofbald.model.item;

import com.thelegendofbald.api.interactable.Interactable;
import com.thelegendofbald.characters.Bald;

public class Chest extends GameItem implements Interactable {

    private boolean isOpen;
    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;
    private static final String ITEM_NAME = "Treasure Chest";
    private static final int REWARD_AMOUNT = 10;

    public Chest(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.isOpen = false;
        loadImage("/images/items/chestClosed.png");
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void interact(final Bald bald) {
        open(bald);
    }

    public void open(final Bald bald) {
        if (isOpen) return;
        this.isOpen = true;
        loadImage("/images/items/chestOpen.png");
        bald.getWallet().addCoins(REWARD_AMOUNT);
    }
}
