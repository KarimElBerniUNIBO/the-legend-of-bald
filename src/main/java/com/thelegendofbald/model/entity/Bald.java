package com.thelegendofbald.model.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.combat.effect.StatusEffect;
import com.thelegendofbald.combat.effect.StatusEffectManager;
import com.thelegendofbald.model.system.Wallet;
import com.thelegendofbald.model.item.weapons.Weapon;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.render.Tile;
import com.thelegendofbald.view.render.TileMap;

/**
 * Main player character with movement, animation and tile-based collisions.
 */
public final class Bald extends Entity implements Combatant {

    private static final int FRAME_WIDTH = 50;
    private static final int FRAME_HEIGHT = 50;

    private static final int HITBOX_WIDTH = 15;
    private static final int HITBOX_HEIGHT = 25;
    private static final int ENTITY_SIZE = 50;
    private static final int TILE_SIZE = 32;
    private static final int COLLISION_TILE_ID = 2;

    private static final int ATTACK_FRAMES = 8;
    private static final int RUN_FRAMES = 9;

    private static final int DEFAULT_FRAME_DELAY = 5;
    private static final int FRAME_DELAY = DEFAULT_FRAME_DELAY;

    /** Speed multiplier to convert units/sec into pixels per update. */
    private static final double SPEED_MULTIPLIER = 50.0;

    /** Size used when rendering flipped images. */
    private static final int RENDER_SIZE = 50;

    /** Scheduler used for timed effects (e.g., immobilize), avoids raw threads. */
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor(r -> {
                final Thread t = new Thread(r, "bald-effects-scheduler");
                t.setDaemon(true);
                return t;
            });

    private final Wallet wallet = new Wallet(100);
    private final StatusEffectManager buffManager = new StatusEffectManager(this);

    private Optional<Weapon> weapon = Optional.empty();

    private TileMap tileMap;
    private int attackPower;

    private double speedX;
    private double speedY;
    private double posX;
    private double posY;

    private BufferedImage[] runFrames;
    private final Map<String, BufferedImage[]> attackFrames = new HashMap<>();
    private BufferedImage[] actualAttackFrames;

    private int currentFrame;
    private int frameCounter;

    private boolean attacking;
    private int currentAttackFrame;
    private boolean facingRight = true;
    private boolean immobilized;

    /**
     * Creates a Bald instance.
     *
     * @param x initial x position in pixels
     * @param y initial y position in pixels
     * @param maxHealth maximum health points
     * @param name display name of the character
     * @param baseAttackPower base attack power before buffs
     */
    public Bald(final int x, final int y, final int maxHealth, final String name, final int baseAttackPower) {
        super(x, y, FRAME_WIDTH, FRAME_HEIGHT, name, new LifeComponent(maxHealth));
        this.attackPower = baseAttackPower;
        loadRunFrames();
        loadAllAttackFrames();
    }


    private void loadRunFrames() {
        runFrames = new BufferedImage[RUN_FRAMES];
        for (int i = 0; i < RUN_FRAMES; i++) {
            final String framePath = String.format("/images/bald_run/PS_BALD GUY_Run_00%d.png", i + 1);
            try (InputStream is = getClass().getResourceAsStream(framePath)) {
                if (is != null) {
                    runFrames[i] = ImageIO.read(is);
                } else {
                    LoggerUtils.error("Run frame not found: " + framePath);
                }
            } catch (final IOException e) {
                 LoggerUtils.error("Error loading run frame " + framePath + ": " + e.getMessage());
            }
        }
    }

