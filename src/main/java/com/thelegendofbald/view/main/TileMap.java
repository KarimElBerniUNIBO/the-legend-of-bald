package com.thelegendofbald.view.main;

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
 * Gestisce il caricamento e il rendering della mappa a tile.
 * <p>La classe è final per evitare il warning "designed for extension".</p>
 */
public class TileMap {

    private static final int ID_EMPTY   = 0;
    private static final int ID_FLOOR_A = 1;
    private static final int ID_WALL    = 2;
    private static final int ID_FLOOR_B = 4;
    private static final int ID_SPAWN   = 5; 
    private static final int ID_SHOP    = 6; 
    private static final int ID_SPECIAL = 7; 
    private static final int ID_PREV_PORTAL = 8;
    private static final int ID_BOSS = 9;
    private static final int ID_NEXT_MAP_TRIGGER = 10;

    private static final int DEFAULT_ROWS = 22; 
    private static final int DEFAULT_COLS = 40; 

    /** Larghezza in pixel dell'area di disegno. */
    private final int width;
    /** Altezza in pixel dell'area di disegno. */
    private final int height;
    /** Dimensione di un tile in pixel. */
    private final int tileSize;

    /** Matrice dei tile correnti renderizzati. */
    private Tile[][] tiles;
    /** Eventuale immagine di background. */
    private Image backgroundImage;
    /** Tipologie di tile indicizzate per id. */
    private final Map<Integer, Tile> tileTypes = new HashMap<>();

    /**
     * Crea una nuova mappa.
     *
     * @param width    larghezza in pixel dell'area di disegno (final)
     * @param height   altezza in pixel dell'area di disegno (final)
     * @param tileSize dimensione del tile in pixel (final)
     */
    public TileMap(final int width, final int height, final int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        loadTileTypes();
    }

    /** Carica i vari tipi di tile (pavimento, muro, shop, ecc.). */
    private void loadTileTypes() {
        try {
            final BufferedImage floor = loadBufferedImage("/images/map_png/floor-tiles.png");
            final BufferedImage wall = loadBufferedImage("/images/map_png/BrickGrey.png");
            final BufferedImage shop = loadBufferedImage("/images/map_png/shop.png");

            tileTypes.put(ID_EMPTY,   new Tile(null,  tileSize, tileSize, ID_EMPTY,   false, false, false, false, null));
            tileTypes.put(ID_FLOOR_A, new Tile(floor, tileSize, tileSize, ID_FLOOR_A, false, true,  false, true,  null));
            tileTypes.put(ID_WALL,    new Tile(wall,  tileSize, tileSize, ID_WALL,    true,  true,  false, false, null));
            tileTypes.put(ID_FLOOR_B, new Tile(floor, tileSize, tileSize, ID_FLOOR_B, false, true,  false, true,  null));
            tileTypes.put(ID_SPAWN,   new Tile(floor, tileSize, tileSize, ID_SPAWN,   false, true,  true,  true,  null));
            tileTypes.put(ID_SHOP,    new Tile(floor, tileSize, tileSize, ID_SHOP,    false, true,  true,  true,  shop));
            tileTypes.put(ID_SPECIAL, new Tile(floor, tileSize, tileSize, ID_SPECIAL, false, true,  true,  true,  null));
            tileTypes.put(ID_PREV_PORTAL,   new Tile(null,  tileSize, tileSize, ID_EMPTY,   false, false, false, false, null));
            tileTypes.put(ID_BOSS, new Tile(floor,tileSize,tileSize,ID_BOSS,false,true,true,true,null));
            tileTypes.put(ID_NEXT_MAP_TRIGGER, new Tile(floor, tileSize, tileSize, ID_NEXT_MAP_TRIGGER, false, true,  false, true,  null));
        } catch (final IOException e) {
            LoggerUtils.error("Errore nel caricamento delle immagini dei tile.", e);
            throw new IllegalStateException("Errore nel caricamento delle immagini dei tile.", e);
        }
    }

