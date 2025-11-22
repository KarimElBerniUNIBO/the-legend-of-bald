package com.thelegendofbald.view.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;
import javax.swing.JPanel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Headless test class for {@link GridPanel}.
 * Verifies dynamic sizing behavior and custom painting logic without requiring a visible UI.
 */
class GridPanelTest {

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 704;

    private static final int PARENT_WIDTH = 500;
    private static final int PARENT_HEIGHT = 400;
    private static final int CHILD_BOUNDS_SIZE = 100;

    private static final int GRID_SPACING = 32;
    private static final int DOUBLE_GRID = 64;

    // Dimensions for the paint test (enough to cover 0, 32, 64)
    private static final int TEST_CANVAS_SIZE = 90;

    // Offset to sample pixels avoiding intersection points
    private static final int SAMPLE_OFFSET = 10;

    // Alpha channel shift for ARGB color integers
    private static final int ALPHA_SHIFT = 24;
    private static final int ALPHA_MASK = 0xFF;

    /**
     * Verifies that {@code getPreferredSize()} returns the default dimensions
     * when the panel has no parent container.
     */
    @Test
    @DisplayName("getPreferredSize() without parent returns default dimension")
    void preferredSizeWithoutParentReturnsDefault() {
        final GridPanel panel = new GridPanel();
        assertEquals(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT), panel.getPreferredSize());
    }

    /**
     * Verifies that {@code getPreferredSize()} adapts to match the parent container's size
     * when the panel is added to one.
     */
    @Test
    @DisplayName("getPreferredSize() with parent matches parent size")
    void preferredSizeWithParentMatchesParentSize() {
        final JPanel parent = new JPanel(null);
        parent.setSize(PARENT_WIDTH, PARENT_HEIGHT);

        final GridPanel panel = new GridPanel();
        panel.setBounds(0, 0, CHILD_BOUNDS_SIZE, CHILD_BOUNDS_SIZE);
        parent.add(panel);

        assertSame(parent, panel.getParent(), "Parent should be assigned");
        assertEquals(new Dimension(PARENT_WIDTH, PARENT_HEIGHT), panel.getPreferredSize(),
                "Preferred size should match parent size when attached");
    }

    /**
     * Performs a headless rendering test to verify that grid lines are drawn
     * at the correct intervals (every 32 pixels).
     */
    @Test
    @DisplayName("paintComponent() draws grid lines every 32px")
    void paintDrawsGridCorrectly() {
        final GridPanel panel = new GridPanel();
        panel.setOpaque(false);
        panel.setSize(TEST_CANVAS_SIZE, TEST_CANVAS_SIZE);

        final BufferedImage img = new BufferedImage(TEST_CANVAS_SIZE, TEST_CANVAS_SIZE,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = img.createGraphics();

        panel.paintComponent(g2);
        g2.dispose();

        // Helper function to extract the Alpha component (0..255) of a pixel
        final BiFunction<Integer, Integer, Integer> alphaAt = (x, y) ->
                (img.getRGB(x, y) >>> ALPHA_SHIFT) & ALPHA_MASK;

        // 1. Verify Vertical Lines (at x = 0, 32, 64)
        // We check at y = SAMPLE_OFFSET to avoid crossing horizontal lines
        assertTrue(alphaAt.apply(0, SAMPLE_OFFSET) > 0,
                "Vertical line at x=0 should be drawn");
        assertTrue(alphaAt.apply(GRID_SPACING, SAMPLE_OFFSET) > 0,
                "Vertical line at x=32 should be drawn");
        assertTrue(alphaAt.apply(DOUBLE_GRID, SAMPLE_OFFSET) > 0,
                "Vertical line at x=64 should be drawn");

        // Verify gaps (pixels next to lines should be transparent)
        assertEquals(0, alphaAt.apply(1, SAMPLE_OFFSET),
                "Pixel at x=1 should be transparent");
        assertEquals(0, alphaAt.apply(GRID_SPACING - 1, SAMPLE_OFFSET),
                "Pixel at x=31 should be transparent");
        assertEquals(0, alphaAt.apply(GRID_SPACING + 1, SAMPLE_OFFSET),
                "Pixel at x=33 should be transparent");

        // 2. Verify Horizontal Lines (at y = 0, 32, 64)
        // We check at x = SAMPLE_OFFSET
        assertTrue(alphaAt.apply(SAMPLE_OFFSET, 0) > 0,
                "Horizontal line at y=0 should be drawn");
        assertTrue(alphaAt.apply(SAMPLE_OFFSET, GRID_SPACING) > 0,
                "Horizontal line at y=32 should be drawn");
        assertTrue(alphaAt.apply(SAMPLE_OFFSET, DOUBLE_GRID) > 0,
                "Horizontal line at y=64 should be drawn");

        // Verify gaps for horizontal lines
        assertEquals(0, alphaAt.apply(SAMPLE_OFFSET, 1),
                "Pixel at y=1 should be transparent");
        assertEquals(0, alphaAt.apply(SAMPLE_OFFSET, GRID_SPACING - 1),
                "Pixel at y=31 should be transparent");
        assertEquals(0, alphaAt.apply(SAMPLE_OFFSET, GRID_SPACING + 1),
                "Pixel at y=33 should be transparent");
    }
}
