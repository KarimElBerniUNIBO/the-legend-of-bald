package com.thelegendofbald.characters;

import java.awt.Graphics;

public class Bald extends Entity {
    private int attackPower; // Potenza d'attacco

    public Bald(int x, int y, int health, String name, int attackPower ) {
        super(x, y, health, name);
        this.attackPower = attackPower;
    }

    public int getAttackPower() { 
        return attackPower; 
    }
    public void setAttackPower(int attackPower) { 
        this.attackPower = attackPower; 
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0; // Health non può essere negativa
        }
    }

    public void heal(int amount) {
        this.health += amount;
    }

    @Override
    public void update() {
        // Logica di aggiornamento per Bald
    }

    @Override
    public void render(Graphics g) {
        // Logica di rendering per Bald
    }
    public void attack(Entity target) {

            target.setHealth(target.getHealth() - attackPower);

    }
    
}
