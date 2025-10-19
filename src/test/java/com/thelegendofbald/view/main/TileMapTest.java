package com.thelegendofbald.view.main;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TileMapTest {

    private static final int TILE_SIZE = 32;

    @SuppressWarnings("unchecked")
    private static Map<Integer, Tile> getTileTypes(TileMap map) {
        try {
            Field f = TileMap.class.getDeclaredField("tileTypes");
            f.setAccessible(true);
            return (Map<Integer, Tile>) f.get(map);
        } catch (Exception e) { // compatto e robusto per i test
            throw new AssertionError(e);
        }
    }

    private static void setTiles(TileMap map, Tile[][] tiles) {
        try {
            Field f = TileMap.class.getDeclaredField("tiles");
            f.setAccessible(true);
            f.set(map, tiles);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    void testGetTileIdAtAndFindSpawn() {
        TileMap map = new TileMap(320, 192, TILE_SIZE);
        map.changeMap(null);

        Map<Integer, Tile> types = getTileTypes(map);
        Tile empty = types.get(0);
        Tile wall  = types.get(2);
        Tile spawn = types.get(5);

        // Matrice 2x3:
        // r0: [wall, empty, wall]
        // r1: [empty, spawn, empty]
        Tile[][] custom = {
            {wall,  empty, wall},
            {empty, spawn, empty}
        };
        setTiles(map, custom);

        // Coordinate pixel → ID
        assertEquals(2, map.getTileIdAt(0, 0));
        assertEquals(5, map.getTileIdAt(TILE_SIZE + 1, TILE_SIZE + 1));
        assertEquals(-1, map.getTileIdAt(99999, 5)); // fuori mappa

        // Primo spawn trovato
        Point p = map.findSpawnPoint(5);
        assertNotNull(p);
        assertEquals(new Point(TILE_SIZE, TILE_SIZE), p);
    }

    @Test
    void testFindAllWithId() {
        TileMap map = new TileMap(320, 192, TILE_SIZE);
        map.changeMap(null);

        Map<Integer, Tile> types = getTileTypes(map);
        Tile wall  = types.get(2);
        Tile empty = types.get(0);

        // r0: [wall, empty, wall]
        // r1: [empty, wall,  empty]
        Tile[][] custom = {
            {wall,  empty, wall},
            {empty, wall,  empty}
        };
        setTiles(map, custom);

        List<Point> result = map.findAllWithId(2);
        assertEquals(3, result.size());
        assertTrue(result.contains(new Point(0, 0)));
        assertTrue(result.contains(new Point(2 * TILE_SIZE, 0)));
        assertTrue(result.contains(new Point(TILE_SIZE, TILE_SIZE)));
    }

    @Test
    void testPaintDoesNotThrow() {
        TileMap map = new TileMap(320, 192, TILE_SIZE);
        map.changeMap(null);

        BufferedImage surface = new BufferedImage(160, 96, BufferedImage.TYPE_INT_ARGB);
        assertDoesNotThrow(() -> map.paint(surface.createGraphics()));
    }
}
