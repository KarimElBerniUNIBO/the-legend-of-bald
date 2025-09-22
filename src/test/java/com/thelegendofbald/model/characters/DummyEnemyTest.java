package com.thelegendofbald.model.characters;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DummyEnemyTest {

    /** Fake TileMap that is empty: no solid tiles, fixed tile size. */
    static class EmptyTileMap extends TileMap {
        EmptyTileMap() {
            super( 0, 0, 32); // minimal constructor call
        }
        @Override
        public Tile getTileAt(int x, int y) {
            return null; // no collisions
        }
    }

    @Test
    void getAttackPower_returnsValuePassedInConstructor() {
        DummyEnemy enemy = new DummyEnemy(0, 0, 50, "Goblin", 7, new EmptyTileMap());
        assertEquals(7, enemy.getAttackPower());
    }

    @Test
    void takeDamage_reducesHealth_andIsAliveReflectsThat() {
        DummyEnemy enemy = new DummyEnemy(0, 0, 20, "Goblin", 5, new EmptyTileMap());
        assertTrue(enemy.isAlive());

        enemy.takeDamage(15);
        assertTrue(enemy.isAlive());

        enemy.takeDamage(10); // total damage 25 > 20
        assertFalse(enemy.isAlive());
    }

    @Test
    void followPlayer_movesCloserToBald_whenNoCollision() {
        DummyEnemy enemy = new DummyEnemy(0, 0, 30, "Goblin", 5, new EmptyTileMap());
        Bald bald = new Bald(100, 100, 100, "Hero", 10);

        enemy.followPlayer(bald);

        // Since Bald is to the bottom-right, both coordinates should increase
        assertTrue(enemy.getX() > 0);
        assertTrue(enemy.getY() > 0);
    }
}

