package com.thelegendofbald.view.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.thelegendofbald.view.render.Tile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Tile} class.
 * Verifies constructor logic, defensive copying of images, resizing behavior,
 * rendering safety, and equality contracts.
 */
class TileTest {

    private static final int WIDTH_10 = 10;
    private static final int HEIGHT_12 = 12;

    private static final int SMALL_SIZE = 8;
    private static final int TARGET_WIDTH_32 = 32;
    private static final int TARGET_HEIGHT_24 = 24;
    private static final int OVERLAY_SIZE = 16;

    private static final int ID_DEFAULT = 0;
    private static final int ID_TEST = 7;
    private static final int ID_NO_ID = -1;
    private static final int ID_EQUALITY = 42;

    private static final int RECT_SIZE = 5;
    private static final int PIXEL_CHECK_X = 2;
    private static final int PIXEL_CHECK_Y = 2;
    private static final int RENDER_X = 10;
    private static final int RENDER_Y = 10;

    /**
     * Helper method to create a solid color BufferedImage for testing.
     *
     * @param width  the width of the image
     * @param height the height of the image
     * @param color  the fill color
     * @return a new BufferedImage instance
     */
    private static BufferedImage makeImg(final int width, final int height, final Color color) {
        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return img;
    }

    /**
     * Verifies that the simplified constructor correctly initializes dimensions,
     * defaults flags to false, and performs defensive copying of the source image.
     */
    @Test
    @DisplayName("Simplified Constructor: validates dimensions and defensive copying")
    void simpleConstructorBuildsAndCopiesCorrectly() {
        final BufferedImage src = makeImg(WIDTH_10, HEIGHT_12, Color.RED);
        final Tile tile = new Tile(src, WIDTH_10, HEIGHT_12);

        assertEquals(WIDTH_10, tile.getWidth());
        assertEquals(HEIGHT_12, tile.getHeight());
        assertEquals(ID_DEFAULT, tile.getId());
        assertFalse(tile.isSolid());
        assertFalse(tile.isSpawn());
        assertFalse(tile.isWalkable());

        final BufferedImage got1 = tile.getImage();
        final BufferedImage got2 = tile.getImage();

        assertNotNull(got1);
        assertNotSame(src, got1, "Getter should return a copy, not the original reference");
        assertNotSame(got1, got2, "Subsequent calls should return new copies");

        final Graphics2D g = got1.createGraphics();
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, RECT_SIZE, RECT_SIZE);
        g.dispose();

