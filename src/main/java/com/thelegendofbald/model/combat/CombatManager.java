package com.thelegendofbald.model.combat;

import java.util.List;
import java.util.Optional;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.model.weapons.Weapon;

public class CombatManager {

    private static final int ENEMY_ATTACK_COOLDOWN = 700; // milliseconds

    private final Bald bald;
    private final List<DummyEnemy> enemies;
    
    private long lastAttackTime = 0;

    public CombatManager(Bald bald, List<DummyEnemy> enemies) {
        this.bald = bald;
        this.enemies = enemies;
    }

    public void tryToAttack() {
        Optional<Weapon> weapon = bald.getWeapon();
        long now = System.currentTimeMillis();
        long cooldown = now - lastAttackTime;

        weapon.ifPresentOrElse(w -> {
            if (cooldown < w.getAttackCooldown()) {
                System.out.println("Weapon is on cooldown. Please wait.");
                return;
            }

            bald.startAttackAnimation();
            w.performAttack(bald, enemies);
            lastAttackTime = now;

        }, () -> System.out.println("No weapon equipped."));
    }

    public void checkEnemyAttacks() {
        long now = System.currentTimeMillis();

        enemies.stream()
            .filter(enemy -> enemy.isAlive() && enemy.getBounds().intersects(bald.getBounds()))
            .forEach(enemy -> {
                if (now - enemy.getLastAttackTime() >= ENEMY_ATTACK_COOLDOWN) {
                    bald.takeDamage(enemy.getAttackPower());
                    enemy.setLastAttackTime(now);
                }
            });
    }

}
