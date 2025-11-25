package com.thelegendofbald.model.item.weapons;

import com.thelegendofbald.model.system.CombatManager;

/**
 * Represents a light melee weapon in the game.
 * Light melee weapons are typically fast and agile, allowing for quick attacks.
 */
public abstract class LightMeleeWeapon extends MeleeWeapon {

    private static final int ATTACK_COOLDOWN = 650; 

    /**
     * Constructs a LightMeleeWeapon with the specified parameters.
     *
     * @param x                The x-coordinate of the weapon's position.
     * @param y                The y-coordinate of the weapon's position.
     * @param preferredSizeX   The preferred width of the weapon's sprite.
     * @param preferredSizeY   The preferred height of the weapon's sprite.
     * @param name             The name of the weapon.
     * @param damage           The damage dealt by the weapon.
     * @param combatManager    The combat manager handling combat interactions.
     * @param attackRange      The range at which the weapon can attack.
     */
    protected LightMeleeWeapon(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
            final String name, final int damage, final CombatManager combatManager, final int attackRange) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, ATTACK_COOLDOWN, combatManager, attackRange);
    }

}
