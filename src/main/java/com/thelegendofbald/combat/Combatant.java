package com.thelegendofbald.combat;

public interface Combatant {
    int getAttackPower();
    void takeDamage(int damage);
    boolean isAlive();
}
