package com.thelegendofbald.model.weapons;

import com.thelegendofbald.model.combat.CombatManager;

public abstract class LightMeleeWeapon extends MeleeWeapon {

    private static final int ATTACK_COOLDOWN = 650; // milliseconds

    protected LightMeleeWeapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage,
            CombatManager combatManager, int attackRange) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, ATTACK_COOLDOWN, combatManager, attackRange);
    }

}
