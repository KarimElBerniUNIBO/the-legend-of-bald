package com.thelegendofbald.ui.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class TileMap {

    private final int width, height;
    private String currentMap;
    private Tile[][] tiles;
    private Image backgroundImage;
    public final int TILE_SIZE = 32; // puoi cambiarlo
    private BufferedImage image;

    private final Map<Integer, Tile> tileTypes = new HashMap<>();

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.currentMap = "map_1"; // default
        loadTileTypes();
        loadMap(currentMap);
    }

    // Carica i vari tipi di tile (es. erba, muro, acqua)
    private void loadTileTypes() {
        try {
            // Carica le immagini per i vari tipi di tile
            BufferedImage floor = ImageIO.read(getClass().getResource("/images/map_png/floor-tiles.png"));
            BufferedImage wall = ImageIO.read(getClass().getResource("/images/map_png/BrickGrey.png"));

            // Aggiungi i vari tipi di tile con il costruttore aggiornato
            tileTypes.put(0, new Tile(null, TILE_SIZE, TILE_SIZE, 0, false, false, false, false));  // Tile vuoto
            tileTypes.put(1, new Tile(floor, TILE_SIZE, TILE_SIZE, 1, false, true, false, true));  // Tile per il pavimento
            tileTypes.put(2, new Tile(wall, TILE_SIZE, TILE_SIZE, 2, false, true, false, false));    // Tile per il muro (solido)
            tileTypes.put(4, new Tile(floor, TILE_SIZE, TILE_SIZE, 4, false, true, false, true));
            tileTypes.put(5, new Tile(floor, TILE_SIZE, TILE_SIZE, 5, false, true, true, true)); // Un altro tile pavimento (opzionale)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carica una mappa specifica
    private void loadMap(String mapName) {
        currentMap = mapName;
        int[][] mapData = generateFlatMap(22, 40, 0); // fallback

        if (mapName != null) {
            switch (mapName) {
                case "map_1":
                    mapData = loadMapFromFile("map_1.txt");
                    backgroundImage = loadImage("/images/map_png/castle.png");
                    break;
                case "map_2":
                    mapData = loadMapFromFile("map_2.txt");
                    break;
                case "map_3":
                    mapData = loadMapFromFile("map_3.txt");
                    break;
            }
        }

        tiles = new Tile[mapData.length][mapData[0].length];
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length; x++) {
                int tileId = mapData[y][x];
                Tile baseTile = tileTypes.get(tileId);

                if (baseTile != null) {
                    tiles[y][x] = new Tile(
                        baseTile.getImage(),
                        baseTile.getWidth(),
                        baseTile.getHeight(),
                        tileId,
                        baseTile.isSolid(),
                        false,
                        baseTile.isWalkable(),
                        baseTile.isSpawn()
                    );
                } else {
                    tiles[y][x] = null;
                }
            }
        }
    }

    // Carica una mappa da file txt in resources/map
    private int[][] loadMapFromFile(String fileName) {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/map/" + fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Salta le righe vuote
                String[] tokens = line.split("\\s+|,");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }
                rows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[][] map = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            map[i] = rows.get(i);
        }

        return map;
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int[][] generateFlatMap(int rows, int cols, int tileType) {
        int[][] map = new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                map[y][x] = tileType;
            }
        }
        return map;
    }

    public int getTileIdAt(int x, int y) {
        int tileX = x / TILE_SIZE;
        int tileY = y / TILE_SIZE;

        if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
            Tile tile = tiles[tileY][tileX];
            return tile != null ? tile.getId() : -1;
        }
        return -1;
    }

    public Tile getTileAt(int x, int y) {
        int tileX = x / TILE_SIZE;
        int tileY = y / TILE_SIZE;

        if (tileY >= 0 && tileY < tiles.length && tileX >= 0 && tileX < tiles[0].length) {
            return tiles[tileY][tileX];
        }
        return null;
    }

    public int getMapWidthInTiles() {
        if (tiles != null && tiles.length > 0) {
            return tiles[0].length;
        }
        return 0;
    }

    public int getMapHeightInTiles() {
        if (tiles != null) {
            return tiles.length;
        }
        return 0;
    }

    // Metodo pubblico per cambiare mappa
    public void changeMap(String mapName) {
        loadMap(mapName);
    }

    public Point findSpawnPoint(int spawnTileId) {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile != null && tile.getId() == spawnTileId) {
                    return new Point(x * TILE_SIZE, y * TILE_SIZE);
                }
            }
        }
        return null;
    }

    // Disegna mappa e background
    public void paint(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
        }

        if (tiles == null) return;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                Tile tile = tiles[row][col];
                if (tile != null) {
                    tile.render(g, col * TILE_SIZE, row * TILE_SIZE);
                }
            }
        }
    }
} 

    
       
