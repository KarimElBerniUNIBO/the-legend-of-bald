package com.thelegendofbald.effects;

import java.util.ArrayList;
import java.util.List;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.utils.LoggerUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Class {@code StatusEffectManager} manages the application and removal of status effects (effects/deeffects)
 * for a Bald character.
 * It allows adding new effects, updating their effects each game tick,
 * and modifying the character's attributes based on active effects.
 * It also provides methods to remove effects by name or clear all effects.
 * This class encapsulates the logic for managing status effects,
 * ensuring that effects are applied, updated, and removed correctly
 * while maintaining a clean separation of concerns.
 */
public class StatusEffectManager {
    private final Bald player;
    private final List<StatusEffect> activeEffects = new ArrayList<>();

    /**
     * Constructs a StatusEffectManager for the specified Bald character.
     *
     * @param player the Bald {@code Bald} character that owns this manager
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "Intentional architecture: StatusEffectManager tightly coupled to work on Bald"
    )
    public StatusEffectManager(final Bald player) {
        if (player == null) {
            throw new IllegalArgumentException("player Bald cannot be null");
        }
        this.player = player;
    }

    /**
     * Applies a new effect to the Bald character.
     * This method first checks if the effect is already active,
     * removes it if it is, and then activates the new effect.
     * It also applies the effect to the character
     * and adds it to the list of active effects.
     * 
     * @param effect the StatusEffect {@codec StatusEffetc} effetc to apply
     */
    public void applyEffect(final StatusEffect effect) {
        activeEffects.removeIf(b -> b.getName().equals(effect.getName()));
        effect.activate();
        activeEffects.add(effect);
        effect.apply(player); 
        LoggerUtils.info("effect di " + effect.getName() + " attivato!");
    }

    /**
     * Updates all active effects for the Bald character.
     * This method iterates through the list of active effects,
     * calls their onTick and update methods, and checks for expiration.
     * If an effect is expired, it is removed from the character
     * and deactivated.
     */
    public void update() {
        final List<StatusEffect> expired = new ArrayList<>();
        for (final StatusEffect effect : activeEffects) {
            effect.update(player);
            if (effect.isExpired()) {
                effect.remove(player);
                expired.add(effect);
            }
        }
        if (!expired.isEmpty()) {
            activeEffects.removeAll(expired);
        }
    }

    /**
     * Modifies the attack power of the Bald character based on active effects.
     * This method iterates through all active effects and applies their modifications
     * to the base attack power.
     *
     * @param basePower the base attack power before applying any effects
     * @return the modified attack power after applying all active effects
     */
    public int modifyAttackPower(final int basePower) {
        int power = basePower;
        for (final StatusEffect effect : activeEffects) {
            power = effect.modifyAttackPower(player, power);
        }
        return power;
    }

    /**
     * Returns a list of all active effects currently applied to the Bald character.
     *
     * @return an unmodifiable list of active StatusEffect objects
     */
    public List<StatusEffect> getactiveEffects() {
        return List.copyOf(activeEffects);
    }

}
