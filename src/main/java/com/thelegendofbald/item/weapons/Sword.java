package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.LightMeleeWeapon;

public class Sword extends LightMeleeWeapon {

    private static final String NAME = "Sword";
    private static final int DAMAGE = 30;
    private static final int ATTACK_RANGE = 75;

    public Sword(int x, int y, int preferredSizeX, int preferredSizeY, CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
    }

}
