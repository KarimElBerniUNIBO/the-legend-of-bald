package com.thelegendofbald.model.characters;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EntityTest {

    private static final int X0 = 0;
    private static final int Y0 = 0;

    private static final int HEALTH_50 = 50;
    private static final int ATTACK_7 = 7;

    private static final int HEALTH_20 = 20;
    private static final int ATTACK_5 = 5;

    private static final int DAMAGE_15 = 15;
    private static final int DAMAGE_10 = 10;

    private static final int HEALTH_30 = 30;

    private static final int BALD_X_100 = 100;
    private static final int BALD_Y_100 = 100;
    private static final int BALD_HEALTH_100 = 100;
    private static final int BALD_ATTACK_10 = 10;

    private static final int START_X_10 = 10;
    private static final int START_Y_10 = 10;

    private static final int TILE_SIZE_32 = 32;
    private static final int TILE_ID_1 = 1;
    private static final int TILE_WIDTH_32 = 32;
    private static final int TILE_HEIGHT_32 = 32;

    private static final String ENEMY_NAME = "Goblin";
    private static final String HERO_NAME = "Hero";

    @Test
    void attackPowerIsValuePassedInConstructor() {
        final DummyEnemy enemy = new DummyEnemy(X0, Y0, HEALTH_50, ENEMY_NAME, ATTACK_7, new EmptyTileMap());
        assertEquals(ATTACK_7, enemy.getAttackPower());
    }

    @Test
    void takeDamageReducesHealthAndIsAliveReflectsIt() {
        final DummyEnemy enemy = new DummyEnemy(X0, Y0, HEALTH_20, ENEMY_NAME, ATTACK_5, new EmptyTileMap());
        assertTrue(enemy.isAlive());

        enemy.takeDamage(DAMAGE_15);
        assertTrue(enemy.isAlive(), "Enemy should still be alive after partial damage");

        enemy.takeDamage(DAMAGE_10); 
        assertFalse(enemy.isAlive(), "Enemy should be dead after lethal damage");
    }

    @Test
    void followPlayerMovesTowardBaldUnlessBlocked() {

        final DummyEnemy enemyFree = new DummyEnemy(X0, Y0, HEALTH_30, ENEMY_NAME, ATTACK_5, new EmptyTileMap());
        final Bald bald = new Bald(BALD_X_100, BALD_Y_100, BALD_HEALTH_100, HERO_NAME, BALD_ATTACK_10);

        enemyFree.followPlayer(bald);
        assertTrue(enemyFree.getX() > X0 && enemyFree.getY() > Y0,
                "Enemy should have moved closer on free map");

        final DummyEnemy enemyBlocked = new DummyEnemy(
            START_X_10,
            START_Y_10,
            HEALTH_30,
            ENEMY_NAME,
            ATTACK_5,
            new SolidTileMap()
        );

        enemyBlocked.followPlayer(bald);
        assertEquals(START_X_10, enemyBlocked.getX());
        assertEquals(START_Y_10, enemyBlocked.getY());
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
                true,
                false,
                false,
                true,
                null
            );
        }
    }
}
