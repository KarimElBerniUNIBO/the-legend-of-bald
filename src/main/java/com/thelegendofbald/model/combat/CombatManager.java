package com.thelegendofbald.model.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.characters.FinalBoss;
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
    private FinalBoss boss; // MODIFICA: Rimosso 'final', non è nel costruttore
    private final List<Projectile> projectiles = new LinkedList<>();

    private long lastAttackTime;

    /**
     * Constructs a CombatManager. Il Boss viene aggiunto in seguito.
     *
     * @param bald    The Bald character involved in combat.
     * @param enemies The list of enemies that Bald will interact with.
     */
    @SuppressFBWarnings(
        value = "EI2", 
        justification = "This constructor is intended to be used for initializing"
        + " the CombatManager instance without throwing exceptions."
        )
    // MODIFICA: Rimosso 'FinalBoss boss' dal costruttore
    public CombatManager(final Bald bald, final List<DummyEnemy> enemies) {
        this.bald = bald;
        this.enemies = enemies;
        this.boss = null; // Il boss è nullo all'inizio
    }

    /**
     * Registra il Boss nel CombatManager dopo che è stato creato.
     * @param boss Il FinalBoss da aggiungere
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "CombatManager needs to operate on the same instance of the boss"
        + " created and managed by GamePanel. This is by design."
    )
    public void setBoss(final FinalBoss boss) {
        this.boss = boss;
    }

    /**
     * Attempts to make Bald attack using the equipped weapon.
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
            
            // MODIFICA: Passa anche il boss al metodo di attacco.
            // Questo richiederà di aggiornare le tue classi Weapon (es. Sword).
            w.performAttack(bald, enemies, boss); 
            
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
        
        // NOTA: Gli attacchi del Boss (Melee/AOE) sono gestiti
        // nel suo metodo followPlayer(), quindi non serve aggiungerli qui.
    }

    /**
     * Checks for projectile collisions with enemies AND THE BOSS.
     */
    public void checkProjectiles() {
        // 1. Controlla i nemici normali (codice esistente)
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

        // 2. --- MODIFICA DA AGGIUNGERE ---
        // Controlla anche il boss, se esiste ed è vivo
        if (boss != null && boss.isAlive()) {
            projectiles.removeIf(projectile -> {
                if (projectile.getBounds().intersects(boss.getBounds())) {
                    boss.takeDamage(projectile.getAttackPower());
                    return true; // Il proiettile colpisce e viene rimosso
                }
                return false;
            });
        }
        // --- FINE MODIFICA ---

        projectiles.removeIf(projectile -> !projectile.isAlive());
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
     * Returns an unmodifiable view of the projectiles.
     *
     * @return A list of projectiles.
     */
    public List<Projectile> getProjectiles() {
        return List.copyOf(projectiles);
    }
}