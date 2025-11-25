package com.thelegendofbald.view.render;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.thelegendofbald.utils.LoggerUtils;

/**
 * Manages loading, logic, and rendering of the tile map.
 * <p>
 * The class is not final to allow mocking in tests (e.g., EmptyTileMap,
 * SolidTileMap).
 * The UI_INHERITANCE_UNSAFE_GETRESOURCE warning is resolved using TileMap.class
 * instead of getClass().
 * </p>
 */
public class TileMap {

    /** Identifier for an empty tile (no render, no collision). */
    private static final int ID_EMPTY = 0;
    /** Identifier for standard floor (type A). */
    private static final int ID_FLOOR_A = 1;
    /** Identifier for a solid wall. */
    private static final int ID_WALL = 2;
    /** Identifier for alternative floor (type B). */
    private static final int ID_FLOOR_B = 4;
    /** Identifier for player spawn point. */
    private static final int ID_SPAWN = 5;
    /** Identifier for shop area. */
    private static final int ID_SHOP = 6;
    /** Identifier for special tiles or events. */
    private static final int ID_SPECIAL = 7;
    /** Identifier for the portal leading to the previous map. */
    private static final int ID_PREV_PORTAL = 8;
    /** Identifier for boss area. */
    private static final int ID_BOSS = 9;
    /** Identifier for the trigger leading to the next map. */
    private static final int ID_NEXT_MAP_TRIGGER = 10;

    /** Default number of rows for a generated map. */
    private static final int DEFAULT_ROWS = 22;
    /** Default number of columns for a generated map. */
    private static final int DEFAULT_COLS = 40;

    /** Total width of the drawing area in pixels. */
    private final int width;
    /** Total height of the drawing area in pixels. */
    private final int height;
    /** Side length of a single tile in pixels. */
    private final int tileSize;

    /** 2D grid containing references to active tiles in the map. */
    private Tile[][] tiles;
    /** Background image drawn before tiles. */
    private Image backgroundImage;
    /** Map associating numeric IDs to configured Tile prototypes. */
    private final Map<Integer, Tile> tileTypes = new HashMap<>();

    /**
     * Creates a new instance of TileMap.
     *
     * @param width    width in pixels of the drawing area
     * @param height   height in pixels of the drawing area
     * @param tileSize tile size in pixels
     */
    public TileMap(final int width, final int height, final int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        loadTileTypes();
    }

    /**
     * Initializes available tile types by loading graphic resources and defining
     * properties.
     * Populates the {@code tileTypes} map.
     */
    private void loadTileTypes() {
        try {
            final BufferedImage floor = loadBufferedImage("/images/map_png/floor-tiles.png");
            final BufferedImage wall = loadBufferedImage("/images/map_png/BrickGrey.png");
            final BufferedImage shop = loadBufferedImage("/images/map_png/shop.png");

            tileTypes.put(ID_EMPTY, new Tile(null, tileSize, tileSize, ID_EMPTY, false, false, false, false, null));
            tileTypes.put(ID_FLOOR_A, new Tile(floor, tileSize, tileSize, ID_FLOOR_A, false, true, false, true, null));
            tileTypes.put(ID_WALL, new Tile(wall, tileSize, tileSize, ID_WALL, true, true, false, false, null));
            tileTypes.put(ID_FLOOR_B, new Tile(floor, tileSize, tileSize, ID_FLOOR_B, false, true, false, true, null));
            tileTypes.put(ID_SPAWN, new Tile(floor, tileSize, tileSize, ID_SPAWN, false, true, true, true, null));
            tileTypes.put(ID_SHOP, new Tile(floor, tileSize, tileSize, ID_SHOP, false, true, true, true, shop));
            tileTypes.put(ID_SPECIAL, new Tile(floor, tileSize, tileSize, ID_SPECIAL, false, true, true, true, null));
            tileTypes.put(ID_PREV_PORTAL,
                    new Tile(null, tileSize, tileSize, ID_EMPTY, false, false, false, false, null));
            tileTypes.put(ID_BOSS, new Tile(floor, tileSize, tileSize, ID_BOSS, false, true, true, true, null));
            tileTypes.put(ID_NEXT_MAP_TRIGGER,
                    new Tile(floor, tileSize, tileSize, ID_NEXT_MAP_TRIGGER, false, true, false, true, null));
        } catch (final IOException e) {
            LoggerUtils.error("Errore nel caricamento delle immagini dei tile.", e);
            throw new IllegalStateException("Errore nel caricamento delle immagini dei tile.", e);
        }
    }

