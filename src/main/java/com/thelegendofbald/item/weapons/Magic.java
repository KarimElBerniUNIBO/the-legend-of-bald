package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.RangedWeapon;

public class Magic extends RangedWeapon {

    private static final String NAME = "Magic";
    private static final int DAMAGE = 10;
    private static final int ATTACK_COOLDOWN = 300; // milliseconds

    public Magic(int x, int y, int preferredSizeX, int preferredSizeY, CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, ATTACK_COOLDOWN, combatManager);
    }

}
