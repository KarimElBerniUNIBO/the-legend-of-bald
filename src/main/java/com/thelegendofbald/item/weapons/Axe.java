package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.weapons.HeavyMeleeWeapon;

public class Axe extends HeavyMeleeWeapon {

    private static final String NAME = "Axe";
    private static final int DAMAGE = 25;
    private static final int ATTACK_RANGE = 65;

    public Axe(int x, int y, int preferredSizeX, int preferredSizeY) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, ATTACK_RANGE);
    }

}
