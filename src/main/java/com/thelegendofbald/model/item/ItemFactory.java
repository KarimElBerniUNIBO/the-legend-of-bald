package com.thelegendofbald.model.item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.thelegendofbald.model.item.potions.HealthPotion;
import com.thelegendofbald.model.item.potions.StrengthPotion;
import com.thelegendofbald.model.item.traps.ImmobilizingTrap;
import com.thelegendofbald.model.item.traps.PoisonTrap;

/**
 * A factory class responsible for creating different types of {@link GameItem}
 * based on an ID. This class follows the Factory design pattern,
 * centralizing item creation and promoting loose coupling.
 */
public class ItemFactory {

    private final Map<Integer, BiFunction<Integer, Integer, GameItem>> registry = new HashMap<>();

    /**
     * Constructs a new ItemFactory and registers all available item types.
     * The registry maps an integer ID to a constructor for the corresponding item class.
     */
    public ItemFactory() {
        // Register item IDs with their respective constructors.
        registry.put(7, HealthPotion::new);
        registry.put(8, StrengthPotion::new);
        
        // Pass the required key ID to the Chest and Key constructors.
        // This decouples the factory from the Bald instance.
        registry.put(9, (x, y) -> new Chest(x, y, 8)); // Chest requiring key ID 8
        registry.put(10, (x, y) -> new Key(x, y, 8)); // Key with ID 8
        registry.put(11, (x, y) -> new PoisonTrap(x, y));
        registry.put(12, (x, y) -> new ImmobilizingTrap(x, y));
    }

    /**
     * Creates a new instance of a {@link GameItem} based on its ID.
     *
     * @param id The unique ID of the item to create.
     * @param x The x-coordinate for the new item.
     * @param y The y-coordinate for the new item.
     * @return A new {@link GameItem} instance, or null if the ID is not found.
     */
    public GameItem createItemById(int id, int x, int y) {
        BiFunction<Integer, Integer, GameItem> constructor = registry.get(id);
        if (constructor == null) {
            return null;
        }
        return constructor.apply(x, y);
    }
}