package com.thelegendofbald.model.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.model.weapons.Weapon;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class CombatManager {

    private static final int ENEMY_ATTACK_COOLDOWN = 700; // milliseconds

    private final Bald bald;
    private final List<DummyEnemy> enemies;
    private final List<Projectile> projectiles = new LinkedList<>();

    private long lastAttackTime = 0;

    @SuppressFBWarnings(
        value = {"EI2"},
        justification = "This constructor is intended to be used for initializing the CombatManager instance without throwing exceptions."
    )
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

            // bald.startAttackAnimation();
            bald.attack();
            w.performAttack(bald, enemies);
            lastAttackTime = now;

        }, () -> System.out.println("No weapon equipped."));
    }

    public void checkEnemyAttacks() {
        long now = System.currentTimeMillis();

        enemies.stream()
                .filter(enemy -> enemy.isAlive() && enemy.getBounds().intersects(bald.getBounds()))
                .filter(enemy -> now - enemy.getLastAttackTime() >= ENEMY_ATTACK_COOLDOWN)
                .forEach(enemy -> {
                    bald.takeDamage(enemy.getAttackPower());
                    enemy.setLastAttackTime(now);
                });
    }

    public void checkProjectiles() {
        enemies.stream()
                .filter(enemy -> enemy.isAlive() && projectiles.stream()
                        .anyMatch(projectile -> projectile.getBounds().intersects(enemy.getBounds())))
                .forEach(enemy -> {
                    projectiles.removeIf(projectile -> {
                        if (projectile.getBounds().intersects(enemy.getBounds())) {
                            enemy.takeDamage(projectile.getAttackPower());
                            return true;
                        }
                        return false;
                    });
                });
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public List<Projectile> getProjectiles() {
        return List.copyOf(projectiles);
    }

}
