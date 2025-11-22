package com.thelegendofbald.view.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Pannello che disegna una griglia di overlay sopra al contenuto.
 * <p>
 * La classe è <b>final</b> e non è pensata per essere estesa.
 */
public final class GridPanel extends JPanel {

    /** Per SpotBugs: le sottoclassi di Serializable dovrebbero dichiararlo. */
    private static final long serialVersionUID = 1L;

    /** Lato di ciascun riquadro della griglia (in pixel). */
    private static final int TILE_SIZE = 32;

    /** Larghezza preferita di default (quando non c'è un parent). */
    private static final int DEFAULT_WIDTH = 1280;

    /** Altezza preferita di default (quando non c'è un parent). */
    private static final int DEFAULT_HEIGHT = 704;

    /** Componenti RGBA per il colore della griglia. */
    private static final int GRID_R = 255;
    private static final int GRID_G = 255;
    private static final int GRID_B = 255;
    private static final int GRID_ALPHA = 40;

    /** Colore della griglia (bianco trasparente). */
    private static final Color GRID_COLOR = new Color(GRID_R, GRID_G, GRID_B, GRID_ALPHA);

    /**
     * Esegue il painting del componente e disegna la griglia di overlay.
     *
     * @param g il contesto grafico, non modificato (non viene riassegnato)
     */
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    /**
     * Disegna la griglia sul pannello.
     *
     * @param g il contesto grafico, non modificato (non viene riassegnato)
     */
    private void drawGrid(final Graphics g) {
        final int width = getWidth();
        final int height = getHeight();

        g.setColor(GRID_COLOR);

        for (int x = 0; x <= width; x += TILE_SIZE) {
            g.drawLine(x, 0, x, height);
        }
        for (int y = 0; y <= height; y += TILE_SIZE) {
            g.drawLine(0, y, width, y);
        }
    }

    /**
     * Restituisce la dimensione preferita: se esiste un parent, usa la sua
     * dimensione corrente; altrimenti ritorna una dimensione di default.
     *
     * @return la dimensione preferita del pannello
     */
    @Override
    public Dimension getPreferredSize() {
        return getParent() != null
                ? getParent().getSize()
                : new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}

