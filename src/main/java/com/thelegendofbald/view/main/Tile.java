package com.thelegendofbald.view.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Tile {
    private final BufferedImage image;
    private final int width;
    private final int height;
    private final int id;
    private final boolean solid;
    private final boolean isSpawn;  
    private final boolean walkable;

    // Costruttore principale
    public Tile(BufferedImage image, int width, int height, int id, boolean solid, boolean resize, boolean isSpawn, boolean walkable) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.solid = solid;
        this.isSpawn = isSpawn;
        this.walkable = walkable;
        if (image != null) {
            if (resize) {
                this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = this.image.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.drawImage(image, 0, 0, width, height, null);
                g.dispose();
            } else {
                this.image = image;
            }
        } else {
            this.image = null;
        }
    }

    // Costruttore secondario semplificato
    public Tile(BufferedImage image, int width, int height) {
        this(image, width, height, 0, false, false, false, false);
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isSpawn() {
        return isSpawn;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean hasId() {
        return id != -1;
    }

    public void render(Graphics g, int x, int y) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        } 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tile)) return false;
        Tile other = (Tile) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isResizeable() {
        return image != null && (image.getWidth() != width || image.getHeight() != height);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "id=" + id +
                ", solid=" + solid +
                ", isSpawn=" + isSpawn +
                ", walkable=" + walkable +
                '}';
    }

    public boolean isDummy() {
        return id == -1; // Considera un tile senza ID come un "dummy"
    }
    public boolean isEmpty() {
        return image == null || (width == 0 && height == 0);
    }
    public boolean isValid() {
        return image != null && width > 0 && height > 0;
    }
    public boolean isValidTile() {
        return isValid() && !isDummy() && !isEmpty();
    }
    public boolean isSolidTile() {
        return isValidTile() && solid;
    }
    public boolean isSpawnTile() {
        return isValidTile() && isSpawn;
    }
    public boolean isWalkableTile() {
        return isValidTile() && walkable;
    }
    public boolean isNonWalkableTile() {
        return isValidTile() && !walkable;
    }
    public boolean isSpawnOrWalkableTile() {
        return isValidTile() && (isSpawn || walkable);
    }
    public boolean isSpawnAndWalkableTile() {
        return isValidTile() && isSpawn && walkable;
    }
    public boolean isSpawnOrSolidTile() {
        return isValidTile() && (isSpawn || solid);
    }
    public boolean isSpawnAndSolidTile() {
        return isValidTile() && isSpawn && solid;
    }
    public boolean isSpawnOrSolidOrWalkableTile() {
        return isValidTile() && (isSpawn || solid || walkable);
    }
    public boolean isSpawnAndSolidAndWalkableTile() {
        return isValidTile() && isSpawn && solid && walkable;
    }
    public boolean isSpawnOrSolidAndWalkableTile() {
        return isValidTile() && (isSpawn || (solid && walkable));
    }
    public boolean isSpawnAndSolidOrWalkableTile() {
        return isValidTile() && (isSpawn && solid) || walkable;
    }
    public boolean isSpawnOrSolidAndNonWalkableTile() {
        return isValidTile() && (isSpawn || (solid && !walkable));
    }
    public boolean isSpawnAndSolidOrNonWalkableTile() {
        return isValidTile() && (isSpawn && solid) || !walkable;
    }
    public boolean isSpawnOrWalkableAndNonSolidTile() {
        return isValidTile() && (isSpawn || (walkable && !solid));
    }
    public boolean isSpawnAndWalkableOrNonSolidTile() {
        return isValidTile() && (isSpawn && walkable) || !solid;
    }
      
}
