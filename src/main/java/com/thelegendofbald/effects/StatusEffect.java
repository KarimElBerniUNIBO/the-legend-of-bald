package com.thelegendofbald.effects;

import com.thelegendofbald.characters.Bald;

/**
 * Abstract class {@code status effect} representing a status effect that can be applied to a Bald character.
 * status effects have a name, duration, and can be activated or deactivated.
 * They can modify player attributes and have an expiration mechanism.
 * Every subclass must implement the {@code apply} and {@code remove} methods
 */
public abstract class StatusEffect {
    private final String name;
    private final long durationMs;
    private long startTime;
    private boolean isActive;

    /**
     * Constructs a status effect with the specified name and duration.
     *
     * @param name the name of the status effect
     * @param durationMs the duration of the status effect in milliseconds
     */
    public StatusEffect(final String name, final long durationMs) {
        this.name = name;
        this.durationMs = durationMs;
        this.isActive = false;
    }

    /**
     * Activates the status effect, setting the start time to the current system time.
     */
    public void activate() {
        this.startTime = System.currentTimeMillis();
        this.isActive = true;
    }

    /**
     * Deactivates the status effect, marking it as inactive.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Checks if the status effect is expired based on its duration and start time.
     *
     * @return {@code true} if the status effect is expired, {@code false} otherwise
     */
    public boolean isExpired() {
        return !isActive || System.currentTimeMillis() - startTime >= durationMs;
    }

    /**
     * Returns the name of the status effect.
     *
     * @return the name of the status effect
     */
    public String getName() { 
        return name; 
    }

    /**
     * Check if the status effect is currently active.
     *
     * @return {@code true} if the status effect is active , {@code false} otherwise
     */
    public boolean isActive() { 
        return isActive; 
    }

    /**
     * Returns the remaining time of the status effect in seconds.
     *
     * @return the remaining time in seconds, or 0 if the status effect is not active
     */
    public long getRemainingTime() {
        if (!isActive) {
            return 0;
        }
        final long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, (durationMs - elapsed) / 1000);
    }

    /**
     * Applies the StatusEffect to the specified Bald {@code Bald} character.
     * This method must be implemented by subclasses to define the specific effects of the status effect.
     * 
     * @param player the Bald {@code Bald} character to which the status effect is applied
     */
    public abstract void apply(Bald player);

    /**
     * Removes the status effect from the specified Bald player.
     * This method must be implemented by subclasses to define the specific effects of removing the status effect.
     * 
     * @param player the Bald {@code Bald} character from which the status effect is removed
     */
    public abstract void remove(final Bald player);

    /**
     * Updates the status effect on the specified Bald player.
     * @param player the Bald {@code Bald} character whose status effect is updated
     */
    public void update(final Bald player) {
    }

    /**
     * Modifies the attack power of the Bald player based on the status effect.
     * This method can be overridden by subclasses to provide specific modifications.
     * @param player the Bald {@code Bald} character whose attack power is modified
     * @param basePower base attack of the Bald player
     * @return the modified attack power
     */
    public int modifyAttackPower(final Bald player, final int basePower) {
        return basePower;
    }

    /**
     * Execute an action on each tick of the game loop.
     * This method can be overridden by subclasses to define specific actions that should occur each tick
     * such as Damage over Time (DoT) effects , healing over time(HoT) , etc.
     * @param player the Bald {@code Bald} character on which the action is executed
     */
    public void onTick(final Bald player) { }
}
