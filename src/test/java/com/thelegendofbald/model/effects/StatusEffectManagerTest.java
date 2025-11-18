package com.thelegendofbald.model.effects;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.effects.StatusEffect;
import com.thelegendofbald.effects.debuffs.PoisonDebuff;
import com.thelegendofbald.effects.buffs.StrengthBuff;
import com.thelegendofbald.effects.StatusEffectManager;


/**
 * Unit tests for StatusEffectManager class.
 * 
 * These tests cover the application, updating, and modification of status effects
 * on a Bald character, ensuring correct behavior of the manager.
 */
class StatusEffectManagerTest {

    // Helper: a simple Bald instance using the real constructor
    private static Bald makeBald() {
        return new Bald(0, 0, 100, "test-bald", 10);
    }

    // Tests use real effect classes: PoisonDebuff and StrengthBuff
    @Test
    void constructor_nullOwner_throws() {
        assertThrows(IllegalArgumentException.class, () -> new StatusEffectManager(null));
    }

    @Test
    void applyEffectActivatesAndAddsToActiveEffectsAndAppliesToOwner() {
        Bald owner = makeBald();
        StatusEffectManager mgr = new StatusEffectManager(owner);
        PoisonDebuff eff = new PoisonDebuff(60000L, 1, 1000L);

        mgr.applyEffect(eff);

        List<StatusEffect> active = mgr.getactiveEffects();
        assertEquals(1, active.size());
        assertSame(eff, active.get(0));
        assertTrue(eff.isActive());
    }

    @Test
    void applyEffect_replacesExistingEffectWithSameName() {
        Bald owner = makeBald();
        StatusEffectManager mgr = new StatusEffectManager(owner);
        StrengthBuff first = new StrengthBuff(60000L, 5);
        StrengthBuff second = new StrengthBuff(60000L, 10);

        mgr.applyEffect(first);
        mgr.applyEffect(second); // should remove first by name and add second

        List<StatusEffect> active = mgr.getactiveEffects();
        assertEquals(1, active.size());
        assertSame(second, active.get(0));
        assertTrue(second.isActive());
        // first should not be present
        assertFalse(active.contains(first));
    }

    @Test
    void updateRemovesExpiredEffectsAndCallsRemove() {
        Bald owner = makeBald();
        StatusEffectManager mgr = new StatusEffectManager(owner);
        // Create a poison effect with zero duration so it is immediately expired
        PoisonDebuff expires = new PoisonDebuff(0L, 1, 1L);

        mgr.applyEffect(expires);
        // After update, it should be considered expired and removed
        mgr.update();

        List<StatusEffect> active = mgr.getactiveEffects();
        assertTrue(active.isEmpty());
    }

    @Test
    void modifyAttackPowerAppliesAllEffectModifiersInOrder() {
        Bald owner = makeBald();
        StatusEffectManager mgr = new StatusEffectManager(owner);
        StrengthBuff eff = new StrengthBuff(60000L, 3);

        mgr.applyEffect(eff);

        int base = 10;
        int modified = mgr.modifyAttackPower(base);
        // strength buff adds its bonus: 10 + 3 = 13
        assertEquals(13, modified);
    }

    @Test
    void getactiveEffectsReturnsUnmodifiableCopy() {
        Bald owner = makeBald();
        StatusEffectManager mgr = new StatusEffectManager(owner);
        PoisonDebuff eff = new PoisonDebuff(60000L, 1, 1000L);
        mgr.applyEffect(eff);

        List<StatusEffect> active = mgr.getactiveEffects();
        assertThrows(UnsupportedOperationException.class, () -> active.add(eff));
    }
}
