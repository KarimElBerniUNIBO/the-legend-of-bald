package com.thelegendofbald.model.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.render.Tile;
import com.thelegendofbald.view.render.TileMap;

/**
 * Simple enemy that follows the player, reacts to hits, and has a death animation.
 */
public final class DummyEnemy extends Entity implements Combatant {


    private enum EnemyState {
        RUNNING,
        HURT,
        DYING,
        DEAD 
    }

    private static final int FRAME_WIDTH = 50;
    private static final int FRAME_HEIGHT = 50;
    private static final int RENDER_SIZE = 50;
    private static final int RUN_FRAMES = 9;
    private static final int HURT_FRAMES = 5;
    private static final int DEAD_FRAMES = 7;
    private static final int DEFAULT_FRAME_DELAY = 5;
    private static final double DEFAULT_SPEED = 1.0;
    private static final double MIN_DISTANCE = 200;
    private static final double SPEED_X = DEFAULT_SPEED;
    private static final double SPEED_Y = DEFAULT_SPEED;
    private static final int FRAME_DELAY = DEFAULT_FRAME_DELAY;
    private final int attackPower;
    private final transient TileMap tileMap;

    private BufferedImage[] runFrames;
    private BufferedImage[] hurtFrames;
    private BufferedImage[] deadFrames;

    private int currentFrame;
    private int frameCounter;
    private long lastAttackTime;
    private EnemyState state = EnemyState.RUNNING;

    /**
     * Creates an enemy.
     *
     * @param x           spawn X position
     * @param y           spawn Y position
     * @param health      initial health points
     * @param name        entity name
     * @param attackPower base attack power
     * @param tileMap     map for collision handling
     */
    public DummyEnemy(final int x, final int y, final int health, final String name,
                      final int attackPower, final TileMap tileMap) {
        super(x, y, FRAME_WIDTH, FRAME_HEIGHT, name, new LifeComponent(health));
        this.attackPower = attackPower;
        this.tileMap = Objects.requireNonNull(tileMap, "tileMap must not be null");
        loadFrames();
    }

    /**
     * Loads ALL animation frames (Run, Hurt, Dead).
     */
    private void loadFrames() {
        runFrames = new BufferedImage[RUN_FRAMES];
        hurtFrames = new BufferedImage[HURT_FRAMES];
        deadFrames = new BufferedImage[DEAD_FRAMES];
        for (int i = 0; i < RUN_FRAMES; i++) {
            final String framePath = String.format("/images/dummyenemy_run/__TRAINEE_Run_00%d.png", i + 1);
            runFrames[i] = loadImage(framePath);
        }

        for (int i = 0; i < HURT_FRAMES; i++) {
            final String framePath = String.format("/images/dummyenemy_run/06-Hurt/__TRAINEE_Hurt_00%d.png", i + 1);
            hurtFrames[i] = loadImage(framePath);
        }

        for (int i = 0; i < DEAD_FRAMES; i++) {
            final String framePath = String.format("/images/dummyenemy_run/07-Dead/__TRAINEE_Dead_00%d.png", i + 1);
            deadFrames[i] = loadImage(framePath);
        }
    }

