package com.thelegendofbald.model.characters;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    /** Fake TileMap: no solid tiles, fixed tile size. */
    static class EmptyTileMap extends TileMap {
        EmptyTileMap() {
            super( 0, 0, 32); // tileSize = 32
        }
        @Override
        public Tile getTileAt(int x, int y) {
            return null; // always empty
        }
    }

    /** Fake TileMap: every tile is solid. */
static class SolidTileMap extends TileMap {
    SolidTileMap() {
        super( 0, 0, 32); // tileSize = 32, righe/colonne = 0 per test
    }

    @Override
    public Tile getTileAt(int x, int y) {
        // Ritorna sempre un Tile solido
        return new Tile(
            null,    // image
            32,      // width
            32,      // height
            1,       // id
            true,    // solid
            false,   // resize
            false,   // isSpawn
            true,    // walkable
            null     // overlayImage
        );
    }
}

    @Test
    void attackPowerIsValuePassedInConstructor() {
        DummyEnemy enemy = new DummyEnemy(0, 0, 50, "Goblin", 7, new EmptyTileMap());
        assertEquals(7, enemy.getAttackPower());
    }

    @Test
    void takeDamageReducesHealth_andIsAliveReflectsIt() {
        DummyEnemy enemy = new DummyEnemy(0, 0, 20, "Goblin", 5, new EmptyTileMap());
        assertTrue(enemy.isAlive());

        enemy.takeDamage(15);
        assertTrue(enemy.isAlive(), "Enemy should still be alive after partial damage");

        enemy.takeDamage(10); // total damage > health
        assertFalse(enemy.isAlive(), "Enemy should be dead after lethal damage");
    }

    @Test
    void followPlayerMovesTowardBaldUnlessBlocked() {
        // Free map: enemy moves closer
        DummyEnemy enemyFree = new DummyEnemy(0, 0, 30, "Goblin", 5, new EmptyTileMap());
        Bald bald = new Bald(100, 100, 100, "Hero", 10);

        enemyFree.followPlayer(bald);
        assertTrue(enemyFree.getX() > 0 && enemyFree.getY() > 0,
                "Enemy should have moved closer on free map");

        // Solid map: enemy blocked
        DummyEnemy enemyBlocked = new DummyEnemy(10, 10, 30, "Goblin", 5, new SolidTileMap());
        enemyBlocked.followPlayer(bald);
        assertEquals(10, enemyBlocked.getX());
        assertEquals(10, enemyBlocked.getY());
    }
}