    /**
     * Loads an image as {@link BufferedImage} from the classpath.
     *
     * @param path path of the image file in the resources folder
     * @return the loaded image
     * @throws IOException if file reading fails
     */
    private BufferedImage loadBufferedImage(final String path) throws IOException {
        final InputStream stream = TileMap.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("Risorsa non trovata: " + path);
        }
        return ImageIO.read(stream);
    }

    /**
     * Loads data and specific resources for a map based on the provided name.
     * Sets the {@code tiles} matrix and background image.
     *
     * @param mapName identifier name of the map (e.g. "map_1")
     */
    private void loadMap(final String mapName) {
        int[][] mapData = generateFlatMap(DEFAULT_ROWS, DEFAULT_COLS, ID_EMPTY);

        if (mapName != null) {
            switch (mapName) {
                case "map_1" -> {
                    mapData = loadMapFromFile("map_1.txt");
                    backgroundImage = loadImage("/images/map_png/castle.png");
                }
                case "map_2" -> mapData = loadMapFromFile("map_2.txt");
                case "map_3" -> mapData = loadMapFromFile("map_3.txt");
                case "map_4" -> mapData = loadMapFromFile("map_4.txt");
                default -> {
                    // Mappa default o non gestita
                }
            }
        }

        tiles = new Tile[mapData.length][mapData[0].length];
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length; x++) {
                final int tileId = mapData[y][x];
                final Tile baseTile = tileTypes.get(tileId);

                if (baseTile != null) {
                    tiles[y][x] = new Tile(
                            baseTile.getImage(),
                            baseTile.getWidth(),
                            baseTile.getHeight(),
                            tileId,
                            baseTile.isSolid(),
                            false,
                            baseTile.isWalkable(),
                            baseTile.isSpawn(),
                            baseTile.getOverlayImage());
                } else {
                    tiles[y][x] = null;
                }
            }
        }
    }

    /**
     * Loads the map structure from a text file located in {@code resources/map}.
     *
     * @param fileName name of the file (e.g. {@code map_1.txt})
     * @return a matrix of integers representing tile IDs
     */
    private int[][] loadMapFromFile(final String fileName) {
        final List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                TileMap.class.getResourceAsStream("/map/" + fileName), StandardCharsets.UTF_8))) {

            String line = reader.readLine();
            while (line != null) {
                final String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    final String[] tokens = trimmed.split("\\s+|,");
                    final int[] row = new int[tokens.length];
                    for (int i = 0; i < tokens.length; i++) {
                        row[i] = Integer.parseInt(tokens[i]);
                    }
                    rows.add(row);
                }
                line = reader.readLine();
            }
        } catch (final IOException e) {
            LoggerUtils.error("Errore nel caricamento della mappa: " + fileName, e);
        }

        final int[][] map = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            map[i] = rows.get(i);
        }
        return map;
    }

    /**
     * Loads a simple image from the classpath without throwing blocking exceptions.
     *
     * @param path path of the image resource
     * @return the loaded image or {@code null} if not found or in case of error
     */
    private BufferedImage loadImage(final String path) {
        try (InputStream is = TileMap.class.getResourceAsStream(path)) {
            if (is == null) {
                LoggerUtils.error("Immagine non trovata: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (final IOException e) {
            LoggerUtils.error("Errore nel caricamento dell'immagine: " + path, e);
            return null;
        }
    }

    /**
     * Generates a "flat" map (empty or uniform) of specified dimensions.
     *
     * @param rows     number of rows
     * @param cols     number of columns
     * @param tileType ID of the tile to fill the map with
     * @return initialized ID matrix
     */
    private int[][] generateFlatMap(final int rows, final int cols, final int tileType) {
        final int[][] map = new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                map[y][x] = tileType;
            }
        }
        return map;
    }

    /**
     * Returns the ID of the tile located at the specified pixel coordinates.
     *
     * @param x x coordinate in pixels
     * @param y y coordinate in pixels
     * @return the tile ID, or -1 if coordinates are out of map bounds
     */
    public int getTileIdAt(final int x, final int y) {
        final int tileX = x / tileSize;
        final int tileY = y / tileSize;

        if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
            final Tile tile = tiles[tileY][tileX];
            return tile != null ? tile.getId() : -1;
        }
        return -1;
    }

    /**
     * Returns the {@link Tile} object at the specified grid coordinates.
     *
     * @param tileX column index (grid coordinate)
     * @param tileY row index (grid coordinate)
     * @return the Tile object or {@code null} if coordinates are out of map
     */
    public Tile getTileAt(final int tileX, final int tileY) {
        if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
            return tiles[tileY][tileX];
        }
        return null;
    }

    /**
     * Returns the map width expressed in number of tiles (columns).
     *
     * @return the number of columns in the map
     */
    public int getMapWidthInTiles() {
        if (tiles != null && tiles.length > 0) {
            return tiles[0].length;
        }
        return 0;
    }

    /**
     * Returns the map height expressed in number of tiles (rows).
     *
     * @return the number of rows in the map
     */
    public int getMapHeightInTiles() {
        if (tiles != null) {
            return tiles.length;
        }
        return 0;
    }

    /**
     * Changes the current map by loading data and resources of the new specified
     * map.
     *
     * @param mapName name of the map to load (e.g. {@code map_1})
     */
    public void changeMap(final String mapName) {
        loadMap(mapName);
    }

    /**
     * Searches for the first occurrence (in pixel coordinates) of a tile with the
     * specified spawn ID.
     *
     * @param spawnTileId the spawn tile ID to search for
     * @return a {@link Point} containing pixel coordinates (x, y) or {@code null}
     *         if not found
     */
    public Point findSpawnPoint(final int spawnTileId) {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                final Tile tile = tiles[y][x];
                if (tile != null && tile.getId() == spawnTileId) {
                    return new Point(x * tileSize, y * tileSize);
                }
            }
        }
        return null;
    }

    /**
     * Renders the map (background and tiles) on the provided graphics context.
     *
     * @param g the {@link Graphics} context to draw on
     */
    public void paint(final Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
        }

        if (tiles == null) {
            return;
        }
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                final Tile tile = tiles[row][col];
                if (tile != null) {
                    tile.render(g, col * tileSize, row * tileSize);
                }
            }
        }
    }

    /**
     * Returns the side length of a tile.
     *
     * @return tile size in pixels.
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Finds all positions (in pixels) of tiles matching the specified ID.
     *
     * @param wantedId the tile ID to search for
     * @return a list of {@link Point} representing pixel coordinates (top-left) of
     *         found tiles
     */
    public List<Point> findAllWithId(final int wantedId) {
        final List<Point> points = new ArrayList<>();
        if (tiles == null) {
            return points;
        }
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                final Tile t = tiles[y][x];
                if (t != null && t.getId() == wantedId) {
                    points.add(new Point(x * tileSize, y * tileSize));
                }
            }
        }
        return points;
    }
}
