package com.thelegendofbald.item.potions;

import com.thelegendofbald.buffs.StrengthBuff;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.item.UsableItem;

/**
 * A potion that temporarily increases the player's attack strength.
 * This class demonstrates a good practice by using a {@link StrengthBuff}
 * to apply its effect, showcasing a clean separation of concerns.
 */
public class StrengthPotion extends Potion implements UsableItem { 

    private static final int STRENGTH_BONUS = 50; 
    private static final long DURATION_MS = 10000; // 10 seconds

    /**
     * Constructs a new StrengthPotion instance.
     *
     * @param x The x-coordinate of the potion.
     * @param y The y-coordinate of the potion.
     */
    public StrengthPotion(int x, int y) {
        super(x, y, 32, 32, "Strength Potion");
        setPrice(50);
        setDescription("Increases attack strength by " + STRENGTH_BONUS + " for " + (DURATION_MS / 1000) + " seconds.");
        loadImage("/images/potions/strength_potion.png");
    }

    /**
     * Applies the strength buff to the player.
     * It creates a new {@link StrengthBuff} and delegates the management
     * of the buff to the player's character class.
     *
     * @param bald The player instance to apply the buff to.
     */
    @Override
    public void applyEffect(Bald bald) {
        StrengthBuff buff = new StrengthBuff(DURATION_MS, STRENGTH_BONUS);
        bald.applyBuff(buff);
        System.out.println("You used a Strength Potion! Attack strength increased for " + (DURATION_MS / 1000) + " seconds.");
        System.out.println("Current attack power: " + bald.getAttackPower());
    }
}