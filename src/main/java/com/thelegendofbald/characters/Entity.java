package com.thelegendofbald.characters;


import java.awt.Rectangle;

import com.thelegendofbald.life.LifeComponent;

public abstract class Entity  {
    protected int x, y; // Posizione
    protected String name; // Nome dell'entità
    protected static LifeComponent lifeComponent;

    public Entity(int x, int y,String name , LifeComponent lifeComponent) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.lifeComponent = lifeComponent;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }


    public LifeComponent getLifeComponent(){
        return this.lifeComponent;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }


}
