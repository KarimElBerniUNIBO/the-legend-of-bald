package com.thelegendofbald.buffs;

import com.thelegendofbald.characters.Bald;

/**
 * A buff that inflicts damage over time (DoT) to the player.
 * This buff will periodically reduce the player's health while active.
 */
public class PoisonBuff extends Buff {

    private final int damagePerTick;
    private final long tickIntervalMs; // How often damage is applied
    private long lastTickTime; // When the last damage tick occurred

    /**
     * Constructs a new PoisonBuff.
     *
     * @param durationMs The total duration of the poison effect in milliseconds.
     * @param damagePerTick The amount of damage inflicted each tick.
     * @param tickIntervalMs The interval between damage ticks in milliseconds.
     */
    public PoisonBuff(long durationMs, int damagePerTick, long tickIntervalMs) {
        super("Poison", durationMs); // Pass the name "Poison" and durationMs to the superclass constructor
        this.damagePerTick = damagePerTick;
        this.tickIntervalMs = tickIntervalMs;
        this.lastTickTime = System.currentTimeMillis(); // Initialize last tick time
    }

    /**
     * Applies the initial effect of the buff.
     * For PoisonBuff, this typically involves setting up the initial state
     * and possibly logging that the player is now poisoned.
     * The actual damage is applied periodically in the update method handled by Bald.
     *
     * @param player The Bald player instance to apply the buff to.
     */
    @Override
    public void apply(Bald player) {
        System.out.println("Player is poisoned!");
    }

    /**
     * Removes the poison effect from the player.
     * This method is called when the buff expires or is manually removed.
     *
     * @param player The Bald player instance to remove the buff from.
     */
    @Override
    public void remove(Bald player) {
        System.out.println("Poison effect worn off!");
    }

    /**
     * Updates the poison buff's internal state.
     * This method is called by the game loop (via Bald's updateBuffs).
     * It primarily manages the buff's duration via the superclass update.
     * The periodic damage application is handled by Bald's updateBuffs() method
     * by checking {@link #shouldTick()} and calling {@link #getDamagePerTick()}.
     */
    @Override
    public void update() {
        super.update(); // Update the base buff (duration)
    }

    /**
     * Checks if enough time has passed to apply another tick of poison damage.
     * This method is called by the {@link Bald#updateBuffs()} method.
     *
     * @return true if a tick should occur, false otherwise.
     */
    public boolean shouldTick() {
        return System.currentTimeMillis() - lastTickTime >= tickIntervalMs;
    }

    /**
     * Resets the internal timer for the next damage tick.
     * This method should be called by {@link Bald#updateBuffs()} after a damage tick has been applied.
     */
    public void resetTickTimer() {
        this.lastTickTime = System.currentTimeMillis();
    }

    /**
     * Returns the amount of damage this buff inflicts per tick.
     *
     * @return The damage per tick.
     */
    public int getDamagePerTick() {
        return damagePerTick;
    }
}