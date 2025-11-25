package com.thelegendofbald.view.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Rappresenta una tessera (tile) della mappa, composta da un'immagine di base e un'eventuale immagine di overlay.
 * <p>
 * La classe è immutabile e <b>final</b>. Gestisce automaticamente la copia difensiva delle immagini
 * per prevenire modifiche esterne accidentali e supporta il ridimensionamento in fase di inizializzazione.
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
     * Costruttore principale per creare un nuovo Tile con tutte le proprietà configurabili.
     *
     * @param image         immagine di base del tile (può essere {@code null})
     * @param width         larghezza desiderata del tile in pixel
     * @param height        altezza desiderata del tile in pixel
     * @param id            identificatore univoco del tipo di tile (usare -1 se non applicabile)
     * @param solid         {@code true} se il tile deve bloccare il movimento (collisione)
     * @param resize        {@code true} per forzare il ridimensionamento dell'immagine alle dimensioni specificate
     * @param isSpawn       {@code true} se questo tile rappresenta un punto di spawn
     * @param walkable      {@code true} se il tile è attraversabile dalle entità
     * @param overlayImage  immagine opzionale da disegnare sopra quella di base (può essere {@code null})
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
     * Costruttore semplificato per creare un tile di base non solido e senza proprietà speciali.
     *
     * @param image  immagine di base del tile
     * @param width  larghezza del tile
     * @param height altezza del tile
     */
    public Tile(final BufferedImage image, final int width, final int height) {
        this(image, width, height, 0, false, false, false, false, null);
    }

    /**
     * Restituisce una copia dell'immagine di base del tile.
     *
     * @return una nuova istanza {@link BufferedImage} o {@code null} se non presente
     */
    public BufferedImage getImage() {
        return image == null ? null : deepCopy(image);
    }

    /**
     * Restituisce la larghezza del tile.
     *
     * @return larghezza in pixel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Restituisce l'altezza del tile.
     *
     * @return altezza in pixel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Restituisce l'identificatore numerico del tipo di tile.
     *
     * @return l'ID del tile
     */
    public int getId() {
        return id;
    }

    /**
     * Indica se il tile è solido (bloccante per le collisioni).
     *
     * @return {@code true} se solido, altrimenti {@code false}
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * Indica se il tile è un punto di spawn.
     *
     * @return {@code true} se è uno spawn point, altrimenti {@code false}
     */
    public boolean isSpawn() {
        return isSpawn;
    }

    /**
     * Indica se il tile è calpestabile/attraversabile.
     *
     * @return {@code true} se attraversabile, altrimenti {@code false}
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * Verifica se il tile possiede un ID valido.
     *
     * @return {@code true} se l'ID è diverso da -1
     */
    public boolean hasId() {
        return id != -1;
    }

    /**
     * Restituisce una copia dell'immagine di overlay.
     *
     * @return una nuova istanza {@link BufferedImage} o {@code null} se non presente
     */
    public BufferedImage getOverlayImage() {
        return overlayImage == null ? null : deepCopy(overlayImage);
    }

    /**
     * Esegue il rendering del tile (immagine base e overlay) alle coordinate specificate.
     *
     * @param g contesto grafico su cui disegnare
     * @param x coordinata x di destinazione
     * @param y coordinata y di destinazione
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
     * Verifica l'uguaglianza tra due tile basandosi esclusivamente sul loro ID.
     *
     * @param obj l'oggetto da confrontare
     * @return {@code true} se gli oggetti hanno lo stesso ID
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
     * Calcola l'hash code basato sull'ID del tile.
     *
     * @return il valore di hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Verifica se le dimensioni dell'immagine corrente differiscono dalle dimensioni target del tile.
     *
     * @return {@code true} se l'immagine necessita di ridimensionamento
     */
    public boolean isResizeable() {
        return image != null && (image.getWidth() != width || image.getHeight() != height);
    }

    /**
     * Restituisce una rappresentazione in stringa dello stato del tile.
     *
     * @return stringa descrittiva
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
     * Crea una copia profonda (deep copy) di una BufferedImage per garantire l'immutabilità.
     *
     * @param src immagine sorgente
     * @return una nuova BufferedImage contenente gli stessi dati della sorgente
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
