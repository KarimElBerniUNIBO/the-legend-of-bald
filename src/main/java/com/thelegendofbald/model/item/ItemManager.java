package com.thelegendofbald.model.item;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thelegendofbald.api.common.animation.Animatable;
import com.thelegendofbald.api.item.UsableItem;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.model.item.loot.LootGenerator;
import com.thelegendofbald.model.item.map.MapItemLoader;
import com.thelegendofbald.model.item.map.MapItemSpawner;
import com.thelegendofbald.model.item.traps.Trap;
import com.thelegendofbald.view.main.TileMap;

/**
 * Manages all game items, including loading, updating, rendering,
 * and handling interactions with the player character (Bald).
 * This class utilizes an {@link ItemFactory} to create items,
 * a {@link MapItemLoader} to load items from files,
 * and a {@link LootGenerator} to generate loot from chests.
 * It also handles automatic item collection when Bald intersects with items.
 */
public class ItemManager {

    private List<GameItem> items;
    private final ItemFactory itemFactory;
    private final MapItemLoader mapItemLoader;
    private final TileMap tileMap;
    private final LootGenerator lootGenerator;

    /**
     * Constructs an ItemManager with the specified dependencies.
     *
     * @param tileMap        the tile map where items are placed
     * @param itemFactory    the factory used to create items
     * @param mapItemLoader  the loader used to load items from files
     * @param lootGenerator  the generator used to create loot from chests
     */
    public ItemManager(final TileMap tileMap,final ItemFactory itemFactory,final MapItemLoader mapItemLoader,final LootGenerator lootGenerator) {
        this.items = new ArrayList<>();
        this.itemFactory = itemFactory;
        this.mapItemLoader = mapItemLoader;
        this.tileMap = tileMap;
        this.lootGenerator = lootGenerator;
    }

    /**
     * Load items for the specified map from a corresponding item file.
     * If the file does not exist or cannot be loaded, initializes with an empty item list.
     * @param mapName the name of the map to load items for
     * @throws IOException if there is an error reading the item file
     */
    public void loadItemsForMap(final String mapName) {
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
     * Updates all animatable items.
     * Items that implement the {@link Animatable} interface will have their animations updated.
     * This method should be called in the game loop to ensure smooth animations.
     */
    public void updateAll() {
        items.stream()
            .filter(item -> item instanceof Animatable)
            .map(item -> (Animatable) item)
            .forEach(Animatable::updateAnimation);
    }

    /**
     * Renders all items onto the provided Graphics context.
     * @param g the Graphics context to draw on
     */
    public void renderAll(final Graphics g) {
        items.forEach(item -> item.render(g));
    }

    /**
     * Handles item collection when Bald intersects with items.
     * Chests are opened to generate loot, usable items apply their effects and are removed,
     * and traps are triggered to apply their effects.
     * @param bald the Bald {@code Bald} player character who may collect items
     */
    public void handleItemCollection(final Bald bald) {
        List<GameItem> newItems = new ArrayList<>();
        Iterator<GameItem> it = items.iterator();

        while (it.hasNext()) {
            GameItem item = it.next();
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
                } 
                else if (item instanceof UsableItem usable) {
                    usable.applyEffect(bald);
                    it.remove(); 
                } 
                else if (item instanceof Trap trap) {
                    if (!trap.isTriggered()) {
                        trap.interact(bald);
                        if (trap.shouldRemoveOnTrigger()) {
                            it.remove();
                        }
                    }
                }
            }
        }
        items.addAll(newItems);
    }
    
    /**
     * Returns the list of all current items managed by this ItemManager.
     * @return a list of all {@link GameItem} objects
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Adds a new item to the item list.
     * @param item the {@link GameItem} to add
     */
    public void addItem(final GameItem item) {
        items.add(item);
    }
    
}
