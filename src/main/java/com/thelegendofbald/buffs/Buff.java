package com.thelegendofbald.buffs;

import com.thelegendofbald.characters.Bald; // Importa Bald per i metodi apply/remove

/**
 * An abstract base class for all temporary buffs and debuffs in the game.
 * It provides common functionality for managing duration and active state.
 * Subclasses must implement specific effects when applied or removed.
 */
public abstract class Buff {
    
    private final String name;
    private final long durationMs; // Duration in milliseconds
    private long startTime;
    private boolean isActive;

    /**
     * Constructs a new Buff.
     *
     * @param name The name of the buff (e.g., "Strength Boost", "Poison").
     * @param durationMs The total duration of the buff in milliseconds.
     */
    public Buff(String name, long durationMs) {
        this.name = name;
        this.durationMs = durationMs;
        this.isActive = false;
    }

    /**
     * Activates the buff, setting its start time and active state.
     * This method is called when the buff is initially applied to the player.
     */
    public void activate() {
        this.startTime = System.currentTimeMillis();
        this.isActive = true;
    }

    /**
     * Deactivates the buff, marking it as inactive.
     * This might be called manually before its duration expires.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Checks if the buff has expired.
     * A buff is expired if it's not active or its duration has run out.
     *
     * @return true if the buff has expired, false otherwise.
     */
    public boolean isExpired() {
        if (!isActive) {
            return true;
        }
        return System.currentTimeMillis() - startTime >= durationMs;
    }

    /**
     * Retrieves the name of the buff.
     * @return The name of the buff.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the buff is currently active.
     * @return true if the buff is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Calculates the remaining time of the buff in seconds.
     * @return The remaining time in seconds, or 0 if inactive/expired.
     */
    public long getRemainingTime() {
        if (!isActive) {
            return 0;
        }
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, (durationMs - elapsed) / 1000); // Remaining time in seconds
    }

    /**
     * This method is called once when the buff is initially applied to the player.
     * Subclasses must implement the specific effect applied when the buff starts.
     * @param player The Bald player instance to apply the effect to.
     */
    public abstract void apply(Bald player);

    /**
     * This method is called once when the buff is removed from the player (e.g., when it expires).
     * Subclasses must implement the logic to reverse the effect of the buff.
     * @param player The Bald player instance to remove the effect from.
     */
    public abstract void remove(Bald player);

    /**
     * Updates the buff's internal state. This method is called every game tick
     * for active buffs. Subclasses can override this to implement periodic effects
     * (e.g., damage over time for poison).
     */
    public void update() {
        // Default implementation does nothing. Subclasses can override for periodic effects.
    }
}