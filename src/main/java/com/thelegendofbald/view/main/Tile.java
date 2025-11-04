package com.thelegendofbald.view.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Rappresenta una tessera (tile) della mappa con immagine base e opzionale overlay.
 * <p>La classe è <b>final</b> e non è pensata per essere estesa.</p>
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
     * Costruttore principale.
     *
     * @param image         immagine di base (può essere {@code null})
     * @param width         larghezza target del tile
     * @param height        altezza target del tile
     * @param id            identificatore del tile (usa -1 se assente)
     * @param solid         {@code true} se bloccante
     * @param resize        {@code true} per ridimensionare {@code image} a (width x height)
     * @param isSpawn       {@code true} se tile di spawn
     * @param walkable      {@code true} se attraversabile
     * @param overlayImage  immagine overlay (può essere {@code null})
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
            final BufferedImage overlayImage
    ) {
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
                this.image = resized; // già copia
            } else {
                this.image = deepCopy(image); // copia difensiva
            }
        } else {
            this.image = null;
        }

        this.overlayImage = overlayImage != null ? deepCopy(overlayImage) : null; // copia difensiva
    }

    /**
     * Costruttore secondario semplificato.
     *
     * @param image  immagine di base
     * @param width  larghezza
     * @param height altezza
     */
    public Tile(final BufferedImage image, final int width, final int height) {
        this(image, width, height, 0, false, false, false, false, null);
    }

    /** @return una copia dell'immagine base del tile (può essere {@code null}) */
    public BufferedImage getImage() {
        return image == null ? null : deepCopy(image);
    }

    /** @return larghezza del tile */
    public int getWidth() {
        return width;
    }

    /** @return altezza del tile */
    public int getHeight() {
        return height;
    }

    /** @return identificatore del tile */
    public int getId() {
        return id;
    }

    /** @return {@code true} se il tile è solido/bloccante */
    public boolean isSolid() {
        return solid;
    }

    /** @return {@code true} se il tile è di spawn */
    public boolean isSpawn() {
        return isSpawn;
    }

    /** @return {@code true} se il tile è attraversabile */
    public boolean isWalkable() {
        return walkable;
    }

    /** @return {@code true} se l'id è valorizzato (diverso da -1) */
    public boolean hasId() {
        return id != -1;
    }

    /** @return una copia dell'immagine overlay (può essere {@code null}) */
    public BufferedImage getOverlayImage() {
        return overlayImage == null ? null : deepCopy(overlayImage);
    }

    /**
     * Disegna il tile (immagine base + overlay) alle coordinate specificate.
     *
     * @param g contesto grafico
     * @param x coordinata x
     * @param y coordinata y
     */
    public void render(final Graphics g, final int x, final int y) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
        if (overlayImage != null) {
            g.drawImage(overlayImage, x, y, null);
        }
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * @return {@code true} se l'immagine corrente non è della dimensione target.
     */
    public boolean isResizeable() {
        return image != null && (image.getWidth() != width || image.getHeight() != height);
    }

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

    /* ===================== Util: copia difensiva BufferedImage ===================== */

    private static BufferedImage deepCopy(final BufferedImage src) {
        // Evita TYPE_CUSTOM = 0
        final int type = src.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : src.getType();
        final BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), type);
        final Graphics2D g = copy.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return copy;
    }
}
