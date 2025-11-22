package com.thelegendofbald.model.characters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.entity.FinalBoss;
import com.thelegendofbald.model.entity.LifeComponent;
import com.thelegendofbald.view.render.Tile;
import com.thelegendofbald.view.render.TileMap;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link FinalBoss} entity.
 * Verifies phase transitions, health management, and basic movement AI.
 */
class FinalBossTest {

    private static final int X0 = 0;
    private static final int Y0 = 0;

    private static final int MAX_HEALTH = 1000;
    private static final int HEALTH_PHASE_2 = 660;
    private static final int HEALTH_PHASE_3 = 330;

    private static final int BASE_ATTACK_100 = 100;
    private static final double PHASE1_ATK_MULT = 1.00;
    private static final double PHASE2_ATK_MULT = 1.25;
    private static final double PHASE3_ATK_MULT = 1.50;

    private static final int TILE_SIZE_32 = 32;
    private static final int TILE_ID_1 = 1;
    private static final int TILE_WIDTH_32 = 32;
    private static final int TILE_HEIGHT_32 = 32;

    private static final int DAMAGE_10 = 10;

    // Constant for string literals to avoid duplication
    private static final String BOSS_NAME = "Boss Test";

    // Constants for damage tests
    private static final int TEST_INITIAL_HP = 50;
    private static final int DAMAGE_PARTIAL = 20;
    private static final int EXPECTED_HP_AFTER_PARTIAL = 30;
    private static final int DAMAGE_LETHAL = 40;
    private static final int DAMAGE_OVERKILL = 100;

    // Constants for movement tests
    private static final int START_XY_50 = 50;
    private static final int BALD_XY_FAR = 200;
    private static final int BALD_X_AGGRO = 240;
    private static final int BALD_Y_AGGRO = 50;
    private static final int BALD_HP_100 = 100;
    private static final int BALD_ATK_10 = 10;

    /**
     * Tests that the boss is initialized with correct health, max health,
     * and starts in Phase 1.
     */
    @Test
    void initialHealthAndPhaseAreCorrect() {
        final FinalBoss boss = new FinalBoss(X0, Y0, BOSS_NAME, MAX_HEALTH, BASE_ATTACK_100,
                new DummyLifeComponent(MAX_HEALTH), new EmptyTileMap());

        assertEquals(MAX_HEALTH, boss.getHealth());
        assertEquals(MAX_HEALTH, boss.getMaxHealth());
        assertTrue(boss.isAlive());
        assertEquals(1, boss.getPhase(), "Initial phase should be 1");
        assertNotNull(boss.getLifeComponent(), "LifeComponent should not be null");
    }

    /**
     * Tests the transition between phases (1, 2, 3) based on health thresholds
     * and verifies the corresponding attack power scaling.
     */
    @Test
    void phaseAndAttackChangeCorrectlyBasedOnHealth() {
        final LifeComponent life = new DummyLifeComponent(MAX_HEALTH);
        final FinalBoss boss = new FinalBoss(X0, Y0, BOSS_NAME, MAX_HEALTH, BASE_ATTACK_100,
                life, new EmptyTileMap());

        assertEquals(1, boss.getPhase());
        assertEquals((int) (BASE_ATTACK_100 * PHASE1_ATK_MULT),
                boss.getAttackPower(), "Phase 1 Attack Power");

        boss.takeDamage(MAX_HEALTH - HEALTH_PHASE_2);
        assertEquals(HEALTH_PHASE_2, boss.getHealth());
        assertEquals(2, boss.getPhase(), "Should be Phase 2 at 66% HP");
        assertEquals((int) (BASE_ATTACK_100 * PHASE2_ATK_MULT),
                boss.getAttackPower(), "Phase 2 Attack Power");

        boss.takeDamage(HEALTH_PHASE_2 - HEALTH_PHASE_3);
        assertEquals(HEALTH_PHASE_3, boss.getHealth());
        assertEquals(3, boss.getPhase(), "Should be Phase 3 at 33% HP");
        assertEquals((int) (BASE_ATTACK_100 * PHASE3_ATK_MULT),
                boss.getAttackPower(), "Phase 3 Attack Power");

        boss.takeDamage(HEALTH_PHASE_3 + DAMAGE_10);
        assertEquals(0, boss.getHealth());
        assertFalse(boss.isAlive(), "Boss should be dead");
        assertEquals(3, boss.getPhase(), "Phase should remain 3 when dead");
    }

