package com.thelegendofbald.life;

// --- MODIFICHE (Import aggiunti) ---
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
// --- FINE MODIFICHE ---

/**
 * A component that encapsulates the health and life-related logic for an entity.
 * This class follows the principle of composition, allowing an entity to have
 * health without inheriting from a health-managing class.
 * * ORA implementa il PropertyChangeSupport per notificare le viste.
 */
public class LifeComponent {

    private final int maxHealth;
    private int currentHealth;

    // --- MODIFICA (Aggiunto il gestore degli eventi) ---
    private final PropertyChangeSupport support;
    public static final String HEALTH_PROPERTY = "currentHealth";
    // --- FINE MODIFICA ---


    /**
     * Constructs a new LifeComponent with a specified maximum health.
     *
     * @param maxHealth The maximum health value for the entity.
     */
    public LifeComponent(final int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        // --- MODIFICA (Inizializza il support) ---
        this.support = new PropertyChangeSupport(this);
        // --- FINE MODIFICA ---
    }

    
    public LifeComponent(LifeComponent other) {
        this.maxHealth = other.maxHealth;
        this.currentHealth = other.currentHealth;
        // --- MODIFICA (Inizializza il support) ---
        this.support = new PropertyChangeSupport(this);
        // --- FINE MODIFICA ---
    }

    // --- MODIFICA (Metodi per "iscriversi" agli aggiornamenti) ---
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
    // --- FINE MODIFICA ---


    public static LifeComponent noLife() {
        LifeComponent lc = new LifeComponent(1);
        lc.setCurrentHealth(0);
        return lc; 
    }

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
        // --- MODIFICA (Logica per notificare l'aggiornamento) ---
        int oldHealth = this.currentHealth;
        this.currentHealth = Math.max(0, Math.min(newHealth, this.maxHealth));
        
        // "Spara" l'evento di notifica
        support.firePropertyChange(HEALTH_PROPERTY, oldHealth, this.currentHealth);
        // --- FINE MODIFICA ---
    }

    /**
     * Inflicts damage to the entity, reducing its current health.
     * Notifica i listener del cambiamento.
     *
     * @param damage The amount of damage to be inflicted.
     */
    public void damageTaken(final int damage) {
        // --- MODIFICA (Logica per notificare l'aggiornamento) ---
        int oldHealth = this.currentHealth;
        this.currentHealth = Math.max(0, this.currentHealth - damage);

        // "Spara" l'evento di notifica
        support.firePropertyChange(HEALTH_PROPERTY, oldHealth, this.currentHealth);
        // --- FINE MODIFICA ---
    }

    /**
     * Heals the entity by increasing its current health.
     * Notifica i listener del cambiamento.
     *
     * @param amount The amount of health to restore.
     */
    public void heal(final int amount) {
        // --- MODIFICA (Logica per notificare l'aggiornamento) ---
        int oldHealth = this.currentHealth;
        
        if (getCurrentHealth() + amount > this.maxHealth) {
            this.currentHealth = this.maxHealth;
        } else {
            this.currentHealth = getCurrentHealth() + amount;
        }
        
        // "Spara" l'evento di notifica solo se la vita è cambiata
        if (oldHealth != this.currentHealth) {
            support.firePropertyChange(HEALTH_PROPERTY, oldHealth, this.currentHealth);
        }
        // --- FINE MODIFICA ---
    }

    public boolean isDead() {
        return this.currentHealth <= 0;
    }

    public double getPercentage() {
        return (double) currentHealth / maxHealth;
    }
}