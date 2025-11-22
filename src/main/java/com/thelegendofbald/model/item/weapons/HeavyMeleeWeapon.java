package com.thelegendofbald.model.item.weapons;

import com.thelegendofbald.model.system.CombatManager;

/**
 * Abstract class representing a heavy melee weapon in the game.
 * Heavy melee weapons have a longer attack cooldown compared to lighter weapons.
 */
public abstract class HeavyMeleeWeapon extends MeleeWeapon {

    private static final int ATTACK_COOLDOWN = 1000; // milliseconds

    /**
     * Constructs a HeavyMeleeWeapon with the specified parameters.
     *
     * @param x                The x-coordinate of the weapon's position.
     * @param y                The y-coordinate of the weapon's position.
     * @param preferredSizeX   The preferred width of the weapon's sprite.
     * @param preferredSizeY   The preferred height of the weapon's sprite.
     * @param name             The name of the weapon.
     * @param damage           The damage dealt by the weapon.
     * @param combatManager    The CombatManager instance for handling combat logic.
     * @param attackRange      The range of the weapon's attack.
     */
    protected HeavyMeleeWeapon(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
            final String name, final int damage, final CombatManager combatManager, final int attackRange) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, ATTACK_COOLDOWN, combatManager, attackRange);
    }

}
