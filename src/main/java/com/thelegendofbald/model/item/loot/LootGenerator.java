package com.thelegendofbald.model.item.loot;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.model.item.ItemFactory;

/**
 * The LootGenerator class is responsible for generating random loot items
 * based on a predefined loot pool. It uses an {@link ItemFactory} to create
 * items and a {@link Random} instance to determine randomness.
 */
public class LootGenerator {

    private final ItemFactory itemFactory;
    private final Random random = new Random();

    private final List<Integer> lootPool;

    /**
     * Constructs a LootGenerator with the specified {@link ItemFactory} and loot pool.
     *
     * @param itemFactory the factory used to create items
     * @param lootPool    the list of item IDs that can be generated as loot
     */
    public LootGenerator(final ItemFactory itemFactory, final List<Integer> lootPool) {
        this.itemFactory = itemFactory;
        this.lootPool = Collections.unmodifiableList(lootPool);
    }

    /**
     * Generates a single random item from the loot pool.
     *
     * @param x the x-coordinate where the item will be placed
     * @param y the y-coordinate where the item will be placed
     * @return a {@link GameItem} created from the loot pool, or {@code null} if the loot pool is empty
     */
    public GameItem generateRandomItem(final int x, final int y) {
        if (lootPool.isEmpty()) {
            return null;
        }
        final int randomId = lootPool.get(random.nextInt(lootPool.size()));
        return itemFactory.createItemById(randomId, x, y);
    }
}
