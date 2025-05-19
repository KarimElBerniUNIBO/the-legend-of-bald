package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.thelegendofbald.api.views.View;

public class GridPanel extends JPanel {

    private static final int TILE_SIZE = 32;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        g.setColor(new Color(255, 255, 255, 40)); // bianco trasparente

        for (int x = 0; x <= width; x += TILE_SIZE) {
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y <= height; y += TILE_SIZE) {
            g.drawLine(0, y, width, y);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return getParent() != null ? ((View) getParent()).getInternalSize() : new Dimension(1280, 704);
    }
}
