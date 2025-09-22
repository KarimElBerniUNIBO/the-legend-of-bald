package com.thelegendofbald.model.combat.projectile;

import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    /** Mappa vuota: nessuna tile solida. (Costruttore a 3 int come nel tuo snippet: super(0,0,32)) */
    static class EmptyMap extends TileMap {
        EmptyMap() { super(0, 0, 32); }
        @Override public Tile getTileAt(int x, int y) { return null; }
    }

    /** Mappa con una tile solida in (solidX, solidY). */
    static class SolidAtMap extends TileMap {
        final int solidX, solidY;
        SolidAtMap(int solidX, int solidY) {
            super(0, 0, 32);
            this.solidX = solidX; this.solidY = solidY;
        }
        @Override public Tile getTileAt(int x, int y) {
            if (x == solidX && y == solidY) {
                // Tile solida: usa il costruttore lungo che hai indicato
                return new Tile(
                        null,   // image
                        32,     // width
                        32,     // height
                        1,      // id
                        true,   // solid
                        false,  // resize
                        false,  // isSpawn
                        true,   // walkable (irrilevante qui)
                        null    // overlayImage
                );
            }
            // Tile non solida altrove
            return new Tile(null, 32, 32, 0, false, false, false, true, null);
        }
    }

    @Test
    void movesRightOnEmptyMap_andStaysActive() {
        TileMap map = new EmptyMap();
        Projectile p = new Projectile(10, 20, /*direction=*/0, /*speed=*/5, /*damage=*/12);

        p.move(map);

        // N.B. Projectile mantiene x,y privati ⇒ leggiamo da getBounds()
        assertEquals(15, p.getBounds().x);
        assertEquals(20, p.getBounds().y);
        assertTrue(p.isAlive());
        assertEquals(12, p.getAttackPower());
    }

    @Test
    void deactivatesOnCollision_withSolidTile_cornerCheck() {
        // TILE_SIZE = 32; solid in (2,0)
        // Start x=63, speed=4 ⇒ nextX=67; top-right corner: 67+5=72 ⇒ 72/32=2 (colonna 2), y=0 ⇒ riga 0
        TileMap map = new SolidAtMap(/*solidX=*/2, /*solidY=*/0);
        Projectile p = new Projectile(63, 0, /*direction=*/0, /*speed=*/4, /*damage=*/5);

        p.move(map);

        // Collisione ⇒ non avanza e diventa inactive
        assertEquals(63, p.getBounds().x);
        assertEquals(0, p.getBounds().y);
        assertFalse(p.isAlive());
    }

    @Test
    void boundsAre6x6_andFollowPosition() {
        Projectile p = new Projectile(100, 200, /*direction=*/1, /*speed=*/3, /*damage=*/1);
        assertEquals(100, p.getBounds().x);
        assertEquals(200, p.getBounds().y);
        assertEquals(6, p.getBounds().width);
        assertEquals(6, p.getBounds().height);
    }
}
