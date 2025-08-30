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
    public ItemManager(final TileMap tileMap,
                       final ItemFactory itemFactory,
                       final MapItemLoader mapItemLoader,
                       final LootGenerator lootGenerator) {
        this.items = new ArrayList<>();
        this.itemFactory = itemFactory;
        this.mapItemLoader = mapItemLoader;
        this.tileMap = tileMap;
        this.lootGenerator = lootGenerator;
    }

    /**
     * Loads items for the specified map from a corresponding item file.
     * @param mapName the name of the map
     */
    public void loadItemsForMap(final String mapName) {
        final String itemFile = "item_" + mapName + ".txt";
        final MapItemSpawner spawner = new MapItemSpawner(tileMap, itemFactory, mapItemLoader, itemFile);
        spawner.spawnItems();
        this.items = new ArrayList<>(spawner.getItems());
    }

    /**
     * Updates all animatable items.
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
        final List<GameItem> snapshot = new ArrayList<>(items);
        snapshot.forEach(item -> item.render(g));
    }

    /**
     * Handles item collection when Bald intersects with items.
     * Chests are opened to generate loot, usable items apply effects and are removed,
     * and traps are triggered to apply their effects.
     * @param bald the Bald player character
     */
    public void handleItemCollection(final Bald bald) {
        final List<GameItem> newItems = new ArrayList<>();
        final Iterator<GameItem> it = items.iterator();

        while (it.hasNext()) {
            final GameItem item = it.next();

            if (bald.getBounds().intersects(item.getBounds())) {

                if (item instanceof Chest chest && !chest.isOpen()) {
                        chest.open(bald);
                        final GameItem loot = lootGenerator.generateRandomItem(
                            chest.getX() + chest.getWidth() + 5,
                            chest.getY()
                        );
                        if (loot != null) {
                            newItems.add(loot);
                        }
                } else if (item instanceof UsableItem usable) {
                    usable.applyEffect(bald);
                    it.remove();
                } else if (item instanceof Trap trap && !trap.isTriggered()) {
                    trap.interact(bald);
                    if (trap.shouldRemoveOnTrigger()) {
                        it.remove();
                    }
                }
            }
        }

        items.addAll(newItems);
    }

    /**
     * Returns the list of all current items managed by this ItemManager.
     * @return a list of all GameItem objects
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Adds a new item to the item list.
     * @param item the GameItem to add
     */
    public void addItem(final GameItem item) {
        items.add(item);
    }
}
