package com.thelegendofbald.combat;

import java.awt.Rectangle;

/**
 * Represents an entity that can participate in combat.
 *
 * <p>Implementations provide an attack power, can receive damage, expose collision
 * bounds, and report whether they are still alive.</p>
 *
 */
public interface Combatant {
    /**
     * Returns this combatant's attack power.
     *
     * @return the attack power (typically non-negative)
     */
    int getAttackPower();

    /**
     * Applies damage to this combatant.
     *
     * <p>Implementations decide how damage affects internal state (for example,
     * by reducing hit points). Negative values could be treated as healing or ignored;
     * such behavior should be specified by the implementation.</p>
     *
     * @param damage amount of damage to apply
     */
    void takeDamage(int damage);

    /**
     * Returns the rectangle used for collision detection and positioning.
     *
     * @return a {@link Rectangle} describing the combatant's bounds; implementations
     *         should prefer returning a non-null value
     */
    Rectangle getBounds();

    /**
     * Indicates whether the combatant is still alive and able to act.
     *
     * @return {@code true} if the combatant is alive (e.g. hit points &gt; 0), {@code false} otherwise
     */
    boolean isAlive();

}
