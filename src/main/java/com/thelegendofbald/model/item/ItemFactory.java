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

    private static final int ID_HEALTH_POTION = 7;
    private static final int ID_STRENGTH_POTION = 8;
    private static final int ID_COIN = 9;
    private static final int ID_CHEST = 10;
    private static final int ID_POISON_TRAP = 11;
    private static final int ID_IMMOBILIZING_TRAP = 12;

    private final Map<Integer, BiFunction<Integer, Integer, GameItem>> registry = new HashMap<>();

    /**
     * Constructs a new ItemFactory and registers all available item types.
     * The registry maps an integer ID to a constructor for the corresponding item class.
     */
    public ItemFactory() {
        registry.put(ID_HEALTH_POTION, HealthPotion::new);
        registry.put(ID_STRENGTH_POTION, StrengthPotion::new);
        registry.put(ID_CHEST, (x, y) -> new Chest(x, y));
        registry.put(ID_POISON_TRAP, (x, y) -> new PoisonTrap(x, y));
        registry.put(ID_IMMOBILIZING_TRAP, (x, y) -> new ImmobilizingTrap(x, y));
        registry.put(ID_COIN, Coin::new);
    }

    /**
     * Creates a new instance of a {@link GameItem} based on its ID.
     *
     * @param id The unique ID of the item to create.
     * @param x  The x-coordinate for the new item.
     * @param y  The y-coordinate for the new item.
     * @return A new {@link GameItem} instance, or null if the ID is not found.
     */
    public GameItem createItemById(final int id, final int x, final int y) {
        final BiFunction<Integer, Integer, GameItem> constructor = registry.get(id);
        if (constructor == null) {
            return null;
        }
        return constructor.apply(x, y);
    }
}
