package com.thelegendofbald.view.main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test "headless" per {@link GridPanel} senza Mockito.
 * Vive nel package 'model' ma importa la classe dalla view.
 */
class GridPanelTest {

    // Costanti usate anche nella classe da testare
    private static final int DEFAULT_W = 1280;
    private static final int DEFAULT_H = 704;

    @Test
    @DisplayName("getPreferredSize() senza parent -> dimensione di default")
    void preferredSize_withoutParent_returnsDefault() {
        GridPanel panel = new GridPanel();
        assertEquals(new Dimension(DEFAULT_W, DEFAULT_H), panel.getPreferredSize());
    }

    @Test
    @DisplayName("getPreferredSize() con parent -> uguale a size del parent")
    void preferredSize_withParent_matchesParentSize() {
        JPanel parent = new JPanel(null);     // no layout per non alterare size
        parent.setSize(500, 400);             // getPreferredSize() di GridPanel usa getParent().getSize()

        GridPanel panel = new GridPanel();
        panel.setBounds(0, 0, 100, 100);
        parent.add(panel);

        assertSame(parent, panel.getParent(), "Il parent deve essere assegnato");
        assertEquals(new Dimension(500, 400), panel.getPreferredSize(),
                "Con parent presente, la preferred deve essere la size del parent");
    }

    @Test
    @DisplayName("paintComponent() disegna linee ogni 32px (verticali e orizzontali)")
    void paint_drawsGridEvery32px() {
        // Useremo 90x90 per avere linee a 0,32,64 (96 sarebbe oltre bordo)
        final int w = 90, h = 90;

        GridPanel panel = new GridPanel();
        panel.setOpaque(false);            // evita riempimento di background opaco
        panel.setSize(w, h);

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        // nessuna antialias per linee nette
        panel.paintComponent(g2);
        g2.dispose();

        // Helper: alpha del pixel (0..255)
        java.util.function.BiFunction<Integer, Integer, Integer> alphaAt = (x, y) ->
                (img.getRGB(x, y) >>> 24) & 0xFF;

        // Verticali attese a x = 0, 32, 64 (campiono a y=10 per evitare l'incrocio con orizzontali)
        assertTrue(alphaAt.apply(0, 10)  > 0, "Linea verticale a x=0 deve essere disegnata");
        assertTrue(alphaAt.apply(32, 10) > 0, "Linea verticale a x=32 deve essere disegnata");
        assertTrue(alphaAt.apply(64, 10) > 0, "Linea verticale a x=64 deve essere disegnata");

        // Vicinanza fuori linea deve restare trasparente
        assertEquals(0, alphaAt.apply(1, 10),  "x=1 non deve essere su una linea");
        assertEquals(0, alphaAt.apply(31, 10), "x=31 non deve essere su una linea");
        assertEquals(0, alphaAt.apply(33, 10), "x=33 non deve essere su una linea");

        // Orizzontali attese a y = 0, 32, 64 (campiono a x=10)
        assertTrue(alphaAt.apply(10, 0)  > 0, "Linea orizzontale a y=0 deve essere disegnata");
        assertTrue(alphaAt.apply(10, 32) > 0, "Linea orizzontale a y=32 deve essere disegnata");
        assertTrue(alphaAt.apply(10, 64) > 0, "Linea orizzontale a y=64 deve essere disegnata");

        // Vicinanza fuori linea per le orizzontali
        assertEquals(0, alphaAt.apply(10, 1),  "y=1 non deve essere su una linea");
        assertEquals(0, alphaAt.apply(10, 31), "y=31 non deve essere su una linea");
        assertEquals(0, alphaAt.apply(10, 33), "y=33 non deve essere su una linea");
    }
}