    /**
     * Helper method to load a single image.
     *
     * @param path the resource (image) path to load
     * @return The loaded image as BufferedImage, or null if not found
     */
    private BufferedImage loadImage(final String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            } else {
                LoggerUtils.error("Frame not found: " + path);
                return null;
            }
        } catch (final IOException e) {
            LoggerUtils.error("Error loading frame " + path + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * Applies damage and changes state to HURT or DYING. (MODIFIED)
     */
    @Override
    public void takeDamage(final int damage) {
        if (state == EnemyState.DYING || state == EnemyState.DEAD) {
            return;
        }

        this.getLifeComponent().damageTaken(damage);
        currentFrame = 0;
        frameCounter = 0;

        if (this.getLifeComponent().isDead()) {
            state = EnemyState.DYING;
        } else {
            state = EnemyState.HURT;
        }
    }

    /**
     * Heals the enemy by restoring health.
     *
     * @param amount the amount of health to restore
     */
    public void heal(final int amount) {
        this.getLifeComponent().heal(amount);
    }

    /**
     * Advances the animation based on the current state. (MODIFIED)
     */
    public void updateAnimation() {
        frameCounter++;
        if (frameCounter < FRAME_DELAY) {
            return;
        }
        frameCounter = 0;
        currentFrame++;

        switch (state) {
            case RUNNING:
                if (runFrames != null && runFrames.length > 0) {
                    currentFrame = currentFrame % runFrames.length;
                }
                break;

            case HURT:
                if (currentFrame >= HURT_FRAMES) {
                    currentFrame = 0;
                    state = EnemyState.RUNNING;
                }
                break;

            case DYING:
                if (currentFrame >= DEAD_FRAMES) {
                    currentFrame = DEAD_FRAMES - 1;
                    state = EnemyState.DEAD;
                }
                break;

            case DEAD:
                currentFrame = DEAD_FRAMES - 1;
                break;
        }
    }

    /**
     * Renders the correct frame based on the state.
     *
     * @param g the Graphics context to draw on
     */
    public void render(final Graphics g) {
        BufferedImage frame = null;

        switch (state) {
            case RUNNING:
                if (runFrames != null && runFrames.length > 0) {
                    frame = runFrames[currentFrame % runFrames.length];
                }
                break;
            case HURT:
                if (hurtFrames != null && hurtFrames.length > 0) {
                    frame = hurtFrames[currentFrame % HURT_FRAMES];
                }
                break;
            case DYING:
            case DEAD:
                if (deadFrames != null && deadFrames.length > 0) {
                    frame = deadFrames[currentFrame % DEAD_FRAMES];
                }
                break;
        }

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

    /**
     * Follows the player ONLY if in the RUNNING state.
     *
     * @param bald the player (Bald) to follow
     */
    public void followPlayer(final Bald bald) {
        if (state != EnemyState.RUNNING || bald == null) {
            return;
        }

        double dx = 0.0;
        double dy = 0.0;

        if (bald.getX() > getX()) {
            dx = SPEED_X;
            setFacingRight(true);
        } else if (bald.getX() < getX()) {
            dx = -SPEED_X;
            setFacingRight(false);
        }

        if (bald.getY() > getY()) {
            dy = SPEED_Y;
        } else if (bald.getY() < getY()) {
            dy = -SPEED_Y;
        }

        moveWithCollision(dx, dy);
    }

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

    private boolean isColliding(final double nextX, final double nextY) {
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

    /**
     * @return true if the enemy is not yet in the DYING or DEAD state
     */
    @Override
    public boolean isAlive() {
        return state == EnemyState.RUNNING || state == EnemyState.HURT;
    }

    /**
     * Used by GamePanel to know when to remove the body.
     * @return true only if the enemy is in the final DEAD state.
     */
    public boolean isRemovable() {
        return state == EnemyState.DEAD;
    }

    /**
     * @return the timestamp (in milliseconds) of the last attack
     */
    public long getLastAttackTime() {
        return lastAttackTime;
    }

    /**
     * Sets the timestamp of the last attack.
     *
     * @param time the timestamp (in milliseconds) to store
     */
    public void setLastAttackTime(final long time) {
        this.lastAttackTime = time;
    }

    /**
     * Checks if the enemy is sufficiently close to the player
     * (within MIN_DISTANCE).
     *
     * @param bald the player (Bald) to check
     * @return true if the player is close, false otherwise
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

    /**
     * @return the enemy's collision/render width
     */
    @Override
    public int getWidth() {
        return FRAME_WIDTH;
    }

    /**
     * @return the enemy's collision/render height
     */
    @Override
    public int getHeight() {
        return FRAME_HEIGHT;
    }
}
