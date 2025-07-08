package com.thelegendofbald.model.weapons;

import java.util.List;

import com.thelegendofbald.combat.Combatant;

public abstract class RangedWeapon extends Weapon {

    private static final int ATTACK_COOLDOWN = 650; // milliseconds

    protected RangedWeapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, ATTACK_COOLDOWN);
    }

    @Override
    public void performAttack(Combatant attacker, List<? extends Combatant> targets) {
        // TODO Auto-generated method stub
        
    }

}
