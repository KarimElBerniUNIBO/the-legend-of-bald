package com.thelegendofbald.buffs;

public class StrengthBuff extends Buff {
    
    private final int bonusAmount;

    public StrengthBuff(long duration, int bonusAmount) {
        super("Forza", duration);
        this.bonusAmount = bonusAmount;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }
}