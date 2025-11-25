package com.thelegendofbald.model.item.potions;

import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.combat.effect.StrengthBuff;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * A potion that temporarily increases the player's attack strength.
 * This class demonstrates a good practice by using a {@link StrengthBuff}
 * to apply its effect, showcasing a clean separation of concerns.
 */
public class StrengthPotion extends Potion  { 

    private static final int STRENGTH_BONUS = 50;
    private static final long DURATION_MS = 10_000;
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private static final int DEFAULT_PRICE = 50;

    /**
     * Constructs a new StrengthPotion instance.
     *
     * @param x The x-coordinate of the potion.
     * @param y The y-coordinate of the potion.
     */
    public StrengthPotion(final int x, final int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "Strength Potion");
        super.setPrice(DEFAULT_PRICE);
        super.setDescription("Increases attack strength by " + STRENGTH_BONUS + " for " + (DURATION_MS / 1000) + " seconds.");
        super.loadImage("/images/potions/strength_potion.png");
    }

    /**
     * Applies the strength buff to the player.
     * It creates a new {@link StrengthBuff} and delegates the management
     * of the buff to the player's character class.
     *
     * @param bald The player instance to apply the buff to.
     */
    @Override
    public void applyEffect(final Bald bald) {
        final StrengthBuff buff = new StrengthBuff(DURATION_MS, STRENGTH_BONUS);
        bald.applyBuff(buff);
        LoggerUtils.info(String.format("Strength potion used! Attack strength increased for %d seconds.", DURATION_MS / 1000));
        LoggerUtils.info("Current attack power: " + bald.getAttackPower());
    }
}
