package com.thelegendofbald.combat;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.characters.Entity;

public class CombatManager {

    public void handleAttack(Bald bald, DummyEnemy enemy) {
        int attackRange = 50; // Distanza massima per colpire il nemico
        if (isEnemyInRange(bald, enemy)) {
            enemy.takeDamage(bald.getAttackPower());
        }
    }

    private boolean isEnemyInRange(Bald bald, DummyEnemy enemy) {
        int distanceX = Math.abs(bald.getX() - enemy.getX());
        int distanceY = Math.abs(bald.getY() - enemy.getY());
        return distanceX <= 50 && distanceY <= 50;
    }
}