package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.effects.StatusEffect;
import com.thelegendofbald.effects.StatusEffectManager;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.model.common.Wallet;
import com.thelegendofbald.model.weapons.Weapon;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;

/**
 * Main player character with movement, animation and tile-based collisions.
 */
public class Bald extends Entity implements Combatant {

    // ---- Constants (no magic numbers) ----
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

    // ---- State ----
    private final Wallet wallet = new Wallet(0);
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
    private int frameDelay = DEFAULT_FRAME_DELAY;
    private int frameCounter;

    private boolean attacking;
    private int currentAttackFrame;
    private boolean facingRight = true;
    private boolean immobilized;

    /**
     * Creates a Bald instance.
     */
    public Bald(final int x, final int y, final int maxHealth, final String name, final int baseAttackPower) {
        super(x, y, FRAME_WIDTH, FRAME_HEIGHT, name, new LifeComponent(maxHealth));
        this.attackPower = baseAttackPower;
        loadRunFrames();
        loadAllAttackFrames();
    }

    // ------------------- Resources -------------------

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
            } catch (IOException e) {
                LoggerUtils.error("Error loading run frame " + framePath + ": " + e.getMessage());
            }
        }
    }

    private void loadAllAttackFrames() {
        final String basePath = "/images/bald_attack";
        final List<String> weaponNames = List.of("def", "sword", "axe");

        for (String weaponName : weaponNames) {
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
                } catch (IOException e) {
                    LoggerUtils.error("Error loading attack frame " + framePath + ": " + e.getMessage());
                }
            });

            attackFrames.put(weaponName, frames);
        }

        LoggerUtils.info("Attack frames loaded: " + attackFrames.size());
    }

    // ------------------- Spawn / Map -------------------

    public void setTileMap(final TileMap map) {
        this.tileMap = map;
    }

    /**
     * Places Bald centered on a spawn tile (feet placed on ground).
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

            this.x = (int) Math.round(newPosX);
            this.y = (int) Math.round(newPosY);
        } else {
            LoggerUtils.error("Spawn point not found for tile id: " + spawnTileId);
        }
    }

    // ------------------- Combat / Effects -------------------

    @Override
    public int getAttackPower() {
        return buffManager.modifyAttackPower(attackPower);
    }

    public void setAttackPower(final int value) {
        this.attackPower = value;
    }

    public void applyBuff(final StatusEffect buff) {
        buffManager.applyEffect(buff);
    }

    public void updateBuffs() {
        buffManager.update();
    }

    @Override
    public void takeDamage(final int damage) {
        this.lifeComponent.damageTaken(damage);
        LoggerUtils.info("Player took damage: " + damage
                + ". Current health: " + lifeComponent.getCurrentHealth());
    }

    public boolean isAlive() {
        return !this.lifeComponent.isDead();
    }

    public boolean canShoot() {
        return true;
    }

    public void shootProjectile() {
        throw new UnsupportedOperationException("Unimplemented method 'shootProjectile'");
    }

    // ------------------- Animation -------------------

    public void startAttackAnimation() {
        weapon.ifPresent(w ->
                actualAttackFrames = attackFrames.getOrDefault(
                        w.getName().toLowerCase(), attackFrames.get("def")));
        currentAttackFrame = 0;
    }

    public void updateAnimation() {
        frameCounter++;
        if (frameCounter < frameDelay) {
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

    public void render(final Graphics g) {
        if (attacking && actualAttackFrames != null && actualAttackFrames[currentAttackFrame] != null) {
            if (!facingRight) {
                g.drawImage(actualAttackFrames[currentAttackFrame], x, y, RENDER_SIZE, RENDER_SIZE, null);
            } else {
                g.drawImage(actualAttackFrames[currentAttackFrame], x + RENDER_SIZE, y, -RENDER_SIZE, RENDER_SIZE, null);
            }
            return;
        }

        if (runFrames != null && runFrames[currentFrame] != null) {
            if (!facingRight) {
                g.drawImage(runFrames[currentFrame], x, y, getWidth(), getHeight(), null);
            } else {
                g.drawImage(runFrames[currentFrame], x + RENDER_SIZE, y, -RENDER_SIZE, getHeight(), null);
            }
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, RENDER_SIZE, RENDER_SIZE);
        }
    }

    public void attack() {
        this.attacking = true;
        this.startAttackAnimation();
    }

    // ------------------- Movement & Collisions -------------------

    public void setSpeedX(final double value) {
        this.speedX = value;
        if (value > 0) {
            this.facingRight = true;
        } else if (value < 0) {
            this.facingRight = false;
        }
    }

    public void setSpeedY(final double value) {
        this.speedY = value;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(final boolean value) {
        this.facingRight = value;
    }

    public void immobilize(final long durationMillis) {
        immobilized = true;
        setSpeedX(0);
        setSpeedY(0);

        SCHEDULER.schedule(() -> immobilized = false, durationMillis, TimeUnit.MILLISECONDS);
    }

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

        // X axis sweep
        final Rectangle nextHitboxX = new Rectangle(
                (int) (nextX + (ENTITY_SIZE - HITBOX_WIDTH) / 2.0),
                (int) (posY + (ENTITY_SIZE - HITBOX_HEIGHT) / 2.0),
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

        // Y axis sweep
        final Rectangle nextHitboxY = new Rectangle(
                (int) (posX + (ENTITY_SIZE - HITBOX_WIDTH) / 2.0),
                (int) (nextY + (ENTITY_SIZE - HITBOX_HEIGHT) / 2.0),
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

        this.x = (int) Math.round(posX);
        this.y = (int) Math.round(posY);
    }

    // ------------------- Accessors -------------------

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Optional<Weapon> getWeapon() {
        return weapon;
    }

    public void setWeapon(final Weapon wpn) {
        this.weapon = Optional.ofNullable(wpn);
    }

    public boolean isAttacking() {
        return attacking;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(final double value) {
        this.posX = value;
        this.x = (int) Math.round(value);
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(final double value) {
        this.posY = value;
        this.y = (int) Math.round(value);
    }

    @Override
    public int getHeight() {
        return FRAME_HEIGHT;
    }

    @Override
    public int getWidth() {
        return FRAME_WIDTH;
    }
}