    /**
     * Tests that taking damage correctly reduces health and sets the 'alive'
     * status to false when health reaches zero.
     */
    @Test
    void takeDamageReducesHealthAndKillsBoss() {
        final LifeComponent life = new DummyLifeComponent(TEST_INITIAL_HP);
        final FinalBoss boss = new FinalBoss(X0, Y0, BOSS_NAME, TEST_INITIAL_HP, BASE_ATTACK_100,
                life, new EmptyTileMap());

        assertTrue(boss.isAlive());

        boss.takeDamage(DAMAGE_PARTIAL);
        assertEquals(EXPECTED_HP_AFTER_PARTIAL, boss.getHealth());
        assertTrue(boss.isAlive(), "Boss should still be alive after partial damage");

        boss.takeDamage(DAMAGE_LETHAL);
        assertEquals(0, boss.getHealth());
        assertFalse(boss.isAlive(), "Boss should be dead after lethal damage");

        boss.takeDamage(DAMAGE_OVERKILL);
        assertEquals(0, boss.getHealth());
        assertFalse(boss.isAlive(), "Boss should remain dead");
    }

    /**
     * Tests the boss's AI movement, ensuring it only moves when the player
     * is in aggro range and stops if blocked by solid tiles.
     */
    @Test
    void followPlayerMovesTowardBaldUnlessBlocked() {
        final Bald baldFar = new Bald(BALD_XY_FAR, BALD_XY_FAR, BALD_HP_100, "Hero", BALD_ATK_10);
        final FinalBoss bossFar = new FinalBoss(START_XY_50, START_XY_50, BOSS_NAME,
                MAX_HEALTH, BASE_ATTACK_100, new DummyLifeComponent(MAX_HEALTH), new EmptyTileMap());

        bossFar.followPlayer(baldFar);
        assertEquals(START_XY_50, bossFar.getX(), "Boss should not move outside of aggro range (X)");
        assertEquals(START_XY_50, bossFar.getY(), "Boss should not move outside of aggro range (Y)");

        final Bald baldNear = new Bald(BALD_X_AGGRO, BALD_Y_AGGRO, BALD_HP_100, "Hero", BALD_ATK_10);
        final FinalBoss bossNearFree = new FinalBoss(START_XY_50, START_XY_50, BOSS_NAME,
                MAX_HEALTH, BASE_ATTACK_100, new LifeComponent(MAX_HEALTH), new EmptyTileMap());

        bossNearFree.followPlayer(baldNear);
        assertTrue(bossNearFree.getX() > START_XY_50, "Boss should move closer on free map (X)");
        assertEquals(START_XY_50, bossNearFree.getY(), "Boss should not change Y position if not needed");

        final FinalBoss bossNearBlocked = new FinalBoss(START_XY_50, START_XY_50, BOSS_NAME,
                MAX_HEALTH, BASE_ATTACK_100, new DummyLifeComponent(MAX_HEALTH), new SolidTileMap());

        bossNearBlocked.followPlayer(baldNear);
        assertEquals(START_XY_50, bossNearBlocked.getX(), "Boss should be blocked (X)");
        assertEquals(START_XY_50, bossNearBlocked.getY(), "Boss should be blocked (Y)");
    }

    // -------------------------------------------------------
    // INNER CLASSES (Must remain at the bottom for Checkstyle)
    // -------------------------------------------------------

    /**
     * Mock {@link LifeComponent} for testing purposes.
     * Simulates health tracking without UI dependencies.
     */
    static class DummyLifeComponent extends LifeComponent {
        private int currentHealth;

        DummyLifeComponent(final int maxHealth) {
            super(maxHealth);
            this.currentHealth = maxHealth;
        }

        @Override
        public int getCurrentHealth() {
            return currentHealth;
        }

        @Override
        public void setCurrentHealth(final int health) {
            this.currentHealth = health;
        }

        /**
         * Simulates taking damage by reducing current health.
         *
         * @param damage the amount of damage to take.
         */
        public void takeDamage(final int damage) {
            if (damage <= 0) {
                return;
            }
            this.currentHealth = Math.max(0, this.currentHealth - damage);
        }
    }

    /** Fake TileMap: no solid tiles, fixed tile size. */
    static class EmptyTileMap extends TileMap {
        EmptyTileMap() {
            super(X0, Y0, TILE_SIZE_32);
        }

        @Override
        public Tile getTileAt(final int x, final int y) {
            return null;
        }
    }

    /** Fake TileMap: every tile is solid. */
    static class SolidTileMap extends TileMap {
        SolidTileMap() {
            super(X0, Y0, TILE_SIZE_32);
        }

        @Override
        public Tile getTileAt(final int x, final int y) {
            return new Tile(
                    null,
                    TILE_WIDTH_32,
                    TILE_HEIGHT_32,
                    TILE_ID_1,
                    true, // Solid
                    false,
                    false,
                    true,
                    null
            );
        }
    }
}
