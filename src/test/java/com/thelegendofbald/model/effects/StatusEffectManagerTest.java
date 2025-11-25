package com.thelegendofbald.model.effects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.combat.effect.StatusEffect;
import com.thelegendofbald.combat.effect.PoisonDebuff;
import com.thelegendofbald.combat.effect.StrengthBuff;
import com.thelegendofbald.combat.effect.StatusEffectManager;


/**
 * Unit tests for StatusEffectManager class.
 * 
 * These tests cover the application, updating, and modification of status effects
 * on a Bald character, ensuring correct behavior of the manager.
 */
class StatusEffectManagerTest {

    private static final int EXPECTED = 13;
    private static final long EFFECT_DURATION = 60_000L;
    private static final long EFFECT_INTERVAL = 1_000L;
    private static final int POISON_DAMAGE = 1;

    private static Bald makeBald() {
        return new Bald(0, 0, 100, "test-bald", 10);
    }

    @Test
    void constructorNullOwnerThrows() {
        assertThrows(IllegalArgumentException.class, () -> new StatusEffectManager(null));
    }

    @Test
    void applyEffectActivatesAndAddsToActiveEffectsAndAppliesToOwner() {
        final Bald owner = makeBald();
        final StatusEffectManager mgr = new StatusEffectManager(owner);
        final PoisonDebuff eff = new PoisonDebuff(EFFECT_DURATION, POISON_DAMAGE, EFFECT_INTERVAL);

        mgr.applyEffect(eff);

        final List<StatusEffect> active = mgr.getactiveEffects();
        assertEquals(1, active.size());
        assertSame(eff, active.get(0));
        assertTrue(eff.isActive());
    }

    @Test
    void applyEffectReplacesExistingEffectWithSameName() {
        final Bald owner = makeBald();
        final StatusEffectManager mgr = new StatusEffectManager(owner);
        final StrengthBuff first = new StrengthBuff(EFFECT_DURATION, 5);
        final StrengthBuff second = new StrengthBuff(EFFECT_DURATION, 10);

        mgr.applyEffect(first);
        mgr.applyEffect(second);

        final List<StatusEffect> active = mgr.getactiveEffects();
        assertEquals(1, active.size());
        assertSame(second, active.get(0));
        assertTrue(second.isActive());
        assertFalse(active.contains(first));
    }

    @Test
    void updateRemovesExpiredEffectsAndCallsRemove() {
        final Bald owner = makeBald();
        final StatusEffectManager mgr = new StatusEffectManager(owner);
        final PoisonDebuff expires = new PoisonDebuff(0L, POISON_DAMAGE, 1L);

        mgr.applyEffect(expires);
        mgr.update();

        final List<StatusEffect> active = mgr.getactiveEffects();
        assertTrue(active.isEmpty());
    }

    @Test
    void modifyAttackPowerAppliesAllEffectModifiersInOrder() {
        final Bald owner = makeBald();
        final StatusEffectManager mgr = new StatusEffectManager(owner);
        final StrengthBuff eff = new StrengthBuff(EFFECT_DURATION, 3);

        mgr.applyEffect(eff);

        final int base = 10;
        final int modified = mgr.modifyAttackPower(base);
        assertEquals(EXPECTED, modified);
    }

    @Test
    void getactiveEffectsReturnsUnmodifiableCopy() {
        final Bald owner = makeBald();
        final StatusEffectManager mgr = new StatusEffectManager(owner);
        final PoisonDebuff eff = new PoisonDebuff(EFFECT_DURATION, POISON_DAMAGE, EFFECT_INTERVAL);
        mgr.applyEffect(eff);

        final List<StatusEffect> active = mgr.getactiveEffects();
        assertThrows(UnsupportedOperationException.class, () -> active.add(eff));
    }
}
