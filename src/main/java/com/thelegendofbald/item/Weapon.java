package com.thelegendofbald.item;

public abstract class Weapon extends GameItem {

    private final int damage;

    protected Weapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage) {
        super(x, y, preferredSizeX, preferredSizeY, name);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void equip() {
        // Logic to equip the weapon
        System.out.println("Equipping weapon: " + getName());
    }

    public void unequip() {
        // Logic to unequip the weapon
        System.out.println("Unequipping weapon: " + getName());
    }

}
