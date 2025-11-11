package com.thelegendofbald.model.combat.projectile;

import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProjectileTest {

    private static final int TILE_SIZE_32 = 32;
    private static final int TILE_WIDTH_32 = 32;
    private static final int TILE_HEIGHT_32 = 32;
    private static final int TILE_ID_1 = 1;
    private static final int TILE_ID_0 = 0;

    private static final int X_10 = 10;
    private static final int Y_20 = 20;
    private static final int DIR_RIGHT = 0;
    private static final int SPEED_5 = 5;
    private static final int DMG_12 = 12;
    private static final int EXPECT_X_15 = 15;

    private static final int START_X_63 = 63;
    private static final int START_Y_0 = 0;
    private static final int SPEED_4 = 4;
    private static final int DMG_5 = 5;
    private static final int SOLID_COL_2 = 2;
    private static final int SOLID_ROW_0 = 0;

    private static final int X_100 = 100;
    private static final int Y_200 = 200;
    private static final int DIR_DOWN = 1;
    private static final int SPEED_3 = 3;
    private static final int DMG_1 = 1;
    private static final int BOUNDS_6 = 6;

    @Test
    void movesRightOnEmptyMapAndStaysActive() {
        final TileMap map = new EmptyMap();
        final Projectile p = new Projectile(X_10, Y_20, DIR_RIGHT, SPEED_5, DMG_12);

        p.move(map);

        assertEquals(EXPECT_X_15, p.getBounds().x);
        assertEquals(Y_20, p.getBounds().y);
        assertTrue(p.isAlive());
        assertEquals(DMG_12, p.getAttackPower());
    }

    @Test
    void deactivatesOnCollisionWithSolidTileCornerCheck() {
        final TileMap map = new SolidAtMap(SOLID_COL_2, SOLID_ROW_0);
        final Projectile p = new Projectile(START_X_63, START_Y_0, DIR_RIGHT, SPEED_4, DMG_5);
        p.move(map);
        assertEquals(START_X_63, p.getBounds().x);
        assertEquals(START_Y_0, p.getBounds().y);
        assertFalse(p.isAlive());
    }

    @Test
    void boundsAre6x6AndFollowPosition() {
        final Projectile p = new Projectile(X_100, Y_200, DIR_DOWN, SPEED_3, DMG_1);
        assertEquals(X_100, p.getBounds().x);
        assertEquals(Y_200, p.getBounds().y);
        assertEquals(BOUNDS_6, p.getBounds().width);
        assertEquals(BOUNDS_6, p.getBounds().height);
    }

    /** Mappa vuota: nessuna tile solida. */
    static class EmptyMap extends TileMap {
        EmptyMap() {
            super(0, 0, TILE_SIZE_32);
        }

        @Override
        public Tile getTileAt(final int x, final int y) {
            return null; 
        }
    }

    /** Mappa con una tile solida in (solidX, solidY). */
    static class SolidAtMap extends TileMap {
        private final int solidX;
        private final int solidY;

        SolidAtMap(final int solidX, final int solidY) {
            super(0, 0, TILE_SIZE_32);
            this.solidX = solidX;
            this.solidY = solidY;
        }

        public int getSolidX() {
            return this.solidX;
        }

        public int getSolidY() {
            return this.solidY;
        }

        @Override
        public Tile getTileAt(final int x, final int y) {
            if (x == this.solidX && y == this.solidY) {
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
            return new Tile(
                null,
                TILE_WIDTH_32,
                TILE_HEIGHT_32,
                TILE_ID_0,
                false,
                false,
                false,
                true,
                null
            );
        }
    }
}
