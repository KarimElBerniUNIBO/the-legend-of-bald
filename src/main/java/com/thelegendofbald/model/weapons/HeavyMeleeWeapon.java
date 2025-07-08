package com.thelegendofbald.model.weapons;

public abstract class HeavyMeleeWeapon extends MeleeWeapon {

    private static final int ATTACK_COOLDOWN = 1000; // milliseconds

    protected HeavyMeleeWeapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage,
                               int attackRange) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, ATTACK_COOLDOWN, attackRange);
    }

}
