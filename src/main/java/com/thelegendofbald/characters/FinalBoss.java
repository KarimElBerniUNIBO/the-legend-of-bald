package com.thelegendofbald.characters;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;
import java.awt.Rectangle;

/**
 * FinalBoss — nemico finale che estende Entity e implementa Combatant.
 * Ha 3 fasi, un dash per avvicinarsi e un attacco ad area (AOE).
 */
public class FinalBoss extends Entity implements Combatant {

    // --- Costanti di tuning ---
    private static final double PHASE2_THRESHOLD = 0.66; // < 66% HP → fase 2
    private static final double PHASE3_THRESHOLD = 0.33; // < 33% HP → fase 3

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

    // --- Stato ---
    private final int baseAttackPower;
    private int phase = 1;
    private boolean alive = true;

    private final int maxHealth;
    private int health;

    private final TileMap map;
    private final int tileSize;

    private long lastDashAt = -DASH_COOLDOWN_MS;
    private long lastAoeAt  = -AOE_COOLDOWN_MS;

    /**
     * Crea il boss finale.
     */
    public FinalBoss(final int x, final int y,
                     final int width, final int height,
                     final String name,
                     final int maxHealth,
                     final int baseAttackPower,
                     final LifeComponent lifeComponent,
                     final TileMap map) {
        super(x, y, width, height, name != null ? name : "Final Boss", lifeComponent);
        this.maxHealth = Math.max(1, maxHealth);
        this.health = this.maxHealth;
        this.baseAttackPower = baseAttackPower;
        this.map = map;
        this.tileSize = (map != null) ? map.getTileSize() : 32;
        updatePhase();
    }

    // ============================================================
    // Combatant IMPLEMENTATION
    // ============================================================

    @Override
    public int getAttackPower() {
        switch (phase) {
            case 3: return (int) Math.round(baseAttackPower * PHASE3_ATK_MULT);
            case 2: return (int) Math.round(baseAttackPower * PHASE2_ATK_MULT);
            default: return (int) Math.round(baseAttackPower * PHASE1_ATK_MULT);
        }
    }

    @Override
    public void takeDamage(final int damage) {
        if (!alive || damage <= 0){
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

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    // ============================================================
    // LOGICA DEL BOSS
    // ============================================================

    public void followPlayer(final Bald bald) {
        if (!alive || bald == null) {
            return;
        }
        final int dx = bald.getX() - getX();
        final int dy = bald.getY() - getY();
        final double dist = Math.hypot(dx, dy);

        // Se vicino: attacco melee e prova AOE
        if (dist <= MELEE_RANGE_PX) {
            performMelee(bald);
            tryAoe(bald);
            return;
        }

        final long now = System.currentTimeMillis();
        // Dash se lontano
        if ( (dist >= DASH_MIN_DISTANCE_PX && (now - lastDashAt) >= DASH_COOLDOWN_MS ) && tryDash(dx, dy)) {
            lastDashAt = now;
            return;
        }

        // Movimento normale
        final int speed = getCurrentSpeed();
        final int stepX = (int) Math.signum(dx) * speed;
        final int stepY = (int) Math.signum(dy) * speed;
        tryMove(stepX, stepY);
        setFacingRight(stepX >= 0);
    }

    public int getHealth()     { return health; }
    public int getMaxHealth()  { return maxHealth; }
    public int getPhase()      { return phase; }

    // ============================================================
    // METODI PRIVATI
    // ============================================================

    private int getCurrentSpeed() {
        switch (phase) {
            case 3: return PHASE3_SPEED;
            case 2: return PHASE2_SPEED;
            default: return PHASE1_SPEED;
        }
    }

    private void updatePhase() {
        final int ratio =  health / maxHealth;
        if (ratio <= PHASE3_THRESHOLD) {
            phase = 3;
        }
        else if (ratio <= PHASE2_THRESHOLD) {
            phase = 2;
        }
        else {
            phase = 1;
        }
    }
    //DA SISTEMARE
    private void performMelee(final Bald bald) {
        try {
            bald.takeDamage(getAttackPower());
        } catch (Exception ignored) { }
    }

    private void tryAoe(final Bald bald) {
        final long now = System.currentTimeMillis();
        if ((now - lastAoeAt) < AOE_COOLDOWN_MS) {
            return;
        }

        final int dx = bald.getX() - getX();
        final int dy = bald.getY() - getY();

        //DA SISTEMARE
        if (Math.hypot(dx, dy) <= AOE_RANGE_PX) {
            try {
                bald.takeDamage(getAttackPower() + AOE_EXTRA_DAMAGE);
            } catch (Exception ignored) { }
            lastAoeAt = now;
        }
    }

    private boolean tryDash(final int dx, final int dy) {
        if (map == null) {
            return false;
        }
        double len = Math.hypot(dx, dy);
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
            if (!canMoveTo(nextX, nextY)) {
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

    private void tryMove(final int stepX, final int stepY) {
        if (stepX != 0 && canMoveTo(getX() + stepX, getY())) {
            setX(getX() + stepX);
        }
        if (stepY != 0 && canMoveTo(getX(), getY() + stepY)) {
            setY(getY() + stepY);
        }
    }

    private boolean canMoveTo(final int nextX, final int nextY) {
        if (map == null) {
            return true;
        }

        final int left   = nextX / tileSize;
        final int right  = (nextX + getWidth() - 1) / tileSize;
        final int top    = nextY / tileSize;
        final int bottom = (nextY + getHeight() - 1) / tileSize;

        return isTileFree(left, top)
            && isTileFree(right, top)
            && isTileFree(left, bottom)
            && isTileFree(right, bottom);
    }

    private boolean isTileFree(final int tx, final int ty) {
        final Tile t = map.getTileAt(tx, ty);
        return t == null || !t.isSolid();
    }
}
