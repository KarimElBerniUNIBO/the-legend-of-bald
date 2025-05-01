package com.thelegendofbald.item;

public class Chest extends GameItem {

    private boolean isOpen;
    private static final String ITEM_NAME = "Gambling";
    private static final String IMAGE_PATH = "/images/items/chestClosed.png";

    public Chest(int x, int y) {
        super(x, y,ITEM_NAME ,IMAGE_PATH);
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

}
