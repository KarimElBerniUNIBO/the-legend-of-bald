package com.thelegendofbald.view.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Panel that draws an overlay grid on top of the content.
 * <p>
 * The class is <b>final</b> and is not intended to be extended.
 */
public final class GridPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TILE_SIZE = 32;
    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 704;
    private static final int GRID_R = 255;
    private static final int GRID_G = 255;
    private static final int GRID_B = 255;
    private static final int GRID_ALPHA = 40;
    private static final Color GRID_COLOR = new Color(GRID_R, GRID_G, GRID_B, GRID_ALPHA);

    /**
     * Performs component painting and draws the overlay grid.
     *
     * @param g the graphics context, unmodified (not reassigned)
     */
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    /**
     * Draws the grid on the panel.
     *
     * @param g the graphics context, unmodified (not reassigned)
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
     * Returns the preferred size: if a parent exists, uses its current size;
     * otherwise returns a default dimension.
     *
     * @return the preferred size of the panel
     */
    @Override
    public Dimension getPreferredSize() {
        return getParent() != null
                ? getParent().getSize()
                : new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
