package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.LightMeleeWeapon;

/**
 * The {@code Sword} class represents a sword weapon in the game.
 * It extends the {@code LightMeleeWeapon} class and provides specific properties for sword attacks.
 */
public class Sword extends LightMeleeWeapon {

    private static final String NAME = "Sword";
    private static final int DAMAGE = 30;
    private static final int ATTACK_RANGE = 75;

    /**
     * Constructs a new {@code Sword} instance with the specified parameters.
     *
     * @param x                the x-coordinate of the weapon
     * @param y                the y-coordinate of the weapon
     * @param preferredSizeX   the preferred width of the weapon
     * @param preferredSizeY   the preferred height of the weapon
     * @param combatManager    the combat manager to handle combat actions
     */
    public Sword(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
            final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
    }

}
