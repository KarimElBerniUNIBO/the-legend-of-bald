package com.thelegendofbald.characters;



public class DummyEnemy extends Entity {


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

    
}
