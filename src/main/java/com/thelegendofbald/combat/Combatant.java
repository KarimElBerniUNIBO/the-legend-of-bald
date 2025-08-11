package com.thelegendofbald.combat;

import java.awt.Rectangle;

public interface Combatant {
    int getAttackPower();
    void takeDamage(int damage);
    Rectangle getBounds();
    boolean isAlive();
        
}
