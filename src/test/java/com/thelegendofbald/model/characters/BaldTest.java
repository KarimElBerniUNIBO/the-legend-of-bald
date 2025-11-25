package com.thelegendofbald.model.characters;

import com.thelegendofbald.model.system.Wallet;
import com.thelegendofbald.model.entity.Bald;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BaldTest {

    private static final int INITIAL_ATTACK_POWER = 10;
    private static final int UPDATED_ATTACK_POWER = 25;
    private static final int INITIAL_HEALTH = 50;
    private static final int DAMAGE_30 = 30;
    private static final int DAMAGE_25 = 25;
    private static final int INITIAL_COINS = 0;
    private static final int ADDED_COINS = 100;

    @Test
    void testAttackPowerModifiedBySetter() {
        final Bald bald = new Bald(0, 0, 100, "Hero", INITIAL_ATTACK_POWER);
        assertEquals(INITIAL_ATTACK_POWER, bald.getAttackPower());

        bald.setAttackPower(UPDATED_ATTACK_POWER);
        assertEquals(UPDATED_ATTACK_POWER, bald.getAttackPower());
    }

    @Test
    void testTakeDamageAndIsAlive() {
        final Bald bald = new Bald(0, 0, INITIAL_HEALTH, "Hero", INITIAL_ATTACK_POWER);
        assertTrue(bald.isAlive());

        bald.takeDamage(DAMAGE_30);
        assertTrue(bald.isAlive());

        bald.takeDamage(DAMAGE_25);
        assertFalse(bald.isAlive());
    }

    @Test
    void testWalletStartsEmptyAndCanAdd() {
        final Wallet wallet = new Wallet(0);

        assertEquals(INITIAL_COINS, wallet.getCoins());

        wallet.addCoins(ADDED_COINS);
        assertEquals(ADDED_COINS, wallet.getCoins());
    }
}
