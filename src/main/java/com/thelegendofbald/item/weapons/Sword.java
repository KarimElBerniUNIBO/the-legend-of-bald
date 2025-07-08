package com.thelegendofbald.item.weapons;

import com.thelegendofbald.model.weapons.LightMeleeWeapon;

public class Sword extends LightMeleeWeapon {

    private static final String NAME = "Sword";
    private static final int DAMAGE = 10;
    private static final int ATTACK_RANGE = 50;

    public Sword(int x, int y, int preferredSizeX, int preferredSizeY) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, ATTACK_RANGE);
    }

}
