package com.thelegendofbald.model.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.model.weapons.Weapon;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Manages combat interactions between Bald and enemies.
 * Handles attacks, enemy attacks, and projectile interactions.
 */
public class CombatManager {

    private static final int ENEMY_ATTACK_COOLDOWN = 700; // milliseconds

    private final Bald bald;
    private final List<DummyEnemy> enemies;
    private final List<Projectile> projectiles = new LinkedList<>();

    private long lastAttackTime;

    /**
     * Constructs a CombatManager with the specified Bald character and enemies.
     *
     * @param bald    The Bald character involved in combat.
     * @param enemies The list of enemies that Bald will interact with.
     */
    @SuppressFBWarnings(
        value = "EI2", 
        justification = "This constructor is intended to be used for initializing"
        + " the CombatManager instance without throwing exceptions."
        )
    public CombatManager(final Bald bald, final List<DummyEnemy> enemies) {
        this.bald = bald;
        this.enemies = enemies;
    }

    /**
     * Attempts to make Bald attack using the equipped weapon.
     * If the weapon is on cooldown, the attack is not performed.
     */
    public void tryToAttack() {
        final Optional<Weapon> weapon = bald.getWeapon();
        final long now = System.currentTimeMillis();
        final long cooldown = now - lastAttackTime;

        weapon.ifPresent(w -> {
            if (cooldown < w.getAttackCooldown()) {
                return;
            }

            bald.attack();
            w.performAttack(bald, enemies);
            lastAttackTime = now;
        });
    }

    /**
     * Checks for enemy attacks on Bald.
     */
    public void checkEnemyAttacks() {
        final long now = System.currentTimeMillis();

        enemies.stream()
                .filter(enemy -> enemy.isAlive() && enemy.getBounds().intersects(bald.getBounds()))
                .filter(enemy -> now - enemy.getLastAttackTime() >= ENEMY_ATTACK_COOLDOWN)
                .forEach(enemy -> {
                    bald.takeDamage(enemy.getAttackPower());
                    enemy.setLastAttackTime(now);
                });
    }

    /**
     * Checks for projectile collisions with enemies.
     * If a projectile hits an enemy, the enemy takes damage and the projectile is
     * removed.
     */
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

    /**
     * Adds a projectile to the combat manager.
     *
     * @param projectile The projectile to be added.
     */
    public void addProjectile(final Projectile projectile) {
        projectiles.add(projectile);
    }

    /**
     * Returns an unmodifiable view of the projectiles currently in the combat
     * manager.
     *
     * @return A list of projectiles.
     */
    public List<Projectile> getProjectiles() {
        return List.copyOf(projectiles);
    }

}
