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
 * Gestisce il caricamento, la logica e il rendering della mappa a tile.
 * <p>
 * La classe non è final per permettere il mocking nei test (es. EmptyTileMap, SolidTileMap).
 * L'avviso UI_INHERITANCE_UNSAFE_GETRESOURCE è risolto usando TileMap.class invece di getClass().
 * </p>
 */
public class TileMap {

    /** Identificativo per un tile vuoto (nessun render, nessuna collisione). */
    private static final int ID_EMPTY = 0;
    /** Identificativo per il pavimento standard (tipo A). */
    private static final int ID_FLOOR_A = 1;
    /** Identificativo per un muro solido. */
    private static final int ID_WALL = 2;
    /** Identificativo per il pavimento alternativo (tipo B). */
    private static final int ID_FLOOR_B = 4;
    /** Identificativo per il punto di spawn del giocatore. */
    private static final int ID_SPAWN = 5;
    /** Identificativo per l'area del negozio. */
    private static final int ID_SHOP = 6;
    /** Identificativo per tile speciali o eventi. */
    private static final int ID_SPECIAL = 7;
    /** Identificativo per il portale che riporta alla mappa precedente. */
    private static final int ID_PREV_PORTAL = 8;
    /** Identificativo per l'area del boss. */
    private static final int ID_BOSS = 9;
    /** Identificativo per il trigger che porta alla mappa successiva. */
    private static final int ID_NEXT_MAP_TRIGGER = 10;

    /** Numero di righe predefinito per una mappa generata. */
    private static final int DEFAULT_ROWS = 22;
    /** Numero di colonne predefinito per una mappa generata. */
    private static final int DEFAULT_COLS = 40;

    /** Larghezza totale dell'area di disegno in pixel. */
    private final int width;
    /** Altezza totale dell'area di disegno in pixel. */
    private final int height;
    /** Dimensione del lato di un singolo tile in pixel. */
    private final int tileSize;

    /** Griglia bidimensionale contenente i riferimenti ai tile attivi nella mappa. */
    private Tile[][] tiles;
    /** Immagine di sfondo disegnata prima dei tile. */
    private Image backgroundImage;
    /** Mappa che associa gli ID numerici ai prototipi dei Tile configurati. */
    private final Map<Integer, Tile> tileTypes = new HashMap<>();

    /**
     * Crea una nuova istanza di TileMap.
     *
     * @param width    larghezza in pixel dell'area di disegno
     * @param height   altezza in pixel dell'area di disegno
     * @param tileSize dimensione del tile in pixel
     */
    public TileMap(final int width, final int height, final int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        loadTileTypes();
    }

    /**
     * Inizializza i tipi di tile disponibili caricando le risorse grafiche e definendo le proprietà.
     * Popola la mappa {@code tileTypes}.
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
     * Carica un'immagine come {@link BufferedImage} dal classpath.
     *
     * @param path percorso del file immagine nella cartella resources
     * @return l'immagine caricata
     * @throws IOException se la lettura del file fallisce
     */
    private BufferedImage loadBufferedImage(final String path) throws IOException {
        // MANTENIAMO TileMap.class invece di getClass() per evitare problemi di sicurezza
        final InputStream stream = TileMap.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("Risorsa non trovata: " + path);
        }
        return ImageIO.read(stream);
    }

    /**
     * Carica i dati e le risorse specifiche per una mappa in base al nome fornito.
     * Imposta la matrice {@code tiles} e l'immagine di background.
     *
     * @param mapName nome identificativo della mappa (es. "map_1")
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
     * Carica la struttura della mappa da un file di testo situato in {@code resources/map}.
     *
     * @param fileName nome del file (es. {@code map_1.txt})
     * @return una matrice di interi rappresentanti gli ID dei tile
     */
    private int[][] loadMapFromFile(final String fileName) {
        final List<int[]> rows = new ArrayList<>();

        // MANTENIAMO TileMap.class invece di getClass()
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
     * Carica una semplice immagine dal classpath senza lanciare eccezioni bloccanti.
     *
     * @param path percorso della risorsa immagine
     * @return l'immagine caricata o {@code null} se non trovata o in caso di errore
     */
    private BufferedImage loadImage(final String path) {
        // MANTENIAMO TileMap.class invece di getClass()
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
     * Genera una mappa "piatta" (vuota o uniforme) di dimensioni specificate.
     *
     * @param rows     numero di righe
     * @param cols     numero di colonne
     * @param tileType ID del tile con cui riempire la mappa
     * @return matrice di ID inizializzata
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
     * Restituisce l'ID del tile che si trova alle coordinate pixel specificate.
     *
     * @param x coordinata x in pixel
     * @param y coordinata y in pixel
     * @return l'ID del tile, oppure -1 se le coordinate sono fuori dai limiti della mappa
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
     * Restituisce l'oggetto {@link Tile} alle coordinate di griglia specificate.
     *
     * @param tileX indice della colonna (coordinata griglia)
     * @param tileY indice della riga (coordinata griglia)
     * @return l'oggetto Tile o {@code null} se le coordinate sono fuori mappa
     */
    public Tile getTileAt(final int tileX, final int tileY) {
        if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
            return tiles[tileY][tileX];
        }
        return null;
    }

    /**
     * Restituisce la larghezza della mappa espressa in numero di tile (colonne).
     *
     * @return il numero di colonne della mappa
     */
    public int getMapWidthInTiles() {
        if (tiles != null && tiles.length > 0) {
            return tiles[0].length;
        }
        return 0;
    }

    /**
     * Restituisce l'altezza della mappa espressa in numero di tile (righe).
     *
     * @return il numero di righe della mappa
     */
    public int getMapHeightInTiles() {
        if (tiles != null) {
            return tiles.length;
        }
        return 0;
    }

    /**
     * Cambia la mappa corrente caricando i dati e le risorse della nuova mappa specificata.
     *
     * @param mapName nome della mappa da caricare (es. {@code map_1})
     */
    public void changeMap(final String mapName) {
        loadMap(mapName);
    }

    /**
     * Cerca la prima occorrenza (in coordinate pixel) di un tile con l'ID di spawn specificato.
     *
     * @param spawnTileId l'ID del tile di spawn da cercare
     * @return un {@link Point} contenente le coordinate pixel (x, y) o {@code null} se non trovato
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
     * Esegue il rendering della mappa (sfondo e tile) sul contesto grafico fornito.
     *
     * @param g il contesto grafico {@link Graphics} su cui disegnare
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
     * Restituisce la dimensione del lato di un tile.
     *
     * @return dimensione del tile in pixel.
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Trova tutte le posizioni (in pixel) dei tile che corrispondono all'ID specificato.
     *
     * @param wantedId l'ID del tile da cercare
     * @return una lista di {@link Point} rappresentanti le coordinate pixel (top-left) dei tile trovati
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
