package com.thelegendofbald.model.weapons;

import java.util.List;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.item.GameItem;

public abstract class Weapon extends GameItem {

    protected final int damage;
    protected final int attackCooldown;

    protected Weapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage, 
                     int attackCooldown) {
        super(x, y, preferredSizeX, preferredSizeY, name);
        this.damage = damage;
        this.attackCooldown = attackCooldown;
    }

    public void equip() {
        // Logic to equip the weapon
        System.out.println("Equipping weapon: " + getName());
    }

    public void unequip() {
        // Logic to unequip the weapon
        System.out.println("Unequipping weapon: " + getName());
    }

    public abstract void performAttack(Combatant attacker, List<? extends Combatant> targets);

    public int getDamage() {
        return damage;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

}
