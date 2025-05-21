package com.thelegendofbald.item;

public class Potion extends GameItem{

    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;
    private final static String ITEM_NAME = "Potion";
    private static String path = "/images/items/wine.jpg";

    public Potion(int x,int y){
        super(x,y,WIDTH, HEIGHT, ITEM_NAME);
        super.loadImage(path);
    }

}
