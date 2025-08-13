package com.thelegendofbald.item.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.item.ItemFactory;
import com.thelegendofbald.view.main.TileMap;

/**
 * Si occupa di spawnare GameItem leggendo i dati da un file tramite MapItemLoader.
 */
public class MapItemSpawner {

    private final TileMap tileMap;
    private final ItemFactory itemFactory;
    private final MapItemLoader loader;
    private final String itemFile;

    private final List<GameItem> items = new ArrayList<>();

    public MapItemSpawner(final TileMap tileMap, final ItemFactory itemFactory,
                          final MapItemLoader loader, final String itemFile) {
        this.tileMap = tileMap;
        this.itemFactory = itemFactory;
        this.loader = loader;
        this.itemFile = itemFile;
    }

    public List<GameItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void spawnItems() {
        items.clear();
        try {
            List<ItemSpawnData> spawnData = loader.load(itemFile);
            for (ItemSpawnData data : spawnData) {
                GameItem item = itemFactory.createItemById(
                        data.id,
                        data.col * tileMap.TILE_SIZE,
                        data.row * tileMap.TILE_SIZE
                );
                if (item != null) {
                    items.add(item);
                    System.out.printf("Creato item ID %d in posizione: (%d,%d)%n", data.id, data.col, data.row);
                } else {
                    System.err.println("Item con ID " + data.id + " non riconosciuto.");
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nel caricamento del file item: " + itemFile);
            e.printStackTrace();
        }
    }
}
