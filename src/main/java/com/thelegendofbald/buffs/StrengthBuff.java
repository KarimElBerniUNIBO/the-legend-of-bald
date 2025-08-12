package com.thelegendofbald.buffs;

import com.thelegendofbald.characters.Bald; // Importa Bald

/**
 * A buff that temporarily increases the player's attack strength.
 * This buff contributes to the total attack power calculation in the Bald class.
 */
public class StrengthBuff extends Buff {
    
    private final int bonusAmount;

    /**
     * Constructs a new StrengthBuff.
     *
     * @param durationMs The duration of the buff in milliseconds.
     * @param bonusAmount The amount of attack power to add while this buff is active.
     */
    public StrengthBuff(long durationMs, int bonusAmount) {
        super("Strength Buff", durationMs); // Passa il nome e la durata in ms alla superclasse
        this.bonusAmount = bonusAmount;
    }

    /**
     * Retrieves the bonus attack amount provided by this buff.
     * @return The bonus attack amount.
     */
    public int getBonusAmount() {
        return bonusAmount;
    }

    /**
     * Applies the strength buff effect to the player.
     * For StrengthBuff, the actual bonus is applied in Bald's getAttackPower() method,
     * so this method primarily serves for initial setup or logging.
     * @param player The Bald player instance.
     */
    @Override
    public void apply(Bald player) {
        // Bald's getAttackPower() will automatically account for this buff.
        System.out.println(getName() + " applied buff");
        System.out.println("Bonus attack power: " + player.getAttackPower());
    }

    /**
     * Removes the strength buff effect from the player.
     * The removal of the bonus is implicit when the buff expires and is removed
     * from Bald's active buffs list.
     * @param player The Bald player instance.
     */
    @Override
    public void remove(Bald player) {
        System.out.println(getName() + " removed ");
    }

    /**
     * Updates the buff's internal state.
     * No specific periodic effects for StrengthBuff, so it just calls super.update().
     */
    @Override
    public void update() {
        super.update(); // Gestisce la durata
    }
}