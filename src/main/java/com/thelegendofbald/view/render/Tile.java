package com.thelegendofbald.view.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Represents a map tile, composed of a base image and an optional overlay
 * image.
 * <p>
 * The class is immutable and <b>final</b>. It automatically handles defensive
 * copying of images
 * to prevent accidental external modifications and supports resizing during
 * initialization.
 * </p>
 */
public final class Tile {

    private final BufferedImage image;
    private final BufferedImage overlayImage;
    private final int width;
    private final int height;
    private final int id;
    private final boolean solid;
    private final boolean isSpawn;
    private final boolean walkable;

    /**
     * Main constructor to create a new Tile with all configurable properties.
     *
     * @param image        base image of the tile (can be {@code null})
     * @param width        desired width of the tile in pixels
     * @param height       desired height of the tile in pixels
     * @param id           unique identifier of the tile type (use -1 if not
     *                     applicable)
     * @param solid        {@code true} if the tile should block movement
     *                     (collision)
     * @param resize       {@code true} to force resizing of the image to the
     *                     specified dimensions
     * @param isSpawn      {@code true} if this tile represents a spawn point
     * @param walkable     {@code true} if the tile is walkable by entities
     * @param overlayImage optional image to draw over the base one (can be
     *                     {@code null})
     */
    public Tile(
            final BufferedImage image,
            final int width,
            final int height,
            final int id,
            final boolean solid,
            final boolean resize,
            final boolean isSpawn,
            final boolean walkable,
            final BufferedImage overlayImage) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.solid = solid;
        this.isSpawn = isSpawn;
        this.walkable = walkable;

        if (image != null) {
            if (resize) {
                final BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                final Graphics2D g2 = resized.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.drawImage(image, 0, 0, width, height, null);
                g2.dispose();
                this.image = resized;
            } else {
                this.image = deepCopy(image);
            }
        } else {
            this.image = null;
        }

        this.overlayImage = overlayImage != null ? deepCopy(overlayImage) : null;
    }

    /**
     * Simplified constructor to create a basic non-solid tile without special
     * properties.
     *
     * @param image  base image of the tile
     * @param width  width of the tile
     * @param height height of the tile
     */
    public Tile(final BufferedImage image, final int width, final int height) {
        this(image, width, height, 0, false, false, false, false, null);
    }

    /**
     * Returns a copy of the base image of the tile.
     *
     * @return a new {@link BufferedImage} instance or {@code null} if not present
     */
    public BufferedImage getImage() {
        return image == null ? null : deepCopy(image);
    }

    /**
     * Returns the width of the tile.
     *
     * @return width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the tile.
     *
     * @return height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the numeric identifier of the tile type.
     *
     * @return the tile ID
     */
    public int getId() {
        return id;
    }

    /**
     * Indicates if the tile is solid (blocking for collisions).
     *
     * @return {@code true} if solid, otherwise {@code false}
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * Indicates if the tile is a spawn point.
     *
     * @return {@code true} if it is a spawn point, otherwise {@code false}
     */
    public boolean isSpawn() {
        return isSpawn;
    }

    /**
     * Indicates if the tile is walkable/traversable.
     *
     * @return {@code true} if walkable, otherwise {@code false}
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * Checks if the tile has a valid ID.
     *
     * @return {@code true} if the ID is different from -1
     */
    public boolean hasId() {
        return id != -1;
    }

    /**
     * Returns a copy of the overlay image.
     *
     * @return a new {@link BufferedImage} instance or {@code null} if not present
     */
    public BufferedImage getOverlayImage() {
        return overlayImage == null ? null : deepCopy(overlayImage);
    }

    /**
     * Renders the tile (base image and overlay) at the specified coordinates.
     *
     * @param g graphics context to draw on
     * @param x destination x coordinate
     * @param y destination y coordinate
     */
    public void render(final Graphics g, final int x, final int y) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
        if (overlayImage != null) {
            g.drawImage(overlayImage, x, y, null);
        }
    }

    /**
     * Checks equality between two tiles based exclusively on their ID.
     *
     * @param obj the object to compare
     * @return {@code true} if objects have the same ID
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tile)) {
            return false;
        }
        final Tile other = (Tile) obj;
        return this.id == other.id;
    }

    /**
     * Calculates the hash code based on the tile ID.
     *
     * @return the hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Checks if the current image dimensions differ from the target tile
     * dimensions.
     *
     * @return {@code true} if the image needs resizing
     */
    public boolean isResizeable() {
        return image != null && (image.getWidth() != width || image.getHeight() != height);
    }

    /**
     * Returns a string representation of the tile state.
     *
     * @return descriptive string
     */
    @Override
    public String toString() {
        return new StringBuilder("Tile{")
                .append("id=").append(id)
                .append(", solid=").append(solid)
                .append(", isSpawn=").append(isSpawn)
                .append(", walkable=").append(walkable)
                .append('}')
                .toString();
    }

    /**
     * Creates a deep copy of a BufferedImage to ensure immutability.
     *
     * @param src source image
     * @return a new BufferedImage containing the same data as the source
     */
    private static BufferedImage deepCopy(final BufferedImage src) {
        final int type = src.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : src.getType();
        final BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), type);
        final Graphics2D g = copy.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return copy;
    }
}
