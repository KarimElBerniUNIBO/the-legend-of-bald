package com.thelegendofbald.combat.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.thelegendofbald.characters.Entity;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;

/**
 * Simple projectile that moves in a straight line and deactivates on solid-tile collision.
 */
public final class Projectile extends Entity implements Combatant {

    // ---- Constants (no magic numbers) ----
    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;
    private static final int CORNER_OFFSET = 5;
    private static final int DIR_RIGHT = 0;
    private static final int DIR_LEFT = 1;

    // ---- State ----
    private final int speed;
    private final int direction; // 0 = right, 1 = left
    private boolean active = true;
    private final int damage;

    /**
     * Creates a projectile.
     *
     * @param x         start x in pixels
     * @param y         start y in pixels
     * @param direction movement direction (0=right, 1=left)
     * @param speed     pixels per tick
     * @param damage    attack power of the projectile
     */
    public Projectile(final int x, final int y, final int direction, final int speed, final int damage) {
        // LifeComponent is not used by a projectile; pass null
        super(x, y, WIDTH, HEIGHT, "bullet", LifeComponent.noLife());
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
    }

    /**
     * Advances the projectile and deactivates it if a solid tile is hit.
     *
     * @param tileMap current map used for collision checks
     */
    public void move(final TileMap tileMap) {
        final int nextX;
        final int nextY = getY();

        if (this.direction == DIR_RIGHT) {
            nextX = getX() + this.speed;
        } else if (this.direction == DIR_LEFT) {
            nextX = getX() - this.speed;
        } else {
            nextX = getX();
        }

        // Check collision on the four corners of the projectile
        final int[][] points = {
            { nextX, nextY },
            { nextX + CORNER_OFFSET, nextY },
            { nextX, nextY + CORNER_OFFSET },
            { nextX + CORNER_OFFSET, nextY + CORNER_OFFSET },
        };

        for (final int[] p : points) {
            final int tileX = p[0] / tileMap.getTileSize();
            final int tileY = p[1] / tileMap.getTileSize();
            final Tile tile = tileMap.getTileAt(tileX, tileY);
            if (tile != null && tile.isSolid()) {
                this.active = false;
                return;
            }
        }

        setX(nextX);
        setY(nextY);
    }

    /**
     * Renders the projectile as a filled circle.
     *
     * @param g graphics context
     */
    public void render(final Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(getX(), getY(), WIDTH, HEIGHT);
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), WIDTH, HEIGHT);
    }

    /** {@inheritDoc} */
    @Override
    public int getAttackPower() {
        return this.damage;
    }

    /** {@inheritDoc} */
    @Override
    public void takeDamage(final int damage) {
        // no-op: projectiles don't have health
    }

    /**
     * @return true if the projectile is still active (not collided)
     */
    @Override
    public boolean isAlive() {
        return this.active;
    }
}
