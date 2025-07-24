package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.HeavyMeleeWeapon;

public class Axe extends HeavyMeleeWeapon {

    private static final String NAME = "Axe";
    private static final int DAMAGE = 50;
    private static final int ATTACK_RANGE = 60;

    public Axe(int x, int y, int preferredSizeX, int preferredSizeY, CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
    }

}
