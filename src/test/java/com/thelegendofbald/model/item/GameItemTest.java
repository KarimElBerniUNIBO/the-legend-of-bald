package com.thelegendofbald.model.item;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.model.item.GameItem;

/**
 * Unit tests for the {@link GameItem} class.
 */
class GameItemTest {

    private GameItem testItem;
    private static final int X = 10;
    private static final int Y = 20;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 40;
    private static final String NAME = "TestItem";

    @BeforeEach
    void setUp() {
        // Create a new GameItem before each test.
        testItem = new GameItem(X, Y, WIDTH, HEIGHT, NAME);
    }

    @Test
    void testConstructorAndGetters() {
        // Assert that the constructor correctly initializes the properties.
        assertEquals(X, testItem.getX(), "X coordinate should be correctly initialized.");
        assertEquals(Y, testItem.getY(), "Y coordinate should be correctly initialized.");
        assertEquals(WIDTH, testItem.getWidth(), "Width should be correctly initialized.");
        assertEquals(HEIGHT, testItem.getHeight(), "Height should be correctly initialized.");
        assertEquals(NAME, testItem.getName(), "Name should be correctly initialized.");
        assertEquals(0, testItem.getPrice(), "Default price should be 0.");
        assertEquals("", testItem.getDescription(), "Default description should be an empty string.");
    }

    @Test
    void testSetters() {
        // Test if setters correctly update the item's properties.
        final int newPrice = 100;
        final String newDescription = "A description.";

        testItem.setPrice(newPrice);
        testItem.setDescription(newDescription);

        assertEquals(newPrice, testItem.getPrice(), "Price should be updated by the setter.");
        assertEquals(newDescription, testItem.getDescription(), "Description should be updated by the setter.");
    }

    @Test
    void testGetBounds() {
        // Test if getBounds returns a correct Rectangle object.
        Rectangle bounds = testItem.getBounds();
        assertNotNull(bounds, "The bounds rectangle should not be null.");
        assertEquals(X, bounds.x, "Bounds x-coordinate should match item's x.");
        assertEquals(Y, bounds.y, "Bounds y-coordinate should match item's y.");
        assertEquals(WIDTH, bounds.width, "Bounds width should match item's width.");
        assertEquals(HEIGHT, bounds.height, "Bounds height should match item's height.");
    }

    // Note: The `render` and `loadImage` methods are visual/side-effect based,
    // so they are typically not unit-tested directly in this manner.
}