package com.thelegendofbald.model.item.weapons;

import java.awt.geom.Arc2D;
import java.util.List;

import com.thelegendofbald.model.entity.Entity;
import com.thelegendofbald.model.entity.FinalBoss;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.model.system.CombatManager;

/**
 * Abstract class representing a melee weapon in the game.
 * Melee weapons have an attack area defined by an arc and a specific attack
 * range.
 */
public abstract class MeleeWeapon extends Weapon {

    private static final int STARTING_ARC_DEGREE = 90;
    private static final int DRAWING_ANGLE_ARC_DEGREE = 180;

    private Arc2D attackArea = new Arc2D.Double(0, 0, 0, 0, 0, 0, Arc2D.PIE);
    private final int attackRange;

    /**
     * Constructor for MeleeWeapon.
     *
     * @param x              The x-coordinate of the weapon.
     * @param y              The y-coordinate of the weapon.
     * @param preferredSizeX The preferred width of the weapon.
     * @param preferredSizeY The preferred height of the weapon.
     * @param name           The name of the weapon.
     * @param damage         The damage dealt by the weapon.
     * @param attackCooldown The cooldown time between attacks.
     * @param combatManager  The combat manager handling combat logic.
     * @param attackRange    The range of the weapon's attack area.
     */
    protected MeleeWeapon(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
            final String name, final int damage, final int attackCooldown, final CombatManager combatManager,
            final int attackRange) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, attackCooldown, combatManager);
        this.attackRange = attackRange;
    }

    /**
     * Performs an attack with this melee weapon.
     * <p>
     * Calcola l'area di attacco e danneggia tutti i nemici (inclusi
     * DummyEnemy e FinalBoss) che si trovano al suo interno.
     * </p>
     *
     * @param attacker the entity performing the attack
     * @param targets  the list of enemies (usa un tipo generico per la compatibilità)
     * @param boss     the final boss
     */
    @Override
    public void performAttack(final Combatant attacker, final List<? extends Combatant> targets, final FinalBoss boss) {
        final Entity entityAttacker = (Entity) attacker;
        final int attackX = entityAttacker.getX() + entityAttacker.getWidth() / 2;
        final int attackY = entityAttacker.getY();
        final int width = this.attackRange;
        final int height = entityAttacker.getHeight();
        final int correction = 22;

        if (entityAttacker.isFacingRight()) {
            attackArea = new Arc2D.Double(attackX - correction, attackY, width, height, STARTING_ARC_DEGREE,
                    -DRAWING_ANGLE_ARC_DEGREE, Arc2D.PIE);
        } else {
            attackArea = new Arc2D.Double(attackX - width + correction, attackY, width, height, STARTING_ARC_DEGREE,
                    DRAWING_ANGLE_ARC_DEGREE, Arc2D.PIE);
        }

        targets.stream()
                .filter(target -> target.isAlive() && attackArea.intersects(target.getBounds()))
                .forEach(target -> target.takeDamage(this.getDamage()));

        if (boss != null && boss.isAlive() && attackArea.intersects(boss.getBounds())) {
            boss.takeDamage(this.getDamage());
        }
    }
    /**
     * Gets the attack range of the melee weapon.
     *
     * @return The attack range.
     */
    public int getAttackRange() {
        return attackRange;
    }

    /**
     * Gets the attack area of the melee weapon.
     *
     * @return The attack area as an Arc2D object.
     */
    public Arc2D getAttackArea() {
        return (Arc2D) attackArea.clone();
    }

}
