package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;

/**
 * Very simple enemy that follows the player and collides with solid tiles.
 */
public class DummyEnemy extends Entity implements Combatant {

    // ---- Constants ----
    private static final int FRAME_WIDTH = 50;
    private static final int FRAME_HEIGHT = 50;

    private static final int RENDER_SIZE = 50;     // draw size (kept square)
    private static final int RUN_FRAMES = 9;
    private static final int DEFAULT_FRAME_DELAY = 5;

    private static final double DEFAULT_SPEED = 1.0;   // px per tick (prima era int 1)

    // ---- State ----
    private final int attackPower;
    private final transient TileMap tileMap;

    private double speedX = DEFAULT_SPEED;
    private double speedY = DEFAULT_SPEED;

    private BufferedImage[] runFrames;
    private int currentFrame;
    private int frameDelay = DEFAULT_FRAME_DELAY;
    private int frameCounter;

    private long lastAttackTime;           // usato dal combat manager altrove?
    private final double minDistance = 200;

    /**
     * Creates a dummy enemy.
     *
     * @param x           spawn x
     * @param y           spawn y
     * @param health      starting health
     * @param name        entity name
     * @param attackPower base attack power
     * @param tileMap     map for collisions
     */
    public DummyEnemy(final int x, final int y, final int health, final String name,
                      final int attackPower, final TileMap tileMap) {
        super(x, y, FRAME_WIDTH, FRAME_HEIGHT, name, new LifeComponent(health));
        this.attackPower = attackPower;
        this.tileMap = Objects.requireNonNull(tileMap, "tileMap must not be null");
        loadRunFrames();
    }

    // ------------------- Resources -------------------

    private void loadRunFrames() {
        runFrames = new BufferedImage[RUN_FRAMES];
        for (int i = 0; i < RUN_FRAMES; i++) {
            final String framePath = String.format("/images/dummyenemy_run/__TRAINEE_Run_00%d.png", i + 1);
            try (InputStream is = getClass().getResourceAsStream(framePath)) {
                if (is != null) {
                    runFrames[i] = ImageIO.read(is);
                } else {
                    LoggerUtils.error("Enemy run frame not found: " + framePath);
                }
            } catch (IOException e) {
                LoggerUtils.error("Error loading enemy run frame " + framePath + ": " + e.getMessage());
            }
        }
    }

    // ------------------- Combatant -------------------

    @Override
    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public void takeDamage(final int damage) {
        this.lifeComponent.damageTaken(damage);
    }

    public void heal(final int amount) {
        this.lifeComponent.heal(amount);
    }

    // ------------------- Animation & Render -------------------

    public void updateAnimation() {
        frameCounter++;
        if (frameCounter < frameDelay) {
            return;
        }
        frameCounter = 0;

        if (runFrames != null && runFrames.length > 0) {
            currentFrame = (currentFrame + 1) % runFrames.length;
        }
    }

    public void render(final Graphics g) {
        final BufferedImage frame =
                (runFrames != null && runFrames.length > 0) ? runFrames[currentFrame] : null;

        if (frame != null) {
            if (!facingRight) {
                g.drawImage(frame, x, y, RENDER_SIZE, RENDER_SIZE, null);
            } else {
                g.drawImage(frame, x + RENDER_SIZE, y, -RENDER_SIZE, RENDER_SIZE, null);
            }
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, RENDER_SIZE, RENDER_SIZE);
        }
    }

    // ------------------- AI & Movement -------------------

    /**
     * Simple chase logic: set per-axis deltas toward Bald, then move with tile collision.
     *
     * @param bald player to follow
     */
    public void followPlayer(final Bald bald) {
        if (bald == null) {
            return;
        }

        double dx = 0.0;
        double dy = 0.0;

        if (bald.getX() > this.x) {
            dx = speedX;
            facingRight = true;
        } else if (bald.getX() < this.x) {
            dx = -speedX;
            facingRight = false;
        }

        if (bald.getY() > this.y) {
            dy = speedY;
        } else if (bald.getY() < this.y) {
            dy = -speedY;
        }

        moveWithCollision(dx, dy);
    }

    private void moveWithCollision(final double dx, final double dy) {
        final double nextX = x + dx;
        final double nextY = y + dy;

        // asse X
        if (!isColliding(nextX, y)) {
            x = (int) Math.round(nextX);
        }
        // asse Y
        if (!isColliding(x, nextY)) {
            y = (int) Math.round(nextY);
        }
    }

    private boolean isColliding(final double nextX, final double nextY) {
        // tileMap è final e non null per contratto
        final int tileSize = tileMap.getTileSize();
        final int entityWidth = getWidth();
        final int entityHeight = getHeight();

        final int leftTile = (int) nextX / tileSize;
        final int rightTile = (int) (nextX + entityWidth - 1) / tileSize;
        final int topTile = (int) nextY / tileSize;
        final int bottomTile = (int) (nextY + entityHeight - 1) / tileSize;

        for (int row = topTile; row <= bottomTile; row++) {
            for (int col = leftTile; col <= rightTile; col++) {
                final Tile tile = tileMap.getTileAt(col, row);
                if (tile != null && tile.isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

    // ------------------- Misc -------------------

    public boolean isAlive() {
        return !this.lifeComponent.isDead();
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(final long time) {
        this.lastAttackTime = time;
    }

    public boolean isCloseTo(final Bald bald) {
        if (bald == null) {
            return false;
        }
        final double dx = this.x - bald.getX();
        final double dy = this.y - bald.getY();
        final double distance = Math.hypot(dx, dy);
        return distance < this.minDistance;
    }

    // ------------------- Overrides -------------------

    @Override
    public int getWidth() {
        return FRAME_WIDTH;
    }

    @Override
    public int getHeight() {
        return FRAME_HEIGHT;
    }
}
