package com.thelegendofbald.item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class GameItem{

    private int x;
    private int y;
    private int preferredSizeX;
    private int preferredSizeY;
    private String name;
    private BufferedImage sprite;

    protected GameItem(int x,int y,int preferredSizeX, int preferredSizeY,String name){
        this.x = x;
        this.y = y;
        this.preferredSizeX = preferredSizeX;
        this.preferredSizeY = preferredSizeY;
        this.name = name;
    }

    protected void loadImage(String path) {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImagePath(String path) {
        loadImage(path);
    }

    public void render(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, preferredSizeX, preferredSizeY, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, preferredSizeX, preferredSizeY);
        }
    }
    
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BufferedImage getSprite() {
        return sprite;
    }
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public int getPreferredSizeX() {
        return preferredSizeX;
    }

    public int getPreferredSizeY() {
        return preferredSizeY;
    }

    public void setPreferredSizeX(int preferredSizeX) {
        this.preferredSizeX = preferredSizeX;
    }

    public void setPreferredSizeY(int preferredSizeY) {
        this.preferredSizeY = preferredSizeY;
    }

    

}
