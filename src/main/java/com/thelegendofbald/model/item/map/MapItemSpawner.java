package com.thelegendofbald.model.item.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.model.item.ItemFactory;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.TileMap;

/**
 * The MapItemSpawner class is responsible for spawning items on the game map
 * based on predefined spawn data loaded from a file. It utilizes an
 * {@link ItemFactory} to create items and a {@link MapItemLoader} to load the
 * spawn data.
 */
public class MapItemSpawner {

    private final TileMap tileMap;
    private final ItemFactory itemFactory;
    private final MapItemLoader loader;
    private final String itemFile;

    private final List<GameItem> items = new ArrayList<>();

    /**
     * Constructs a MapItemSpawner with the specified {@link TileMap},
     * {@link ItemFactory}, {@link MapItemLoader}, and item file path.
     *
     * @param tileMap     the tile map where items will be spawned
     * @param itemFactory the factory used to create items
     * @param loader      the loader used to load item spawn data
     * @param itemFile    the path to the file containing item spawn data
     */
    public MapItemSpawner(final TileMap tileMap, final ItemFactory itemFactory,
                          final MapItemLoader loader, final String itemFile) {
        this.tileMap = tileMap;
        this.itemFactory = itemFactory;
        this.loader = loader;
        this.itemFile = itemFile;
    }

    /**
     * Returns an unmodifiable list of currently spawned items.
     *
     * @return a list of spawned {@link GameItem} objects
     */
    public List<GameItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Spawns items on the map based on the spawn data loaded from the item file.
     * Existing items are cleared before spawning new ones.
     */
    public void spawnItems() {
        items.clear();
        try {
            final List<ItemSpawnData> spawnData = loader.load(itemFile);
            for (final ItemSpawnData data : spawnData) {
                final GameItem item = itemFactory.createItemById(
                        data.getId(),
                        data.getCol() * tileMap.TILE_SIZE,
                        data.getRow() * tileMap.TILE_SIZE
                );
                if (item != null) {
                    items.add(item);
                    LoggerUtils.info(String.format("Creato item ID %d in posizione: (%d,%d)%n", data.getId(), data.getCol(), data.getRow()));
                } else {
                    LoggerUtils.error("Item con ID " + data.getId() + " non riconosciuto.");
                }
            }
        } catch (IOException e) {
            LoggerUtils.error("Errore nel caricamento del file item: " + itemFile);
        }
    }
}
