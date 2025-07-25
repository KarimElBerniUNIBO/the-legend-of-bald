package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.RangedWeapon;

/**
 * The {@code Magic} class represents a magic weapon in the game.
 * It extends the {@code RangedWeapon} class and provides specific properties for magic attacks.
 */
public class Magic extends RangedWeapon {

    private static final String NAME = "Magic";
    private static final int DAMAGE = 10;
    private static final int ATTACK_COOLDOWN = 300; // milliseconds

    /**
     * Constructs a new {@code Magic} instance with the specified parameters.
     *
     * @param x                the x-coordinate of the weapon
     * @param y                the y-coordinate of the weapon
     * @param preferredSizeX   the preferred width of the weapon
     * @param preferredSizeY   the preferred height of the weapon
     * @param combatManager    the combat manager to handle combat actions
     */
    public Magic(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
            final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, ATTACK_COOLDOWN, combatManager);
    }

}
