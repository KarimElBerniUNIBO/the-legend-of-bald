package com.thelegendofbald.model.weapons;

import java.util.List;

import com.thelegendofbald.characters.Entity;
import com.thelegendofbald.characters.FinalBoss;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.model.combat.CombatManager;

/**
 * The {@code RangedWeapon} class represents a ranged weapon in the game.
 * It extends the {@code Weapon} class and provides specific properties for
 * ranged attacks.
 */
public abstract class RangedWeapon extends Weapon {

    private static final int PROJECTILE_SPEED = 10;

    /**
     * Constructs a new {@code RangedWeapon} with the specified parameters.
     *
     * @param x              the x-coordinate of the weapon
     * @param y              the y-coordinate of the weapon
     * @param preferredSizeX the preferred width of the weapon
     * @param preferredSizeY the preferred height of the weapon
     * @param name           the name of the weapon
     * @param damage         the damage dealt by the weapon
     * @param attackCooldown the cooldown time between attacks
     * @param combatManager  the combat manager handling combat interactions
     */
    protected RangedWeapon(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
            final String name, final int damage, final int attackCooldown, final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, attackCooldown, combatManager);
    }

    private void createProjectile(final int x, final int y, final int direction, final int speed) {
        final Projectile projectile = new Projectile(x, y, direction, speed, this.getDamage());
        this.getCombatManager().addProjectile(projectile);

    }

    /**
     * Performs an attack by creating a projectile.
     * <p>
     * Questa implementazione ignora la lista dei nemici e il boss,
     * perché il CombatManager gestisce le collisioni dei proiettili.
     * </p>
     *
     * @param attacker the entity performing the attack
     * @param targets  (Ignorato) the list of possible targets
     * @param boss     (Ignorato) the boss entity
     */
    @Override
    public void performAttack(final Combatant attacker,
                              final List<? extends Combatant> targets,
                              final FinalBoss boss) {
        
        final Entity entityAttacker = (Entity) attacker;
        final int attackX = entityAttacker.getX() + entityAttacker.getWidth() / 2;
        final int attackY = entityAttacker.getY() + entityAttacker.getHeight() / 3;
        final int correction = 22;

        if (entityAttacker.isFacingRight()) {
            this.createProjectile(attackX - correction, attackY, 0, PROJECTILE_SPEED);
        } else {
            this.createProjectile(attackX + correction, attackY, 1, PROJECTILE_SPEED);
        }
    }

}