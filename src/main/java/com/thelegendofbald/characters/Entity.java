package com.thelegendofbald.characters;


import java.awt.Rectangle;

import com.thelegendofbald.life.LifeComponent;

public abstract class Entity  {
    protected int x, y; // Posizione
    protected int width, height; // Dimensioni
    protected String name; // Nome dell'entità
    protected LifeComponent lifeComponent;

    protected boolean facingRight = true; // Direzione in cui Bald sta guardando

    public Entity(int x, int y, int width, int height, String name, LifeComponent lifeComponent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.lifeComponent = lifeComponent;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public boolean isFacingRight() {
        return facingRight; // Restituisce 1 se sta guardando a destra, -1 altrimenti
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }


    public LifeComponent getLifeComponent(){
        return this.lifeComponent;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }

}
