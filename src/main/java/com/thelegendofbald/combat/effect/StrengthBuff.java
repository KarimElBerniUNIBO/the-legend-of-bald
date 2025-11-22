package com.thelegendofbald.combat.effect;

import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * Class {@code StrengthBuff} represents a buff that increases the player's attack strength.
 * It extends the {@link StatusEffect} class and provides specific functionality for applying
 * and removing the strength buff, as well as modifying the player's attack power.
 */
public class StrengthBuff extends StatusEffect {
    private final int bonusAmount;

    /**
     * Constructs a StrengthBuff with the specified duration and bonus amount.
     *
     * @param durationMs the duration of the buff in milliseconds
     * @param bonusAmount the amount of attack power to add
     */
    public StrengthBuff(final long durationMs, final int bonusAmount) {
        super("Strength Buff", durationMs);
        this.bonusAmount = bonusAmount;
    }

    /**
     * Returns the amount of attack power added by this buff.
     *
     * @return the bonus amount
     */
    public int getBonusAmount() {
        return bonusAmount;
    }

    /**
     * Applies the buff to the player.
     * 
     * @param player the Bald character to which the buff is applied
     */
    @Override
    public void apply(final Bald player) {
        LoggerUtils.info(getName() + " applied buff");
    }

    /**
     * Removes the buff from the player.
     *
     * @param player the Bald character from which the buff is removed
     */
    @Override
    public void remove(final Bald player) {
        LoggerUtils.info(getName() + " removed");
    }

    /**
     * Modifies the attack power of the player by adding the bonus amount.
     *
     * @param player the Bald character whose attack power is modified
     * @param basePower the base attack power before applying the buff
     * @return the modified attack power after applying the buff
     */
    @Override
    public int modifyAttackPower(final Bald player, final int basePower) {
        return basePower + bonusAmount;
    }

}
