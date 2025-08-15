package com.thelegendofbald.model.item;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.thelegendofbald.api.common.animation.Animatable;
import com.thelegendofbald.api.interactable.Interactable;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.model.item.loot.LootGenerator;
import com.thelegendofbald.model.item.map.MapItemLoader;
import com.thelegendofbald.model.item.map.MapItemSpawner;
import com.thelegendofbald.model.item.traps.Trap;
import com.thelegendofbald.view.main.TileMap;

public class ItemManager {

    private List<GameItem> items;
    private final ItemFactory itemFactory;
    private final MapItemLoader mapItemLoader;
    private final TileMap tileMap;
    private final LootGenerator lootGenerator;

    public ItemManager(TileMap tileMap, ItemFactory itemFactory, MapItemLoader mapItemLoader, LootGenerator lootGenerator) {
        this.items = new ArrayList<>();
        this.itemFactory = itemFactory;
        this.mapItemLoader = mapItemLoader;
        this.tileMap = tileMap;
        this.lootGenerator = lootGenerator;
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
    public void handleItemCollection(Bald bald) {
        List<GameItem> newItems = new ArrayList<>();
    
        items.removeIf(item -> {
            if (bald.getBounds().intersects(item.getBounds())) {
    
                if (item instanceof Chest chest) {
                    if (!chest.isOpen()) {
                        chest.open(bald);
                        GameItem loot = lootGenerator.generateRandomItem(
                                chest.getX() + chest.getWidth() + 5,
                                chest.getY()
                        );
                        if (loot != null) {
                            newItems.add(loot);
                        }
                    }
                    return false;
                }
    
                if (item instanceof UsableItem usable) {
                    usable.applyEffect(bald);
                    return true;
                }
    
                if (item instanceof Trap trap) {
                    if (!trap.isTriggered()) {
                        trap.interact(bald);
                        if (trap.shouldRemoveOnTrigger()) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return false;
        });
    
        // Aggiungiamo il loot dopo la rimozione, per evitare ConcurrentModificationException
        items.addAll(newItems);
    }
    


    public List<GameItem> getItems() {
        return items;
    }

    public void addItem(GameItem item) {
        items.add(item);
    }
    
}
