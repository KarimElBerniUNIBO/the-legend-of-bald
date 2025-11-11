package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;

/**
 * FinalBoss — final enemy extending Entity and implementing Combatant.
 * Uses the collision system of DummyEnemy.
 */
public final class FinalBoss extends Entity implements Combatant {

    private static final double PHASE2_THRESHOLD = 0.66;
    private static final double PHASE3_THRESHOLD = 0.33;

    private static final double PHASE1_ATK_MULT = 1.00;
    private static final double PHASE2_ATK_MULT = 1.25;
    private static final double PHASE3_ATK_MULT = 1.50;

    private static final int PHASE1_SPEED = 2;
    private static final int PHASE2_SPEED = 3;
    private static final int PHASE3_SPEED = 4;

    private static final int MELEE_RANGE_PX = 48;
    private static final int DASH_MIN_DISTANCE_PX = 160;
    private static final int DASH_DISTANCE_PX = 96;
    private static final long DASH_COOLDOWN_MS = 2500;

    private static final int AOE_RANGE_PX = 64;
    private static final int AOE_EXTRA_DAMAGE = 6;
    private static final long AOE_COOLDOWN_MS = 2000;

    private static final int AGGRO_RANGE_PX = 200; 
    private static final int FRAME_WIDTH = 64;
    private static final int FRAME_HEIGHT = 64;
    private static final int RENDER_SIZE = 64;
    private static final int RUN_FRAMES = 2;
    private static final int DEFAULT_FRAME_DELAY = 10;
    private static final int FRAME_DELAY = DEFAULT_FRAME_DELAY;

    private final int baseAttackPower;
    private int phase = 1;
    private boolean alive = true;

    private final int maxHealth;
    private int health;

    private final TileMap map;
    private final int tileSize;

    private long lastDashAt = -DASH_COOLDOWN_MS;
    private long lastAoeAt = -AOE_COOLDOWN_MS;

    private BufferedImage[] runFrames;
    private int currentFrame;
    private int frameCounter;

    /**
     * Creates the final boss.
     *
     * @param x               spawn X position
     * @param y               spawn Y position
     * @param name            the boss's name
     * @param maxHealth       maximum health points
     * @param baseAttackPower base attack power (phase 1)
     * @param lifeComponent   the life component
     * @param map             map for collision handling
     */
    public FinalBoss(final int x, final int y,
                     final String name,
                     final int maxHealth,
                     final int baseAttackPower,
                     final LifeComponent lifeComponent,
                     final TileMap map) {
        super(x, y, FRAME_WIDTH, FRAME_HEIGHT, name != null ? name : "Final Boss", lifeComponent);
        this.maxHealth = Math.max(1, maxHealth);
        this.health = this.maxHealth;
        this.baseAttackPower = baseAttackPower;
        this.map = Objects.requireNonNull(map, "TileMap must not be null");
        this.tileSize = map.getTileSize();
        loadRunFrames(); 
        updatePhase();
    }

