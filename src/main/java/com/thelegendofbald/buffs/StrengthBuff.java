package com.thelegendofbald.buffs;

import com.thelegendofbald.characters.Bald;

public class StrengthBuff extends Buff {
    private final int bonusAmount;

    public StrengthBuff(long durationMs, int bonusAmount) {
        super("Strength Buff", durationMs);
        this.bonusAmount = bonusAmount;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    @Override
    public void apply(Bald player) {
        System.out.println(getName() + " applied buff");
    }

    @Override
    public void remove(Bald player) {
        System.out.println(getName() + " removed");
    }

    @Override
    public int modifyAttackPower(Bald player, int basePower) {
        return basePower + bonusAmount;
    }

    @Override
    public void update() {
        super.update();
    }
}
