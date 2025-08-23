package com.thelegendofbald.effects.debuffs;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.effects.StatusEffect;

/**
 * Class {@code PoisonBuff} represents a poison effect that can be applied to a Bald {@code Bald} character.
 * It deals damage over time at specified intervals until the buff expires.
 */
public class PoisonBuff extends StatusEffect {
    private final int damagePerTick;
    private final long tickIntervalMs;
    private long lastTickTime;

    /**
     * Constructs a PoisonBuff {@code poisonBuff} with the specified duration, damage per tick, and tick interval.
     * @param durationMs       the duration of the poison effect in milliseconds
     * @param damagePerTick    the amount of damage dealt per tick
     * @param tickIntervalMs   the interval between each tick in milliseconds
     */
    public PoisonBuff(long durationMs, int damagePerTick, long tickIntervalMs) {
        super("Poison", durationMs);
        this.damagePerTick = damagePerTick;
        this.tickIntervalMs = tickIntervalMs;
        this.lastTickTime = System.currentTimeMillis();
    }

    /**
     * Applies the poison effect to the player.
     * This method is called when the buff is applied to the player.
     * @param player the Bald {@code Bald} character to which the poison effect is applied
     */
    @Override
    public void apply(Bald player) {
        System.out.println("Player is poisoned!");
    }

    /**
     * Removes the poison effect from the player.
     * This method is called when the buff is removed from the player.
     * @param player the Bald {@code Bald} character from which the poison effect is removed
     */
    @Override
    public void remove(Bald player) {
        System.out.println("Poison effect worn off!");
    }

    /**
     * Handles the tick logic for the poison effect on the player.
     * 
     * @param player the Bald {@code Bald} character to which the poison effect is applied
     */
    @Override
    public void onTick(Bald player) {
        player.getLifeComponent().damageTaken(damagePerTick);
    }

    @Override
    public void update(Bald player) {
        long now = System.currentTimeMillis();
        if (now - lastTickTime >= tickIntervalMs) {
            onTick(player);
            lastTickTime = now;
        }
    }
}
