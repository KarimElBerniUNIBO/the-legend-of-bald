package com.thelegendofbald.view.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.thelegendofbald.view.render.Tile;
import com.thelegendofbald.view.render.TileMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link TileMap}.
 * Uses reflection to inject custom tile configurations for unit testing logic.
 */
class TileMapTest {

    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 320;
    private static final int MAP_HEIGHT = 192;

    private static final int ID_EMPTY = 0;
    private static final int ID_WALL = 2;
    private static final int ID_SPAWN = 5;
    private static final int ID_OUT_OF_BOUNDS = -1;

    private static final int OUT_OF_BOUNDS_X = 99_999;
    private static final int OUT_OF_BOUNDS_Y = 5;

    private static final int PAINT_WIDTH = 160;
    private static final int PAINT_HEIGHT = 96;

    /**
     * Helper method to access the private 'tileTypes' map via reflection.
     *
     * @param map the TileMap instance
     * @return the internal map of ID to Tile objects
     */
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    private static Map<Integer, Tile> getTileTypes(final TileMap map) {
        try {
            final Field f = TileMap.class.getDeclaredField("tileTypes");
            f.setAccessible(true);
            return (Map<Integer, Tile>) f.get(map);
        } catch (final ReflectiveOperationException e) {
            throw new AssertionError("Failed to access tileTypes via reflection", e);
        }
    }

    /**
     * Helper method to inject a custom 2D Tile array into the map via reflection.
     *
     * @param map the TileMap instance
     * @param tiles the 2D array of tiles to set
     */
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    private static void setTiles(final TileMap map, final Tile[][] tiles) {
        try {
            final Field f = TileMap.class.getDeclaredField("tiles");
            f.setAccessible(true);
            f.set(map, tiles);
        } catch (final ReflectiveOperationException e) {
            throw new AssertionError("Failed to set tiles via reflection", e);
        }
    }

    /**
     * Verifies that {@code getTileIdAt} correctly resolves pixel coordinates to tile IDs,
     * handles out-of-bounds coordinates, and that {@code findSpawnPoint} locates the specific ID.
     */
    @Test
    @DisplayName("getTileIdAt resolves coordinates and findSpawnPoint locates ID")
    void verifyTileIdAtAndFindSpawnBehaveCorrectly() {
        final TileMap map = new TileMap(MAP_WIDTH, MAP_HEIGHT, TILE_SIZE);
        map.changeMap(null);

        final Map<Integer, Tile> types = getTileTypes(map);
        final Tile empty = types.get(ID_EMPTY);
        final Tile wall = types.get(ID_WALL);
        final Tile spawn = types.get(ID_SPAWN);

        final Tile[][] custom = {
                {wall, empty, wall},
                {empty, spawn, empty},
        };
        setTiles(map, custom);

        assertEquals(ID_WALL, map.getTileIdAt(0, 0), "Should be Wall at (0,0)");

        assertEquals(ID_SPAWN, map.getTileIdAt(TILE_SIZE + 1, TILE_SIZE + 1),
                "Should be Spawn at (33,33)");

        assertEquals(ID_OUT_OF_BOUNDS, map.getTileIdAt(OUT_OF_BOUNDS_X, OUT_OF_BOUNDS_Y),
                "Should return -1 for coordinates outside the map");

        final Point p = map.findSpawnPoint(ID_SPAWN);
        assertNotNull(p, "Spawn point should be found");
        assertEquals(new Point(TILE_SIZE, TILE_SIZE), p,
                "Spawn point should be at the correct tile coordinates");
    }

    /**
     * Verifies that {@code findAllWithId} returns a list of all points matching a specific Tile ID.
     */
    @Test
    @DisplayName("findAllWithId returns all matching coordinate points")
    void findAllWithIdReturnsCorrectPoints() {
        final TileMap map = new TileMap(MAP_WIDTH, MAP_HEIGHT, TILE_SIZE);
        map.changeMap(null);

        final Map<Integer, Tile> types = getTileTypes(map);
        final Tile wall = types.get(ID_WALL);
        final Tile empty = types.get(ID_EMPTY);

        final Tile[][] custom = {
                {wall, empty, wall},
                {empty, wall, empty},
        };
        setTiles(map, custom);

        final List<Point> result = map.findAllWithId(ID_WALL);

        assertEquals(3, result.size(), "Should find exactly 3 wall tiles");

        assertTrue(result.contains(new Point(0, 0)));
        assertTrue(result.contains(new Point(2 * TILE_SIZE, 0)));
        assertTrue(result.contains(new Point(TILE_SIZE, TILE_SIZE)));
    }

    /**
     * Smoke test to ensure the paint method runs without throwing exceptions on a dummy graphics context.
     */
    @Test
    @DisplayName("paint() executes without exceptions")
    void paintDoesNotThrowException() {
        final TileMap map = new TileMap(MAP_WIDTH, MAP_HEIGHT, TILE_SIZE);
        map.changeMap(null);

        final BufferedImage surface = new BufferedImage(PAINT_WIDTH, PAINT_HEIGHT,
                BufferedImage.TYPE_INT_ARGB);

        assertDoesNotThrow(() -> map.paint(surface.createGraphics()));
    }
}