        final BufferedImage fresh = tile.getImage();
        final int rgbFresh = fresh.getRGB(PIXEL_CHECK_X, PIXEL_CHECK_Y);
        assertEquals(Color.RED.getRGB(), rgbFresh, "Internal image should remain modified");
    }

    /**
     * Verifies the behavior of the main constructor regarding the 'resize' flag.
     * If true, the internal image matches the target dimensions.
     * If false, the internal image retains original size.
     */
    @Test
    @DisplayName("Main Constructor: validates resize behavior")
    void mainConstructorHandlesResizeFlag() {
        final BufferedImage src = makeImg(SMALL_SIZE, SMALL_SIZE, Color.BLUE);

        final Tile resized = new Tile(src, TARGET_WIDTH_32, TARGET_HEIGHT_24, ID_TEST,
                true, true, false, true, null);

        assertFalse(resized.isResizeable(), "Should not be resizeable if already resized");
        assertEquals(TARGET_WIDTH_32, resized.getImage().getWidth());
        assertEquals(TARGET_HEIGHT_24, resized.getImage().getHeight());
        assertEquals(ID_TEST, resized.getId());
        assertTrue(resized.isSolid());
        assertFalse(resized.isSpawn());
        assertTrue(resized.isWalkable());
        assertTrue(resized.hasId());

        final Tile notResized = new Tile(src, TARGET_WIDTH_32, TARGET_HEIGHT_24, ID_NO_ID,
                false, false, false, false, null);

        assertTrue(notResized.isResizeable(), "Should be resizeable if size differs");
        assertEquals(SMALL_SIZE, notResized.getImage().getWidth());
        assertEquals(SMALL_SIZE, notResized.getImage().getHeight());
        assertFalse(notResized.hasId());
    }

    /**
     * Verifies that the overlay image is also defensively copied and protected
     * from external modification via getters.
     */
    @Test
    @DisplayName("Overlay: verifies defensive copying of overlay image")
    void overlayIsDefensivelyCopied() {
        final BufferedImage base = makeImg(OVERLAY_SIZE, OVERLAY_SIZE, Color.GRAY);
        final BufferedImage overlay = makeImg(OVERLAY_SIZE, OVERLAY_SIZE, Color.YELLOW);

        final Tile tile = new Tile(base, OVERLAY_SIZE, OVERLAY_SIZE, ID_TEST,
                false, false, true, true, overlay);

        final BufferedImage gotOverlay1 = tile.getOverlayImage();
        final BufferedImage gotOverlay2 = tile.getOverlayImage();

        assertNotNull(gotOverlay1);
        assertNotSame(overlay, gotOverlay1);
        assertNotSame(gotOverlay1, gotOverlay2);

        final Graphics2D g = gotOverlay1.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, RECT_SIZE - 1, RECT_SIZE - 1);
        g.dispose();

        final BufferedImage fresh = tile.getOverlayImage();
        assertEquals(Color.YELLOW.getRGB(), fresh.getRGB(PIXEL_CHECK_X, PIXEL_CHECK_Y),
                "Internal overlay should not be affected by external modifications");
    }

    /**
     * Ensures that the {@code render} method executes without throwing exceptions,
     * both when images are present and when they are null.
     */
    @Test
    @DisplayName("Render: executes safely with or without images")
    void renderExecutesWithoutThrowing() {

        final Tile withImages = new Tile(makeImg(SMALL_SIZE, SMALL_SIZE, Color.RED),
                SMALL_SIZE, SMALL_SIZE, 1, false, false, false, true,
                makeImg(SMALL_SIZE, SMALL_SIZE, Color.GREEN));

        final BufferedImage canvas1 = new BufferedImage(TARGET_WIDTH_32, TARGET_WIDTH_32,
                BufferedImage.TYPE_INT_ARGB);

        assertDoesNotThrow(() -> {
            withImages.render(canvas1.createGraphics(), RENDER_X, RENDER_Y);
        });


        final Tile noImages = new Tile(null, SMALL_SIZE, SMALL_SIZE, 2,
                false, false, false, false, null);

        final BufferedImage canvas2 = new BufferedImage(TARGET_WIDTH_32, TARGET_WIDTH_32,
                BufferedImage.TYPE_INT_ARGB);

        assertDoesNotThrow(() -> {
            noImages.render(canvas2.createGraphics(), 0, 0);
        });
    }

    /**
     * Verifies that {@code equals} and {@code hashCode} are implemented based solely
     * on the Tile ID.
     */
    @Test
    @DisplayName("equals() and hashCode() are based on ID only")
    void equalsAndHashCodeAreBasedOnId() {
        final Tile a = new Tile(makeImg(RECT_SIZE, RECT_SIZE, Color.RED),
                RECT_SIZE, RECT_SIZE, ID_EQUALITY, false, false, false, false, null);

        final Tile b = new Tile(makeImg(SMALL_SIZE, SMALL_SIZE, Color.BLUE),
                SMALL_SIZE, SMALL_SIZE, ID_EQUALITY, true, true, true, true,
                makeImg(SMALL_SIZE, SMALL_SIZE, Color.CYAN));

        final Tile c = new Tile(makeImg(RECT_SIZE, RECT_SIZE, Color.RED),
                RECT_SIZE, RECT_SIZE, ID_TEST, false, false, false, false, null);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());

        assertNotEquals(a, null);
        assertNotEquals(a, "not a tile");
    }
}
