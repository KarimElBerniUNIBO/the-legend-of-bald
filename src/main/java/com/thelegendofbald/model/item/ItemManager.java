package com.thelegendofbald.model.item;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.thelegendofbald.api.common.animation.Animatable;
import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.model.item.map.MapItemLoader;
import com.thelegendofbald.model.item.map.MapItemSpawner;
import com.thelegendofbald.model.item.traps.Trap;
import com.thelegendofbald.view.main.TileMap;

public class ItemManager {

    private List<GameItem> items;
    private final ItemFactory itemFactory;
    private final MapItemLoader mapItemLoader;
    private final TileMap tileMap;

    public ItemManager(TileMap tileMap, ItemFactory itemFactory, MapItemLoader mapItemLoader) {
        this.items = new ArrayList<>();
        this.itemFactory = itemFactory;
        this.mapItemLoader = mapItemLoader;
        this.tileMap = tileMap;
    }

    /**
     * Carica tutti gli item per una determinata mappa da file.
     */
    public void loadItemsForMap(String mapName) {
        String itemFile = "item_" + mapName + ".txt";
        try {
            MapItemSpawner spawner = new MapItemSpawner(tileMap, itemFactory, mapItemLoader, itemFile);
            spawner.spawnItems();
            this.items = new ArrayList<>(spawner.getItems());
        } catch (Exception e) {
            System.err.println("[ItemManager] Nessun file " + itemFile + " trovato o errore di caricamento: " + e.getMessage());
            this.items = new ArrayList<>();
        }
    }

    /**
     * Aggiorna lo stato di tutti gli item (es. animazioni).
     */
    public void updateAll() {
        items.stream()
            .filter(item -> item instanceof Animatable)
            .map(item -> (Animatable) item)
            .forEach(Animatable::updateAnimation);
    }

    /**
     * Disegna tutti gli item.
     */
    public void renderAll(Graphics g) {
        items.forEach(item -> item.render(g));
    }

    /**
     * Gestisce la raccolta/interazione automatica degli item.
     */
    public void handleItemCollection(Bald bald, Inventory inventory) {
        items.removeIf(item -> {
            if (bald.getBounds().intersects(item.getBounds())) {
                
                if (item instanceof Chest) {
                    return false; // Le chest non si raccolgono automaticamente
                }
                if (item instanceof UsableItem) {
                    ((UsableItem) item).applyEffect(bald);
                    return true;
                }
                if (item instanceof InventoryItem) {
                    ((InventoryItem) item).onCollect(inventory);
                    return true;
                }
                if (item instanceof Trap) {
                    Trap trap = (Trap) item;
                    if (!trap.isTriggered()) {
                        trap.interact(bald, inventory);
                        if (trap.shouldRemoveOnTrigger()) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return false;
        });
    }

    public List<GameItem> getItems() {
        return items;
    }
}
