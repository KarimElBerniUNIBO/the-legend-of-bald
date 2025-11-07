package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;

    /**
     * Crea il boss finale.
     *
     * @param x               posizione X di spawn
     * @param y               posizione Y di spawn
     * @param width           larghezza (attualmente ignorata, usa FRAME_WIDTH)
     * @param height          altezza (attualmente ignorata, usa FRAME_HEIGHT)
     * @param name            nome del boss
     * @param maxHealth       punti vita massimi
     * @param baseAttackPower potenza d'attacco base (fase 1)
     * @param lifeComponent   il componente vita
     * @param map             mappa per la gestione delle collisioni
     */
    public final class FinalBoss extends Entity implements Combatant {

        // --- Costanti di tuning ---
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

        // --- Costante per l'attivazione (ispirata a DummyEnemy.MIN_DISTANCE) ---
        private static final int AGGRO_RANGE_PX = 200; 

        // --- Costanti per Rendering & Animazione ---
        private static final int FRAME_WIDTH = 64;
        private static final int FRAME_HEIGHT = 64;
        private static final int RENDER_SIZE = 64;
        private static final int RUN_FRAMES = 2; 
        private static final int DEFAULT_FRAME_DELAY = 10;

        // --- Stato ---
        private final int baseAttackPower;
        private int phase = 1;
        private boolean alive = true;

        private final int maxHealth;
        private int health;

        // 'map' è l'equivalente di 'tileMap' di DummyEnemy
        private final TileMap map;
        private final int tileSize;

        private long lastDashAt = -DASH_COOLDOWN_MS;
        private long lastAoeAt = -AOE_COOLDOWN_MS;

        // --- Stato Animazione ---
        private BufferedImage[] runFrames;
        private int currentFrame;
        private final int frameDelay = DEFAULT_FRAME_DELAY;
        private int frameCounter;

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
            super(x, y, FRAME_WIDTH, FRAME_HEIGHT, name != null ? name : "Final Boss", lifeComponent);
            this.maxHealth = Math.max(1, maxHealth);
            this.health = this.maxHealth;
            this.baseAttackPower = baseAttackPower;
            // Salva la mappa (equivalente di tileMap in DummyEnemy)
            this.map = java.util.Objects.requireNonNull(map, "TileMap must not be null");
            this.tileSize = map.getTileSize();
            loadRunFrames(); 
            updatePhase();
        }

        // ============================================================
        // RISORSE E ANIMAZIONE
        // ============================================================

        // ... (loadRunFrames, updateAnimation - NESSUNA MODIFICA) ...

        private void loadRunFrames() {
            runFrames = new BufferedImage[RUN_FRAMES];
            for (int i = 0; i < RUN_FRAMES; i++) {
                // NOTA: questo carica la stessa immagine 2 volte. 
                // Se hai 2 frame (es. finalbossL_0.png, finalbossL_1.png), 
                // dovrai cambiare la stringa del path.
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
         * Avanza il frame di animazione in base al contatore.
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
         * Restituisce la potenza d'attacco del boss,
         * moltiplicata in base alla fase corrente.
         *
         * @return la potenza d'attacco attuale
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
         * Applica danno al boss, riducendo la sua salute
         * e aggiornando la sua fase se necessario.
         *
         * @param damage la quantità di danno da infliggere
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
         * @return il rettangolo di collisione (hitbox) del boss.
         */
        @Override
        public Rectangle getBounds() {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }

        /**
         * @return true se il boss ha salute > 0, altrimenti false.
         */
        @Override
        public boolean isAlive() {
            return alive;
        }

        /**
         * Gestisce l'IA del boss: insegue, attacca in melee, usa dash e AOE
         * solo se il giocatore è entro il raggio di "aggro".
         *
         * @param bald il giocatore (Bald) da seguire e attaccare
         */
        public void followPlayer(final Bald bald) {
            if (!alive || bald == null) {
                return;
            }
            final int dx = bald.getX() - getX();
            final int dy = bald.getY() - getY();
            final double dist = Math.hypot(dx, dy);

            // --- Logica di attivazione (Aggro Range) ---
            if (dist > AGGRO_RANGE_PX) {
                return; // Giocatore troppo lontano, non fare nulla
            }
            // --- Logica di Combattimento (Melee/AOE) ---
            if (dist <= MELEE_RANGE_PX) {
                performMelee(bald);
                tryAoe(bald);
                return; // In melee, non muoverti
            }

            // --- Logica di Combattimento (Dash) ---
            final long now = System.currentTimeMillis();
            if ((dist >= DASH_MIN_DISTANCE_PX && (now - lastDashAt) >= DASH_COOLDOWN_MS) && tryDash(dx, dy)) {
                lastDashAt = now;
                return; // Ha appena dashato, non fare movimento normale
            }

            // --- Movimento Normale (Logica di DummyEnemy) ---
            double moveDx = 0.0;
            double moveDy = 0.0;
            // Usa la velocità della fase corrente
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

            // Usa il metodo di movimento di DummyEnemy
            moveWithCollision(moveDx, moveDy);
        }

        public int getHealth() { return health; }
        public int getMaxHealth() { return maxHealth; }
        public int getPhase() { return phase; }

        // ============================================================
        // METODI PRIVATI
        // ============================================================

        // ... (getCurrentSpeed, updatePhase, performMelee, tryAoe - NESSUNA MODIFICA) ...
        private int getCurrentSpeed() {
            switch (phase) {
                case 3: return PHASE3_SPEED;
                case 2: return PHASE2_SPEED;
                default: return PHASE1_SPEED;
            }
        }

        private void updatePhase() {
            final double ratio = (double) health / maxHealth; 
            if (ratio <= PHASE3_THRESHOLD) { phase = 3; }
            else if (ratio <= PHASE2_THRESHOLD) { phase = 2; }
            else { phase = 1; }
        }
        
        private void performMelee(final Bald bald) {
            try { bald.takeDamage(getAttackPower()); } catch (Exception ignored) { }
        }

        private void tryAoe(final Bald bald) {
            final long now = System.currentTimeMillis();
            if ((now - lastAoeAt) < AOE_COOLDOWN_MS) {
                return;
            }
            final int dx = bald.getX() - getX();
            final int dy = bald.getY() - getY();
            if (Math.hypot(dx, dy) <= AOE_RANGE_PX) {
                try { bald.takeDamage(getAttackPower() + AOE_EXTRA_DAMAGE); } catch (Exception ignored) { }
                lastAoeAt = now;
            }
        }
        
        /**
         * Logica Dash, modificata per usare il nuovo isColliding.
         */
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
                
                // --- MODIFICA ---
                // Usa isColliding (che ritorna TRUE se c'è collisione)
                // al posto di canMoveTo (che ritornava TRUE se era libero)
                if (isColliding(nextX, nextY)) { 
                // --- FINE MODIFICA ---
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

        // --- NUOVI METODI DI MOVIMENTO (da DummyEnemy) ---
        // (tryMove e canMoveTo sono stati rimossi)

        /**
         * Moves the entity while checking collisions on the tile map.
         * (Preso da DummyEnemy)
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
         * (Preso da DummyEnemy e adattato)
         *
         * @param nextX next X position
         * @param nextY next Y position
         * @return true if colliding with a solid tile
         */
        private boolean isColliding(final double nextX, final double nextY) {
            // Usa 'this.map' e 'this.tileSize' di FinalBoss
            final int entityWidth = getWidth();
            final int entityHeight = getHeight();

            final int leftTile = (int) nextX / tileSize;
            final int rightTile = (int) (nextX + entityWidth - 1) / tileSize;
            final int topTile = (int) nextY / tileSize;
            final int bottomTile = (int) (nextY + entityHeight - 1) / tileSize;

            for (int row = topTile; row <= bottomTile; row++) {
                for (int col = leftTile; col <= rightTile; col++) {
                    final Tile tile = map.getTileAt(col, row); // Usa 'map'
                    if (tile != null && tile.isSolid()) {
                        return true;
                    }
                }
            }
            return false;
        }


        // ============================================================
        // RENDER
        // ============================================================
        
        /**
         * Renders the current frame.
         * (Logica di specchiamento identica a DummyEnemy)
         */
        public void render(final Graphics g) {
            
            final BufferedImage frame =
                (runFrames != null && runFrames.length > 0) ? runFrames[currentFrame % runFrames.length] : null;

            if (frame != null) {
                // Se l'asset "finalbossL.png" guarda a sinistra (come da nome):
                if (!isFacingRight()) { 
                    // Guarda a SINISTRA -> Disegna immagine NORMALE
                    g.drawImage(frame, getX(), getY(), RENDER_SIZE, RENDER_SIZE, null);
                } else {
                    // Guarda a DESTRA -> Disegna immagine SPECCHIATA
                    g.drawImage(frame, getX() + RENDER_SIZE, getY(),
                                -RENDER_SIZE, RENDER_SIZE, null);
                }
            } else {
                // Fallback
                g.setColor(Color.RED);
                g.fillRect(getX(), getY(), RENDER_SIZE, RENDER_SIZE);
            }
            
            // Disegna HP Bar
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


        // ============================================================
        // OVERRIDES (Dimensioni)
        // ============================================================

        @Override
        public int getWidth() {
            return FRAME_WIDTH;
        }

        @Override
        public int getHeight() {
            return FRAME_HEIGHT;
        }
}
