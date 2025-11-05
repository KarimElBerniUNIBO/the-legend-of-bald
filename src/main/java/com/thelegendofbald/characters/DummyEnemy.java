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
public final class DummyEnemy extends Entity implements Combatant {

    // ---- Constants ----
    private static final int FRAME_WIDTH = 50;
    private static final int FRAME_HEIGHT = 50;
    private static final int RENDER_SIZE = 50;
    private static final int RUN_FRAMES = 9;
    private static final int DEFAULT_FRAME_DELAY = 5;

    private static final double DEFAULT_SPEED = 1.0;
    private static final double MIN_DISTANCE = 200;

    // ---- State ----
    private final int attackPower;
    private final transient TileMap tileMap;

    private final static double speedX = DEFAULT_SPEED;
    private final static double speedY = DEFAULT_SPEED;

    private BufferedImage[] runFrames;
    private int currentFrame;
    private final static int frameDelay = DEFAULT_FRAME_DELAY;
    private int frameCounter;

    private long lastAttackTime;

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

    /**
     * Loads running animation frames from resources.
     */
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
            } catch (final IOException e) {
                LoggerUtils.error("Error loading enemy run frame " + framePath + ": " + e.getMessage());
            }
        }
    }

    // ------------------- Combatant -------------------

    /**
     * Returns the base attack power of this enemy.
     *
     * @return base attack power
     */
    @Override
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * Applies damage to this enemy, reducing its health.
     *
     * @param damage amount of damage to apply
     */
    @Override
    public void takeDamage(final int damage) {
        this.getLifeComponent().damageTaken(damage);
    }

    /**
     * Heals this enemy by the specified amount.
     *
     * @param amount health points to restore
     */
    public void heal(final int amount) {
        this.getLifeComponent().heal(amount);
    }

    // ------------------- Animation & Render -------------------

    /**
     * Advances the animation frame based on an internal frame counter.
     */
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

    /**
     * Renders the current frame or a fallback rectangle if frames are missing.
     *
     * @param g graphics context
     */
    public void render(final Graphics g) {
        final BufferedImage frame =
                (runFrames != null && runFrames.length > 0) ? runFrames[currentFrame] : null;

        if (frame != null) {
            if (!isFacingRight()) {
                g.drawImage(frame, getX(), getY(), RENDER_SIZE, RENDER_SIZE, null);
            } else {
                g.drawImage(frame, getX() + RENDER_SIZE, getY(),
                        -RENDER_SIZE, RENDER_SIZE, null);
            }
        } else {
            g.setColor(Color.RED);
            g.fillRect(getX(), getY(), RENDER_SIZE, RENDER_SIZE);
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

        if (bald.getX() > getX()) {
            dx = speedX;
            setFacingRight(true);
        } else if (bald.getX() < getX()) {
            dx = -speedX;
            setFacingRight(false);
        }

        if (bald.getY() > getY()) {
            dy = speedY;
        } else if (bald.getY() < getY()) {
            dy = -speedY;
        }

        moveWithCollision(dx, dy);
    }

    /**
     * Moves the entity while checking collisions on the tile map.
     *
     * @param dx delta X in pixels
     * @param dy delta Y in pixels
     */
    private void moveWithCollision(final double dx, final double dy) {
        final double nextX = getX() + dx;
        final double nextY = getY() + dy;

        if (!isColliding(nextX, getY())) {
            setX((int) Math.round(nextX));
        }
        if (!isColliding(getX(), nextY)) {
            setY((int) Math.round(nextY));
        }
    }

    /**
     * Checks whether the rectangle at the next position hits a solid tile.
     *
     * @param nextX next X position
     * @param nextY next Y position
     * @return true if colliding with a solid tile
     */
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

    /**
     * @return true if this enemy is alive (health &gt; 0)
     */
    @Override
    public boolean isAlive() {
        return !this.getLifeComponent().isDead();
    }

    /**
     * @return timestamp of the last attack, implementation-defined unit
     */
    public long getLastAttackTime() {
        return lastAttackTime;
    }

    /**
     * Sets the last attack timestamp.
     *
     * @param time timestamp to store
     */
    public void setLastAttackTime(final long time) {
        this.lastAttackTime = time;
    }

    /**
     * Checks whether the enemy is closer than a threshold to the given player.
     *
     * @param bald player to compare distance with
     * @return true if closer than the internal threshold
     */
    public boolean isCloseTo(final Bald bald) {
        if (bald == null) {
            return false;
        }
        final double dx = this.getX() - bald.getX();
        final double dy = this.getY() - bald.getY();
        final double distance = Math.hypot(dx, dy);
        return distance < MIN_DISTANCE;
    }

    // ------------------- Overrides -------------------

    /**
     * @return render width in pixels
     */
    @Override
    public int getWidth() {
        return FRAME_WIDTH;
    }

    /**
     * @return render height in pixels
     */
    @Override
    public int getHeight() {
        return FRAME_HEIGHT;
    }
}
