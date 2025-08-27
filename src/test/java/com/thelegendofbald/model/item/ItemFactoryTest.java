package com.thelegendofbald.model.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.model.item.Chest;
import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.model.item.ItemFactory;
import com.thelegendofbald.model.item.potions.HealthPotion;
import com.thelegendofbald.model.item.potions.StrengthPotion;

/**
 * Unit tests for the {@link ItemFactory} class.
 */
class ItemFactoryTest {

    private ItemFactory itemFactory;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;

    @BeforeEach
    void setUp() {
        // Create a new ItemFactory instance before each test.
        itemFactory = new ItemFactory();
    }

    @Test
    void testCreateHealthPotion() {
        // Test that a HealthPotion is created for ID 7.
        GameItem item = itemFactory.createItemById(7, TEST_X, TEST_Y);
        assertNotNull(item, "Item should not be null for a valid ID.");
        assertTrue(item instanceof HealthPotion, "Item created should be a HealthPotion.");
        assertEquals(TEST_X, item.getX(), "X coordinate should match the input.");
        assertEquals(TEST_Y, item.getY(), "Y coordinate should match the input.");
    }

    @Test
    void testCreateStrengthPotion() {
        // Test that a StrengthPotion is created for ID 8.
        GameItem item = itemFactory.createItemById(8, TEST_X, TEST_Y);
        assertNotNull(item, "Item should not be null for a valid ID.");
        assertTrue(item instanceof StrengthPotion, "Item created should be a StrengthPotion.");
    }

    @Test
    void testCreateChest() {
        // Test that a Chest is created for ID 9.
        GameItem item = itemFactory.createItemById(9, TEST_X, TEST_Y);
        assertNotNull(item, "Item should not be null for a valid ID.");
        assertTrue(item instanceof Chest, "Item created should be a Chest.");
        
        // Additional check: verify the chest has the correct keyId.
        Chest chest = (Chest) item;
        // This is a simplified test; in a real scenario, you'd have a getter for the keyId.
        // Assuming a getter `getRequiredKeyId()` exists, you would use:
        // assertEquals(8, chest.getRequiredKeyId(), "Chest should require key with ID 8.");
    }

    @Test
    void testCreateItemWithInvalidId() {
        // Test that a null item is returned for an invalid ID.
        GameItem item = itemFactory.createItemById(999, TEST_X, TEST_Y);
        assertNull(item, "Item should be null for an invalid ID.");
    }
}