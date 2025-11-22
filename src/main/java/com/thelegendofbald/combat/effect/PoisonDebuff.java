package com.thelegendofbald.combat.effect;

import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * Class {@code PoisonBuff} represents a poison effect that can be applied to a Bald {@code Bald} character.
 * It deals damage over time at specified intervals until the buff expires.
 */
public class PoisonDebuff extends StatusEffect {
    private final int damagePerTick;
    private final long tickIntervalMs;
    private long lastTickTime;

    /**
     * Constructs a PoisonBuff {@code poisonBuff} with the specified duration, damage per tick, and tick interval.
     * @param durationMs       the duration of the poison effect in milliseconds
     * @param damagePerTick    the amount of damage dealt per tick
     * @param tickIntervalMs   the interval between each tick in milliseconds
     */
    public PoisonDebuff(final long durationMs, final int damagePerTick, final long tickIntervalMs) {
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
    public void apply(final Bald player) {
        LoggerUtils.info("Poison applied to player , taking " 
        + damagePerTick 
        + " damage every " 
        + (tickIntervalMs / 1000) + " seconds.");
    }

    /**
     * Removes the poison effect from the player.
     * This method is called when the buff is removed from the player.
     * @param player the Bald {@code Bald} character from which the poison effect is removed
     */
    @Override
    public void remove(final Bald player) {
        LoggerUtils.info("Poison effect worn off! Life Remaining: " + player.getLifeComponent().getCurrentHealth());
    }

    /**
     * Handles the tick logic for the poison effect on the player.
     * 
     * @param player the Bald {@code Bald} character to which the poison effect is applied
     */
    @Override
    public void onTick(final Bald player) {
        player.getLifeComponent().damageTaken(damagePerTick);
    }

    /**
     * Updates the poison effect on the player.
     * This method is called periodically to check if it's time to apply damage.
     * 
     * @param player the Bald {@code Bald} character to which the poison effect is applied
     */
    @Override
    public void update(final Bald player) {
        final long now = System.currentTimeMillis();
        if (now - lastTickTime >= tickIntervalMs) {
            onTick(player);
            lastTickTime = now;
        }
    }
}
