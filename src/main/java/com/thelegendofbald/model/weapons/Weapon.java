package com.thelegendofbald.model.weapons;

import java.util.List;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.item.GameItem;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents a weapon that can be used by a combatant.
 */
public abstract class Weapon extends GameItem {

    private final int damage;
    private final int attackCooldown;
    private final CombatManager combatManager;

    /**
     * Constructs a Weapon instance.
     *
     * @param x                The x-coordinate of the weapon.
     * @param y                The y-coordinate of the weapon.
     * @param preferredSizeX   The preferred width of the weapon.
     * @param preferredSizeY   The preferred height of the weapon.
     * @param name             The name of the weapon.
     * @param damage           The damage dealt by the weapon.
     * @param attackCooldown   The cooldown time between attacks with the weapon.
     * @param combatManager    The combat manager associated with this weapon.
     */
    protected Weapon(final int x, final int y, final int preferredSizeX, final int preferredSizeY, final String name,
            final int damage,
            final int attackCooldown, final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, name);
        this.damage = damage;
        this.attackCooldown = attackCooldown;
        this.combatManager = combatManager;
    }

    /**
     * Performs an attack with this weapon on the specified targets.
     *
     * @param attacker The combatant who is attacking.
     * @param targets  The list of combatants that are the targets of the attack.
     */
    public abstract void performAttack(Combatant attacker, List<? extends Combatant> targets);

    /**
     * Gets the damage dealt by this weapon.
     *
     * @return The damage value.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the cooldown time between attacks with this weapon.
     *
     * @return The attack cooldown time in milliseconds.
     */
    public int getAttackCooldown() {
        return attackCooldown;
    }

    /**
     * Gets the combat manager associated with this weapon.
     *
     * @return The combat manager instance.
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is intended to expose the combat manager for use in combat operations."
    )
    public CombatManager getCombatManager() {
        return combatManager;
    }

}
