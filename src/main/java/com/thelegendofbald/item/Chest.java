package com.thelegendofbald.item;

public class Chest extends GameItem {

    private boolean isOpen;
    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;
    private static final String ITEM_NAME = "Gambling";
    private String imagePath = "/images/items/chestClosed.png";
    
        public Chest(int x, int y) {
            super(x, y,HEIGHT,WIDTH,ITEM_NAME);
            loadImage(imagePath);
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setChestOpen(){
        this.imagePath = "/images/items/chestOpen.png";
        this.isOpen = true;
        loadImage(imagePath);
    }

}