    private void loadAllAttackFrames() {
        final String basePath = "/images/bald_attack";
        final List<String> weaponNames = List.of("def", "sword", "axe");

        for (final String weaponName : weaponNames) {
            final String dir = basePath + "/" + weaponName;
            final BufferedImage[] frames = new BufferedImage[ATTACK_FRAMES];

            IntStream.range(0, ATTACK_FRAMES).forEach(i -> {
                final String framePath = dir + String.format("/frame_%d.png", i);
                try (InputStream is = getClass().getResourceAsStream(framePath)) {
                    if (is != null) {
                        frames[i] = ImageIO.read(is);
                    } else {
                        LoggerUtils.error("Attack frame not found: " + framePath);
                    }
                } catch (final IOException e) {
                    LoggerUtils.error("Error loading attack frame " + framePath + ": " + e.getMessage());
                }
            });

            attackFrames.put(weaponName, frames);
        }

        LoggerUtils.info("Attack frames loaded: " + attackFrames.size());
    }

    /**
     * Sets the current tile map used for collisions and spawn computations.
     * @param map the tile map to use
     */
    public void setTileMap(final TileMap map) {
        this.tileMap = map;
    }

    /**
     * Places Bald centered on the spawn tile; feet rest on ground.
     * @param spawnTileId the spawn tile identifier to search
     * @param tileSize the tile size in pixels
     */
    public void setSpawnPosition(final int spawnTileId, final int tileSize) {
        if (tileMap == null) {
            LoggerUtils.error("TileMap not set: cannot set spawn position.");
            return;
        }
        final Point spawnPoint = tileMap.findSpawnPoint(spawnTileId);
        if (spawnPoint != null) {
            final double newPosX = spawnPoint.x + (tileSize - getWidth()) / 2.0;
            final double newPosY = spawnPoint.y + tileSize - getHeight();

            this.posX = newPosX;
            this.posY = newPosY;

            this.setX((int) (posX + 0.5));
            this.setY((int) (posY + 0.5));
        } else {
            LoggerUtils.error("Spawn point not found for tile id: " + spawnTileId);
        }
    }


    /**
     * @return the current attack power after buffs are applied
     */
    @Override
    public int getAttackPower() {
        return buffManager.modifyAttackPower(attackPower);
    }

    /**
     * Sets the base (unbuffed) attack power.
     * @param value new base attack power
     */
    public void setAttackPower(final int value) {
        this.attackPower = value;
    }

    /**
     * Applies a status effect/buff to the player.
     * @param buff the effect to apply
     */
    public void applyBuff(final StatusEffect buff) {
        buffManager.applyEffect(buff);
    }

    /**
     * Updates active status effects and their timers.
     */
    public void updateBuffs() {
        buffManager.update();
    }

    /**
     * Inflicts damage to the player, reducing health.
     * @param damage the amount of damage to apply
     */
    @Override
    public void takeDamage(final int damage) {
        this.getLifeComponent().damageTaken(damage);
        LoggerUtils.info("Player took damage: " + damage
                + ". Current health: " + getLifeComponent().getCurrentHealth());
    }

    /**
     * @return true if the player is alive (health > 0)
     */
    @Override
    public boolean isAlive() {
        return !this.getLifeComponent().isDead();
    }

    /**
     * @return true if the player is currently allowed to shoot
     */
    public boolean canShoot() {
        return true;
    }

    /**
     * Shoots a projectile from the player.
     * @throws UnsupportedOperationException until implemented
     */
    public void shootProjectile() {
        throw new UnsupportedOperationException("Unimplemented method 'shootProjectile'");
    }

    /**
     * Starts the attack animation based on the current weapon.
     */
    public void startAttackAnimation() {
        weapon.ifPresent(w ->
                actualAttackFrames = attackFrames.getOrDefault(
                        w.getName().toLowerCase(Locale.ROOT), attackFrames.get("def")));
        currentAttackFrame = 0;
    }

    /**
     * Updates the current animation frame (run/attack).
     */
    public void updateAnimation() {
        frameCounter++;
        if (frameCounter < FRAME_DELAY) {
            return;
        }
        frameCounter = 0;

        if (attacking && actualAttackFrames != null) {
            currentAttackFrame++;
            if (currentAttackFrame >= actualAttackFrames.length) {
                attacking = false;
                currentAttackFrame = 0;
            }
        } else if (runFrames != null && runFrames.length > 0) {
            currentFrame = (currentFrame + 1) % runFrames.length;
        }
    }

