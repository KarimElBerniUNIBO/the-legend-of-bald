package com.thelegendofbald.model.characters;

import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.entity.DummyEnemy;
import com.thelegendofbald.view.render.Tile;
import com.thelegendofbald.view.render.TileMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for DummyEnemy behavior.
 */
class DummyEnemyTest {

    private static final int HEALTH_20 = 20;
    private static final int HEALTH_30 = 30;
    private static final int HEALTH_50 = 50;
    private static final int DAMAGE_10 = 10;
    private static final int DAMAGE_15 = 15;
    private static final int ATTACK_5 = 5;
    private static final int ATTACK_7 = 7;

    private static final int START_X_0 = 0;
    private static final int START_Y_0 = 0;
    private static final int START_X_10 = 10;
    private static final int START_Y_10 = 10;

    private static final String ENEMY_NAME = "Goblin";

    @Test
    void attackPowerReturnsValuePassedInConstructor() {
        final DummyEnemy enemy = new DummyEnemy(
                START_X_0, START_Y_0, HEALTH_50, ENEMY_NAME, ATTACK_7, new EmptyTileMap()
        );
        assertEquals(ATTACK_7, enemy.getAttackPower());
    }

    @Test
    void takeDamageReducesHealthAndIsAliveReflectsIt() {
        final DummyEnemy enemy = new DummyEnemy(
                START_X_0, START_Y_0, HEALTH_20, ENEMY_NAME, ATTACK_5, new EmptyTileMap()
        );
        assertTrue(enemy.isAlive());

        enemy.takeDamage(DAMAGE_15);
        assertTrue(enemy.isAlive(), "Enemy should still be alive after partial damage");

        enemy.takeDamage(DAMAGE_10);
        assertFalse(enemy.isAlive(), "Enemy should be dead after lethal damage");
    }

    @Test
    void followPlayerMovesTowardBaldUnlessBlocked() {
        final DummyEnemy enemyFree = new DummyEnemy(
                START_X_0, START_Y_0, HEALTH_30, ENEMY_NAME, ATTACK_5, new EmptyTileMap()
        );
        final Bald bald = new Bald(100, 100, 100, "Hero", 10);

        enemyFree.followPlayer(bald);
        assertTrue(enemyFree.getX() > 0 && enemyFree.getY() > 0,
                "Enemy should have moved closer on free map");

        final DummyEnemy enemyBlocked = new DummyEnemy(
                START_X_10, START_Y_10, HEALTH_30, ENEMY_NAME, ATTACK_5, new SolidTileMap()
        );
        enemyBlocked.followPlayer(bald);
        assertEquals(START_X_10, enemyBlocked.getX());
        assertEquals(START_Y_10, enemyBlocked.getY());
    }

    /** Fake TileMap: no solid tiles, fixed tile size. */
    static class EmptyTileMap extends TileMap {
        EmptyTileMap() {
            super(0, 0, 32);
        }
        @Override
        public Tile getTileAt(final int x, final int y) {
            return null;
        }
    }

    /** Fake TileMap: every tile is solid. */
    static class SolidTileMap extends TileMap {
        SolidTileMap() {
            super(0, 0, 32);
        }
        @Override
        public Tile getTileAt(final int x, final int y) {
            return new Tile(null, 32, 32, 1, true, false, false, true, null);
        }
    }
}
