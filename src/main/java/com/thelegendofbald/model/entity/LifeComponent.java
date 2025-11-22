package com.thelegendofbald.model.entity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A component that encapsulates the health and life-related logic for an entity.
 * This class follows the principle of composition, allowing an entity to have
 * health without inheriting from a health-managing class.
 * * ORA implementa il PropertyChangeSupport per notificare le viste.
 */
public class LifeComponent {

    /**
     * Name of the property.
     */
    public static final String HEALTH_PROPERTY = "currentHealth";

    private final int maxHealth;
    private int currentHealth;

    private final PropertyChangeSupport support;


    /**
     * Constructs a new LifeComponent with a specified maximum health.
     *
     * @param maxHealth The maximum health value for the entity.
     */
    public LifeComponent(final int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Copy constructor for LifeComponent.
     *
     * @param other The LifeComponent instance to copy from.
     */
    public LifeComponent(final LifeComponent other) {
        this.maxHealth = other.maxHealth;
        this.currentHealth = other.currentHealth;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Add a listener to the support property.
     * 
     * @param pcl the property change listener to add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Remove a listener to the support property.
     * 
     * @param pcl the property change listener to remove.
     */
    public void removePropertyChangeListener(final PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /**
     * Returns a life component with no life.
     * 
     * @return a new {@code LifeComponent}.
     */
    public static LifeComponent noLife() {
        final LifeComponent lc = new LifeComponent(1);
        lc.setCurrentHealth(0);
        return lc;
    }

    /**
     * Return the current health of the life component.
     * 
     * @return health as int.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current health of the entity.
     * Notifica i listener del cambiamento.
     *
     * @param newHealth The new health value.
     */
    public void setCurrentHealth(final int newHealth) {
        final int oldHealth = this.currentHealth;
        this.currentHealth = Math.max(0, Math.min(newHealth, this.maxHealth));
        support.firePropertyChange(HEALTH_PROPERTY, oldHealth, this.currentHealth);
    }

    /**
     * Inflicts damage to the entity, reducing its current health.
     * Notifica i listener del cambiamento.
     *
     * @param damage The amount of damage to be inflicted.
     */
    public void damageTaken(final int damage) {
        final int oldHealth = this.currentHealth;
        this.currentHealth = Math.max(0, this.currentHealth - damage);

        // "Spara" l'evento di notifica
        support.firePropertyChange(HEALTH_PROPERTY, oldHealth, this.currentHealth);
    }

    /**
     * Heals the entity by increasing its current health.
     * Notifica i listener del cambiamento.
     *
     * @param amount The amount of health to restore.
     */
    public void heal(final int amount) {
        final int oldHealth = this.currentHealth;

        if (getCurrentHealth() + amount > this.maxHealth) {
            this.currentHealth = this.maxHealth;
        } else {
            this.currentHealth = getCurrentHealth() + amount;
        }

        if (oldHealth != this.currentHealth) {
            support.firePropertyChange(HEALTH_PROPERTY, oldHealth, this.currentHealth);
        }
    }

    /**
     * Check if the life component has no more life.
     * 
     * @return a boolean if life is under or equals 0.
     */
    public boolean isDead() {
        return this.currentHealth <= 0;
    }

    /**
     * Get the life percentage.
     * 
     * @return a percentage as double of the life between 0 and 1.
     */
    public double getPercentage() {
        return (double) currentHealth / maxHealth;
    }
}