    /**
     * Carica un'immagine come {@link BufferedImage} da classpath.
     *
     * @param path percorso nella cartella resources
     * @return immagine caricata
     * @throws IOException se la lettura fallisce
     */
    private BufferedImage loadBufferedImage(final String path) throws IOException {
        final InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("Risorsa non trovata: " + path);
        }
        return ImageIO.read(stream);
    }

    /**
     * Carica una mappa specifica dal nome.
     *
     * @param mapName nome della mappa, es. "map_1"
     */
    private void loadMap(final String mapName) {
        int[][] mapData = generateFlatMap(DEFAULT_ROWS, DEFAULT_COLS, ID_EMPTY); // <- 22,40,0 sostituiti

        if (mapName != null) {
            switch (mapName) {
                case "map_1" -> {
                    mapData = loadMapFromFile("map_1.txt");
                    backgroundImage = loadImage("/images/map_png/castle.png");
                }
                case "map_2" -> mapData = loadMapFromFile("map_2.txt");
                case "map_3" -> mapData = loadMapFromFile("map_3.txt");
                case "map_4" -> mapData = loadMapFromFile("map_4.txt");
                default -> { }
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
                            baseTile.getOverlayImage()
                    );
                } else {
                    tiles[y][x] = null;
                }
            }
        }
    }

    /**
     * Carica una mappa da file di testo (in {@code resources/map}).
     *
     * @param fileName nome file (es. {@code map_1.txt})
     * @return matrice di id tile
     */
    private int[][] loadMapFromFile(final String fileName) {
        final List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/map/" + fileName), StandardCharsets.UTF_8))) {

            String line = reader.readLine();           // ← prima lettura fuori dalla condizione
            while (line != null) {                     // ← niente assegnazioni nell’operando
                final String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    final String[] tokens = trimmed.split("\\s+|,");
                    final int[] row = new int[tokens.length];
                    for (int i = 0; i < tokens.length; i++) {
                        row[i] = Integer.parseInt(tokens[i]);
                    }
                    rows.add(row);
                }
                line = reader.readLine();              // ← avanzamento esplicito
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
     * Carica una semplice immagine da classpath.
     *
     * @param path percorso risorsa
     * @return immagine o {@code null} se fallisce
     */
    private BufferedImage loadImage(final String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
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
     * Genera una mappa rettangolare uniforme.
     *
     * @param rows     righe
     * @param cols     colonne
     * @param tileType id tile da riempimento
     * @return matrice di id
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
     * Restituisce l'id del tile allineato alla griglia in base a coordinate pixel.
     *
     * @param x coordinata x in pixel
     * @param y coordinata y in pixel
     * @return id tile oppure -1 se fuori mappa o nullo
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
     * Restituisce il {@link Tile} alle coordinate di griglia.
     *
     * @param tileX colonna (indice tile)
     * @param tileY riga (indice tile)
     * @return tile o {@code null} se fuori mappa
     */
    public Tile getTileAt(final int tileX, final int tileY) {
        if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
            return tiles[tileY][tileX];
        }
        return null;
    }

    /**
     * Larghezza della mappa in numero di tile.
     *
     * @return colonne
     */
    public int getMapWidthInTiles() {
        if (tiles != null && tiles.length > 0) {
            return tiles[0].length;
        }
        return 0;
    }

    /**
     * Altezza della mappa in numero di tile.
     *
     * @return righe
     */
    public int getMapHeightInTiles() {
        if (tiles != null) {
            return tiles.length;
        }
        return 0;
    }

    /**
     * Cambia la mappa corrente caricandone una nuova.
     *
     * @param mapName nome mappa (es. {@code map_1})
     */
    public void changeMap(final String mapName) {
        loadMap(mapName);
    }

    /**
     * Trova il primo punto di spawn (in pixel) per il dato id tile.
     *
     * @param spawnTileId id del tile di spawn
     * @return punto in pixel o {@code null} se non trovato
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
     * Disegna background e tile.
     *
     * @param g contesto grafico
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
     * @return dimensione del tile in pixel.
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Trova tutte le posizioni (in pixel) dei tile con un certo id.
     *
     * @param wantedId id cercato
     * @return lista di punti in pixel
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