    /**
     * Renders the player sprite (run/attack) or a fallback rectangle.
     * @param g the graphics context
     */
    public void render(final Graphics g) {
        if (attacking && actualAttackFrames != null && actualAttackFrames[currentAttackFrame] != null) {
            if (!isFacingRight()) {
                g.drawImage(actualAttackFrames[currentAttackFrame], getX(), getY(), RENDER_SIZE, RENDER_SIZE, null);
            } else {
                g.drawImage(actualAttackFrames[currentAttackFrame], getX() + RENDER_SIZE, getY(),
                        -RENDER_SIZE, RENDER_SIZE, null);
            }
            return;
        }

        if (runFrames != null && runFrames[currentFrame] != null) {
            if (!isFacingRight()) {
                g.drawImage(runFrames[currentFrame], getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.drawImage(runFrames[currentFrame], getX() + RENDER_SIZE, getY(),
                        -RENDER_SIZE, getHeight(), null);
            }
        } else {
            g.setColor(Color.RED);
            g.fillRect(getX(), getY(), RENDER_SIZE, RENDER_SIZE);
        }
    }

    /**
     * Triggers an attack and its animation.
     */
    public void attack() {
        this.attacking = true;
        this.startAttackAnimation();
    }

    /**
     * Sets horizontal speed; also updates facing direction.
     * @param value horizontal speed in px/s (sign gives direction)
     */
    public void setSpeedX(final double value) {
        this.speedX = value;
        if (value > 0) {
            this.facingRight = true;
        } else if (value < 0) {
            this.facingRight = false;
        }
    }

    /**
     * Sets vertical speed.
     * @param value vertical speed in px/s
     */
    public void setSpeedY(final double value) {
        this.speedY = value;
    }

    /**
     * @return true if the player is currently facing right
     */
    @Override
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * Sets the current facing direction.
     * @param value true if facing right, false if facing left
     */
    @Override
    public void setFacingRight(final boolean value) {
        this.facingRight = value;
    }

    /**
     * Temporarily prevents movement for the given duration.
     * @param durationMillis immobilization duration in milliseconds
     */
    public void immobilize(final long durationMillis) {
        immobilized = true;
        setSpeedX(0);
        setSpeedY(0);

        SCHEDULER.schedule(() -> immobilized = false, durationMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * @return true if movement is currently disabled (immobilized)
     */
    public boolean isImmobilized() {
        return immobilized;
    }

    /**
     * Moves Bald on X/Y axes with collision against solid tiles.
     *
     * @param map       current tile map
     * @param deltaTime seconds since last update
     */
    public void move(final TileMap map, final double deltaTime) {
        if (isImmobilized()) {
            return;
        }
        if (map == null) {
            LoggerUtils.error("TileMap is null: skipping movement.");
            return;
        }
        final double dt = (deltaTime > 0 && !Double.isNaN(deltaTime)) ? deltaTime : (1.0 / 60.0);
        final double nextX = posX + speedX * dt * SPEED_MULTIPLIER;
        final double nextY = posY + speedY * dt * SPEED_MULTIPLIER;
        final Rectangle nextHitboxX = new Rectangle(
                (int) (nextX + (ENTITY_SIZE - HITBOX_WIDTH) / 2.0),
                (int) (posY + ENTITY_SIZE - HITBOX_HEIGHT),
                HITBOX_WIDTH, HITBOX_HEIGHT);

        boolean collisionX = false;
        final int leftX = Math.max(0, nextHitboxX.x / TILE_SIZE);
        final int rightX = Math.max(0, (nextHitboxX.x + nextHitboxX.width - 1) / TILE_SIZE);
        final int topX = Math.max(0, nextHitboxX.y / TILE_SIZE);
        final int bottomX = Math.max(0, (nextHitboxX.y + nextHitboxX.height - 1) / TILE_SIZE);

        outerX:
        for (int tx = leftX; tx <= rightX; tx++) {
            for (int ty = topX; ty <= bottomX; ty++) {
                final Tile tile = map.getTileAt(tx, ty);
                if (tile != null && tile.getId() == COLLISION_TILE_ID) {
                    collisionX = true;
                    break outerX;
                }
            }
        }

        final Rectangle nextHitboxY = new Rectangle(
                (int) (posX + (ENTITY_SIZE - HITBOX_WIDTH) / 2.0),
                (int) (nextY + ENTITY_SIZE - HITBOX_HEIGHT),
                HITBOX_WIDTH, HITBOX_HEIGHT);

        boolean collisionY = false;
        final int leftY = Math.max(0, nextHitboxY.x / TILE_SIZE);
        final int rightY = Math.max(0, (nextHitboxY.x + nextHitboxY.width - 1) / TILE_SIZE);
        final int topY = Math.max(0, nextHitboxY.y / TILE_SIZE);
        final int bottomY = Math.max(0, (nextHitboxY.y + nextHitboxY.height - 1) / TILE_SIZE);

        outerY:
        for (int tx = leftY; tx <= rightY; tx++) {
            for (int ty = topY; ty <= bottomY; ty++) {
                final Tile tile = map.getTileAt(tx, ty);
                if (tile != null && tile.getId() == COLLISION_TILE_ID) {
                    collisionY = true;
                    break outerY;
                }
            }
        }

        if (!collisionX) {
            posX = nextX;
        }
        if (!collisionY) {
            posY = nextY;
        }

        this.setX((int) (posX + 0.5));
        this.setY((int) (posY + 0.5));
    }

    /**
     * @return current horizontal speed in px/s
     */
    public double getSpeedX() {
        return speedX;
    }

    /**
     * @return current vertical speed in px/s
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     * @return the player's wallet instance
     */
    public Wallet getWallet() {
        return new Wallet(this.wallet);  // restituisci il wallet reale
    }

    /**
     * Adds coins to the player's wallet.
     * @param amount number of coins to add
     */
    public void addCoins(final int amount) {
        this.wallet.addCoins(amount);
    }

    /**
     * @return the current number of coins in the wallet
     */
    public int getCoins() {
        return this.wallet.getCoins();
    }

    /**
     * @return the currently equipped weapon, if any
     */
    public Optional<Weapon> getWeapon() {
        return weapon;
    }

    /**
     * Sets/equips the current weapon.
     * @param wpn the weapon to equip (nullable)
     */
    public void setWeapon(final Weapon wpn) {
        this.weapon = Optional.ofNullable(wpn);
    }

    /**
     * @return true if an attack is in progress (animation state)
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * @return precise X position in world coordinates (pixels)
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Sets precise X position and syncs integer render X.
     * @param value new X position in pixels
     */
    public void setPosX(final double value) {
        this.posX = value;
        setX((int) Math.round(value));
    }
    /**
     * @return precise Y position in world coordinates (pixels)
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Sets precise Y position and syncs integer render Y.
     * @param value new Y position in pixels
     */
    public void setPosY(final double value) {
        this.posY = value;
        setY((int) Math.round(value));
    }

    /**
     * @return sprite/render height in pixels
     */
    @Override
    public int getHeight() {
        return FRAME_HEIGHT;
    }

    /**
     * @return sprite/render width in pixels
     */
    @Override
    public int getWidth() {
        return FRAME_WIDTH;
    }

    /**
     * Returns the combat hitbox (small and centered),
     * not the rendering hitbox (large).
     */
    @Override
    public Rectangle getBounds() {
        final int xOffset = (int) ((ENTITY_SIZE - HITBOX_WIDTH) / 2.0);
        final int yOffset = ENTITY_SIZE - HITBOX_HEIGHT;
        return new Rectangle(getX() + xOffset, getY() + yOffset, HITBOX_WIDTH, HITBOX_HEIGHT);
    }
}
