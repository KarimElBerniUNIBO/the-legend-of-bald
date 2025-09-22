package com.thelegendofbald.model.characters;

import com.thelegendofbald.model.common.Wallet;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.life.LifeComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaldTest {

    @Test
    void testAttackPower_ModifiedBySetter() {
        Bald bald = new Bald(0, 0, 100, "Hero", 10);
        assertEquals(10, bald.getAttackPower());

        bald.setAttackPower(25);
        assertEquals(25, bald.getAttackPower());
    }

    @Test
    void testTakeDamageAndIsAlive() {
        Bald bald = new Bald(0, 0, 50, "Hero", 10);
        assertTrue(bald.isAlive());

        bald.takeDamage(30);
        assertTrue(bald.isAlive());

        bald.takeDamage(25); // total > 50
        assertFalse(bald.isAlive());
    }

    @Test
    void testWalletStartsEmptyAndCanAdd() {
        Bald bald = new Bald(0, 0, 100, "Hero", 10);
        Wallet wallet = bald.getWallet();

        assertEquals(0, wallet.getCoins());

        wallet.addCoins(100);
        assertEquals(100, wallet.getCoins());
    }
}
