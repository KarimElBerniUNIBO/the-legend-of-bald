package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.HeavyMeleeWeapon;

/**
 * The {@code Axe} class represents an axe weapon in the game.
 * It extends the {@code HeavyMeleeWeapon} class and provides specific properties for axe attacks.
 */
public class Axe extends HeavyMeleeWeapon {

    private static final String NAME = "Axe";
    private static final int DAMAGE = 50;
    private static final int ATTACK_RANGE = 60;

    /**
     * Constructs a new {@code Axe} instance with the specified parameters.
     *
     * @param x                the x-coordinate of the weapon
     * @param y                the y-coordinate of the weapon
     * @param preferredSizeX   the preferred width of the weapon
     * @param preferredSizeY   the preferred height of the weapon
     * @param combatManager    the combat manager to handle combat actions
     */
    public Axe(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
            final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
    }

}
