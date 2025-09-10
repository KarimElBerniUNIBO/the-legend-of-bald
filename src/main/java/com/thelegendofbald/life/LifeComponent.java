package com.thelegendofbald.life;

/**
 * A component that encapsulates the health and life-related logic for an entity.
 * This class follows the principle of composition, allowing an entity to have
 * health without inheriting from a health-managing class.
 */
public class LifeComponent {

    private final int maxHealth;
    private int currentHealth;

    /**
     * Constructs a new LifeComponent with a specified maximum health.
     * The current health is initialized to the maximum health.
     *
     * @param maxHealth The maximum health value for the entity.
     */
    public LifeComponent(final int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    /**
     * Retrieves the current health of the entity.
     *
     * @return The current health as an integer.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current health of the entity.
     * This method can be used for direct manipulation of the health value,
     * but it is generally recommended to use damageTaken() or heal() for game logic.
     *
     * @param currentHealth The new health value.
     */
    public void setCurrentHealth(final int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Inflicts damage to the entity, reducing its current health.
     * The health is prevented from dropping below zero.
     *
     * @param damage The amount of damage to be inflicted.
     */
    public void damageTaken(final int damage) {
        this.currentHealth= Math.max(0, this.currentHealth - damage);
    }

    /**
     * Heals the entity by increasing its current health.
     * The health is prevented from exceeding the maximum health value.
     *
     * @param amount The amount of health to restore.
     */
    public void heal(final int amount) {
        if (getCurrentHealth() + amount > this.maxHealth) {
            setCurrentHealth(this.maxHealth);
        } else {
            this.currentHealth = getCurrentHealth() + amount;
        }
    }

    /**
     * Checks if the entity is dead.
     * An entity is considered dead if its current health is less than or equal to zero.
     *
     * @return true if the entity is dead, false otherwise.
     */
    public boolean isDead() {
        return this.currentHealth <= 0;
    }

    /**
     * Calculates the current health as a percentage of the maximum health.
     *
     * @return The health percentage as a double, ranging from 0.0 to 1.0.
     */
    public double getPercentage() {
        return (double) currentHealth / maxHealth;
    }
}
