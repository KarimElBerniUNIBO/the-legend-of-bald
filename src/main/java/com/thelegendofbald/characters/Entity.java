package com.thelegendofbald.characters;



public abstract class Entity {
    protected int x, y; // Posizione
    protected int health; // Vita
    protected String name; // Nome dell'entità

    public Entity(int x, int y, int health, String name) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.name = name;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getHealth() { return health; }
    public void setHealth(int health) {this.health = health;}

}
