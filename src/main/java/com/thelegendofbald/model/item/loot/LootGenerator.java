package com.thelegendofbald.model.item.loot;

import java.util.List;
import java.util.Random;

import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.model.item.ItemFactory;

public class LootGenerator {

    private final ItemFactory itemFactory;
    private final Random random = new Random();

    // Lista di ID item che possono uscire come loot
    private final List<Integer> lootPool;

    public LootGenerator(ItemFactory itemFactory, List<Integer> lootPool) {
        this.itemFactory = itemFactory;
        this.lootPool = lootPool;
    }

    /**
     * Genera un singolo item casuale preso dalla loot pool.
     */
    public GameItem generateRandomItem(int x, int y) {
        if (lootPool.isEmpty()) {
            return null;
        }
        int randomId = lootPool.get(random.nextInt(lootPool.size()));
        return itemFactory.createItemById(randomId, x, y);
    }

    /**
     * Genera N item casuali dalla loot pool.
     */
    public List<GameItem> generateRandomItems(int count, int x, int y) {
        return random.ints(count, 0, lootPool.size())
                     .mapToObj(index -> itemFactory.createItemById(lootPool.get(index), x, y))
                     .toList();
    }
}
