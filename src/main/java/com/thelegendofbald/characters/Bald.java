package com.thelegendofbald.characters;

import java.awt.Graphics;

public class Bald extends Entity {
    private int attackPower; // Potenza d'attacco
    private int defense; // Difesa

    public Bald(int x, int y, int width, int height, int health, String name, int attackPower, int defense) {
        super(x, y, width, height, health, name);
        this.attackPower = attackPower;
        this.defense = defense;
    }

    public int getAttackPower() { 
        return attackPower; 
    }
    public void setAttackPower(int attackPower) { 
        this.attackPower = attackPower; 
    }
    public int getDefense() { 
        return defense; 
    }
    public void setDefense(int defense) { 
        this.defense = defense; 
    }
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0; // Assicurati che la vita non scenda sotto zero
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
