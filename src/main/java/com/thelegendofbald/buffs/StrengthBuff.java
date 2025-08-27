package com.thelegendofbald.buffs;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.utils.LoggerUtils;

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
        LoggerUtils.info(getName() + " applied buff");
    }

    @Override
    public void remove(Bald player) {
        LoggerUtils.info(getName() + " removed");
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
