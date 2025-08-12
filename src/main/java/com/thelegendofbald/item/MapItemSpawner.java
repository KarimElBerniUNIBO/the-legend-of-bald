package com.thelegendofbald.item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thelegendofbald.view.main.TileMap;

/**
 * A class responsible for spawning {@link GameItem} instances
 * by reading a map file that defines item locations and IDs.
 * It uses an {@link ItemFactory} to create the items,
 * promoting loose coupling.
 */
public class MapItemSpawner {

    private final TileMap tileMap;
    private final ItemFactory itemFactory;
    private final List<GameItem> items = new ArrayList<>();

    private final String itemFile;

    /**
     * Constructs a new MapItemSpawner.
     *
     * @param tileMap     The {@link TileMap} instance to get tile sizes and coordinates.
     * @param itemFactory The factory to use for creating item instances.
     * @param itemFile    The name of the file containing item data.
     */
    public MapItemSpawner(final TileMap tileMap, final ItemFactory itemFactory, final String itemFile) {
        this.tileMap = tileMap;
        this.itemFactory = itemFactory;
        this.itemFile = itemFile;
    }

    /**
     * Retrieves an unmodifiable list of the spawned items.
     *
     * @return An unmodifiable {@link List} of {@link GameItem} objects.
     */
    public List<GameItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Clears the current list of items and spawns new ones by loading them from the item file.
     */
    public void spawnItems() {
        items.clear();
        loadItemsFromFile(itemFile);
    }

    /**
     * Loads item data from the specified file and populates the items list.
     *
     * @param fileName The name of the item data file (e.g., "item_map_1.txt").
     */
    private void loadItemsFromFile(final String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/item_map/" + fileName)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.split("\\s+");
                if (tokens.length != 3) {
                    System.err.println("Invalid line format in " + fileName + ": " + line);
                    continue;
                }

                try {
                    int itemId = Integer.parseInt(tokens[0]);
                    int row = Integer.parseInt(tokens[1]);
                    int col = Integer.parseInt(tokens[2]);

                    GameItem item = itemFactory.createItemById(itemId, col * tileMap.TILE_SIZE, row * tileMap.TILE_SIZE);
                    if (item != null) {
                        items.add(item);
                        System.out.printf("Created item with ID %d at coordinates: (%d, %d)%n", itemId, col, row);
                    } else {
                        System.err.println("Could not create item with ID: " + itemId);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Numeric format error in line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading item file: " + fileName);
            e.printStackTrace();
        }
    }
}