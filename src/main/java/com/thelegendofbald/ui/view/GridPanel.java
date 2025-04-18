package com.thelegendofbald.ui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GridPanel extends JPanel {
    private static final int TILE_SIZE = 32;  // Dimensione di ciascun tile

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        // Ottieni la larghezza e l'altezza correnti di GridPanel
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Calcola il numero di tile orizzontali e verticali in base alle dimensioni di GridPanel
        int tilesX = panelWidth / TILE_SIZE;
        int tilesY = panelHeight / TILE_SIZE;

        g.setColor(Color.LIGHT_GRAY);  // Imposta il colore della griglia

        // Disegna le linee verticali della griglia
        for (int x = 0; x <= tilesX; x++) {
            g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, panelHeight);  // Linea verticale
        }

        // Disegna le linee orizzontali della griglia
        for (int y = 0; y <= tilesY; y++) {
            g.drawLine(0, y * TILE_SIZE, panelWidth, y * TILE_SIZE);  // Linea orizzontale
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Restituisce le dimensioni correnti di GridPanel
        // che sono le stesse del suo genitore (GamePanel)
        return new Dimension(getParent().getWidth(), getParent().getHeight());
    }
}