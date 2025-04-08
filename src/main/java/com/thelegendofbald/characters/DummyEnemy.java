package com.thelegendofbald.characters;

import java.awt.Graphics;

public class DummyEnemy extends Entity {

    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }
    private int attackPower; // Potenza d'attacco

    public DummyEnemy(int x, int y, int health, String name, int attackPower) {
        super(x, y, health, name);
        this.attackPower = attackPower;
    }

    public int getAttackPower() { 
        return attackPower; 
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
        // Logica di aggiornamento per DummyEnemy
    }

    @Override
    public void render(Graphics g) {
        // Logica di rendering per DummyEnemy
    }
    
}
