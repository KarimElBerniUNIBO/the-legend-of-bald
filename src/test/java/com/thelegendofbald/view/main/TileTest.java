package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TileTest {

    private static BufferedImage makeImg(int w, int h, Color color) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return img;
    }

    @Test
    @DisplayName("Costruttore semplificato: dimensioni e copie difensive")
    void simpleCtor_buildsAndCopies() {
        BufferedImage src = makeImg(10, 12, Color.RED);
        Tile t = new Tile(src, 10, 12);

        assertEquals(10, t.getWidth());
        assertEquals(12, t.getHeight());
        assertEquals(0, t.getId());
        assertFalse(t.isSolid());
        assertFalse(t.isSpawn());
        assertFalse(t.isWalkable());

        // getImage() deve restituire una copia (riferimento diverso)
        BufferedImage got1 = t.getImage();
        BufferedImage got2 = t.getImage();
        assertNotNull(got1);
        assertNotSame(src, got1);
        assertNotSame(got1, got2); // ogni chiamata restituisce una nuova copia

        // Modificare la copia non deve toccare lo stato interno
        Graphics2D g = got1.createGraphics();
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 5, 5);
        g.dispose();

        // Chiedendo di nuovo l'immagine, non deve essere "sporcata" dalla modifica precedente
        BufferedImage fresh = t.getImage();
        // Verifica qualche pixel che avevamo sovrascritto in got1: l'immagine "fresh" deve rimanere rossa
        int rgbFresh = fresh.getRGB(2, 2);
        assertEquals(Color.RED.getRGB(), rgbFresh);
    }

    @Test
    @DisplayName("Costruttore principale: resize=true ridimensiona; resize=false conserva dimensioni sorgente")
    void mainCtor_resizeBehavior() {
        BufferedImage src = makeImg(8, 8, Color.BLUE);

        // resize = true -> l'immagine interna diventa (32x24)
        Tile resized = new Tile(src, 32, 24, 7, true, true, false, true, null);
        assertFalse(resized.isResizeable()); // ora coincide con (width,height)
        assertEquals(32, resized.getImage().getWidth());
        assertEquals(24, resized.getImage().getHeight());
        assertEquals(7, resized.getId());
        assertTrue(resized.isSolid());
        assertFalse(resized.isSpawn());
        assertTrue(resized.isWalkable());
        assertTrue(resized.hasId());

        // resize = false -> l’immagine interna resta 8x8, dunque risulta "resizeable" rispetto a (32x24)
        Tile notResized = new Tile(src, 32, 24, -1, false, false, false, false, null);
        assertTrue(notResized.isResizeable());
        assertEquals(8, notResized.getImage().getWidth());
        assertEquals(8, notResized.getImage().getHeight());
        assertFalse(notResized.hasId()); // id = -1
    }

    @Test
    @DisplayName("Overlay: copia difensiva e getter non espone lo stato interno")
    void overlay_defensiveCopy() {
        BufferedImage base = makeImg(16, 16, Color.GRAY);
        BufferedImage overlay = makeImg(16, 16, Color.YELLOW);

        Tile t = new Tile(base, 16, 16, 5, false, false, true, true, overlay);

        BufferedImage gotOverlay1 = t.getOverlayImage();
        BufferedImage gotOverlay2 = t.getOverlayImage();

        assertNotNull(gotOverlay1);
        assertNotSame(overlay, gotOverlay1);     // copia difensiva nel costruttore
        assertNotSame(gotOverlay1, gotOverlay2); // ogni getter ritorna una nuova copia

        // Sporco gotOverlay1 e controllo che una nuova copia non sia sporcata
        Graphics2D g = gotOverlay1.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 4, 4);
        g.dispose();

        BufferedImage fresh = t.getOverlayImage();
        // il pixel (2,2) della nuova copia deve essere ancora giallo
        assertEquals(Color.YELLOW.getRGB(), fresh.getRGB(2, 2));
    }

    @Test
    @DisplayName("Render: non lancia eccezioni con o senza immagini")
    void render_noThrow() {
        // Caso 1: con immagini
        Tile withImages = new Tile(makeImg(8, 8, Color.RED), 8, 8, 1, false, false, false, true, makeImg(8, 8, Color.GREEN));
        BufferedImage canvas1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        assertDoesNotThrow(() -> {
            withImages.render(canvas1.createGraphics(), 10, 10);
        });

        // Caso 2: immagini nulle
        Tile noImages = new Tile(null, 8, 8, 2, false, false, false, false, null);
        BufferedImage canvas2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        assertDoesNotThrow(() -> {
            noImages.render(canvas2.createGraphics(), 0, 0);
        });
    }

    @Test
    @DisplayName("equals/hashCode basati su id")
    void equalsAndHashCode_onIdOnly() {
        Tile a = new Tile(makeImg(4, 4, Color.RED), 4, 4, 42, false, false, false, false, null);
        Tile b = new Tile(makeImg(8, 8, Color.BLUE), 8, 8, 42, true, true, true, true, makeImg(8, 8, Color.CYAN));
        Tile c = new Tile(makeImg(4, 4, Color.RED), 4, 4, 7, false, false, false, false, null);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());

        assertNotEquals(a, null);
        assertNotEquals(a, "not a tile");
    }
}
