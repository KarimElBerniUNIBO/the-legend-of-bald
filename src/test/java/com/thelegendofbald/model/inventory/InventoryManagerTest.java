package com.thelegendofbald.model.inventory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.item.weapons.Axe;
import com.thelegendofbald.item.weapons.Magic;
import com.thelegendofbald.item.weapons.Sword;
import com.thelegendofbald.model.weapons.Weapon;

class InventoryManagerTest {

    private InventoryManager inventoryManager;
    private final int ROWS = 2;
    private final int COLS = 2;

    @BeforeEach
    void setUp() {
        inventoryManager = new InventoryManager(ROWS, COLS);
    }

    @Test
    void testAddWeapon() {
        Weapon weapon = new Sword(0, 0, 0, 0, null);
        inventoryManager.add(weapon);
        boolean found = inventoryManager.getSlots().stream()
                .anyMatch(slot -> slot.getItem().isPresent() && slot.getItem().get() == weapon);
        assertTrue(found, "Weapon should be present in inventory after adding");
    }

    @Test
    void testRemoveWeapon() {
        Weapon weapon = new Axe(0, 0, 0, 0, null);
        inventoryManager.set(weapon, 0, 0);
        inventoryManager.remove(0, 0);
        assertFalse(inventoryManager.get(0, 0).getItem().isPresent(), "Slot should be empty after removal");
    }

    @Test
    void testSetWeaponAtSpecificPosition() {
        Weapon weapon = new Magic(0, 0, 0, 0, null);
        inventoryManager.set(weapon, 1, 1);
        Optional<GameItem> item = inventoryManager.get(1, 1).getItem();
        assertTrue(item.isPresent(), "Item should be present at (1,1)");
        assertEquals(weapon, item.get(), "The item at (1,1) should be the weapon set");
    }

    @Test
    void testClearInventory() {
        Weapon weapon = new Sword(0, 0, 0, 0, null);
        inventoryManager.set(weapon, 0, 0);
        inventoryManager.set(weapon, 1, 1);
        inventoryManager.clear();
        boolean allEmpty = inventoryManager.getSlots().stream()
                .allMatch(slot -> slot.getItem().isEmpty());
        assertTrue(allEmpty, "All slots should be empty after clearing inventory");
    }
}