    private void loadRunFrames() {
        runFrames = new BufferedImage[RUN_FRAMES];
        for (int i = 0; i < RUN_FRAMES; i++) {
            final String framePath = String.format("/images/finalboss/finalbossL.png", i + 1);
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

    /**
     * Advances the animation frame based on the counter.
     */
    public void updateAnimation() {
        frameCounter++;
        if (frameCounter < FRAME_DELAY) {
            return;
        }
        frameCounter = 0;

        if (runFrames != null && runFrames.length > 0) {
            currentFrame = (currentFrame + 1) % runFrames.length;
        }
    }

    /**
     * Returns the boss's attack power,
     * multiplied based on the current phase.
     *
     * @return the current attack power
     */
    @Override
    public int getAttackPower() {
        switch (phase) {
            case 3: return (int) Math.round(baseAttackPower * PHASE3_ATK_MULT);
            case 2: return (int) Math.round(baseAttackPower * PHASE2_ATK_MULT);
            default: return (int) Math.round(baseAttackPower * PHASE1_ATK_MULT);
        }
    }

    /**
     * Applies damage to the boss, reducing its health
     * and updating its phase if necessary.
     *
     * @param damage the amount of damage to inflict
     */
    @Override
    public void takeDamage(final int damage) {
        if (!alive || damage <= 0) {
            return;
        }
        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
        } else {
            updatePhase();
        }
        getLifeComponent().setCurrentHealth(health);
    }

    /**
     * @return boss hitboxes.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * @return true if boss has life > 0.
     */
    @Override
    public boolean isAlive() {
        return alive;
    }

    /**
     * Manages the boss's AI: follows, attacks in melee, uses dash and AOE
     * only if the player is within "aggro" range.
     *
     * @param bald the player (Bald) to follow and attack
     */
    public void followPlayer(final Bald bald) {
        if (!alive || bald == null) {
            return;
        }
        final int dx = bald.getX() - getX();
        final int dy = bald.getY() - getY();
        final double dist = Math.hypot(dx, dy);

        if (dist > AGGRO_RANGE_PX) {
            return;
        }
        if (dist <= MELEE_RANGE_PX) {
            performMelee(bald);
            tryAoe(bald);
            return;
        }

        final long now = System.currentTimeMillis();
        if (dist >= DASH_MIN_DISTANCE_PX && (now - lastDashAt) >= DASH_COOLDOWN_MS && tryDash(dx, dy)) {
            lastDashAt = now;
            return;
        }

        double moveDx = 0.0;
        double moveDy = 0.0;
        final double speed = getCurrentSpeed(); 

        if (bald.getX() > getX()) {
            moveDx = speed;
            setFacingRight(true);
        } else if (bald.getX() < getX()) {
            moveDx = -speed;
            setFacingRight(false);
        }

        if (bald.getY() > getY()) {
            moveDy = speed;
        } else if (bald.getY() < getY()) {
            moveDy = -speed;
        }

        moveWithCollision(moveDx, moveDy);
    }

    /**
     * @return current boss life.
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return boss maximum health.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return current combat phase.
     */
    public int getPhase() {
        return phase;
    }


    /**
     * @return boss pace, for every phase
     */
    private int getCurrentSpeed() {
        switch (phase) {
            case 3: return PHASE3_SPEED;
            case 2: return PHASE2_SPEED;
            default: return PHASE1_SPEED;
        }
    }

    /**
     * Refreshes the boss phases by checking his life
     */
    private void updatePhase() {
        final double ratio = (double) health / maxHealth;
        if (ratio <= PHASE3_THRESHOLD) {
            phase = 3;
        } else if (ratio <= PHASE2_THRESHOLD) {
            phase = 2;
        } else {
            phase = 1;
        }
    }
    /**
     * Performs an attacks towards Bald
     * @param bald main character 
     */
    private void performMelee(final Bald bald) {
        Optional.ofNullable(bald).ifPresent(b -> b.takeDamage(getAttackPower()));
    }

    /**
     * Attempts to perform an AOE attack on Bald.
     * @param bald the player
     */
    private void tryAoe(final Bald bald) {
        final Bald player = Objects.requireNonNull(bald, "Bald must not be null");
        final long now = System.currentTimeMillis();
        if ((now - lastAoeAt) < AOE_COOLDOWN_MS) {
            return;
        }
        final int dx = player.getX() - getX();
        final int dy = player.getY() - getY();
        if (Math.hypot(dx, dy) <= AOE_RANGE_PX) {
            player.takeDamage(getAttackPower() + AOE_EXTRA_DAMAGE);
            lastAoeAt = now;
        }
    }

    /**
     * Attempts to perform a "dash" towards the player, 
     * checking for collisions.
     *
     * @param dx X component of the direction towards the player
     * @param dy Y component of the direction towards the player
     * @return true if the dash was performed (even partially), false otherwise
     */
    private boolean tryDash(final int dx, final int dy) {
        if (map == null) {
            return false;
        }
        final double len = Math.hypot(dx, dy);
        if (len == 0) {
            return false;
        }
        final double nx = dx / len;
        final double ny = dy / len;
        final int targetX = getX() + (int) Math.round(nx * DASH_DISTANCE_PX);
        final int targetY = getY() + (int) Math.round(ny * DASH_DISTANCE_PX);
        final int steps = 3;
        final int stepX = (targetX - getX()) / steps;
        final int stepY = (targetY - getY()) / steps;
        int curX = getX();
        int curY = getY();
        for (int i = 0; i < steps; i++) {
            final int nextX = curX + stepX;
            final int nextY = curY + stepY;
            if (isColliding(nextX, nextY)) { 
                setX(curX);
                setY(curY);
                setFacingRight(stepX >= 0);
                return i > 0;
            }
            curX = nextX;
            curY = nextY;
        }
        setX(curX);
        setY(curY);
        setFacingRight(stepX >= 0);
        return true;
    }

    /**
     * Moves entity by checking collisions.
     * @param dx delta X
     * @param dy delta Y
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
     * Checks if the entity would collide at a given position.
     * @param nextX next X position
     * @param nextY next Y position
     * @return true if there is a collision
     */
    private boolean isColliding(final double nextX, final double nextY) {
        final int entityWidth = getWidth();
        final int entityHeight = getHeight();

        final int leftTile = (int) nextX / tileSize;
        final int rightTile = (int) (nextX + entityWidth - 1) / tileSize;
        final int topTile = (int) nextY / tileSize;
        final int bottomTile = (int) (nextY + entityHeight - 1) / tileSize;

        for (int row = topTile; row <= bottomTile; row++) {
            for (int col = leftTile; col <= rightTile; col++) {
                final Tile tile = map.getTileAt(col, row);
                if (tile != null && tile.isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Renders the boss's current frame and its health bar.
     *
     * @param g the Graphics context to draw on
     */
    public void render(final Graphics g) {
        final BufferedImage frame =
            (runFrames != null && runFrames.length > 0) ? runFrames[currentFrame % runFrames.length] : null;
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
        final int hpBarWidth = RENDER_SIZE;
        final int hpBarHeight = 5;
        final int hpBarY = getY() - hpBarHeight - 2;
        g.setColor(Color.GRAY);
        g.fillRect(getX(), hpBarY, hpBarWidth, hpBarHeight);
        final int currentHpWidth = (int) (hpBarWidth * ((double) health / maxHealth));
        g.setColor(Color.GREEN.darker());
        g.fillRect(getX(), hpBarY, currentHpWidth, hpBarHeight);
        g.setColor(Color.BLACK);
        g.drawRect(getX(), hpBarY, hpBarWidth, hpBarHeight);
    }

    /**
     * @return Final Boss width value.
     */
    @Override
    public int getWidth() {
        return FRAME_WIDTH;
    }

    /**
     * @return Final Boss height value.
     */
    @Override
    public int getHeight() {
        return FRAME_HEIGHT;
    }
}
