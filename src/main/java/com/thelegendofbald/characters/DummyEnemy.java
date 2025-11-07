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
 * Nemico semplice che segue il giocatore, reagisce ai colpi e ha un'animazione di morte.
 */
public final class DummyEnemy extends Entity implements Combatant {

    // ---- Stati del Nemico (NUOVO) ----
    private enum EnemyState {
        RUNNING, // Insegue il giocatore
        HURT,    // È stato colpito, animazione "hurt"
        DYING,   // Sta morendo, animazione "dead"
        DEAD     // Morto, pronto per essere rimosso
    }
    
    // ---- Costanti ----
    private static final int FRAME_WIDTH = 50;
    private static final int FRAME_HEIGHT = 50;
    private static final int RENDER_SIZE = 50;

    // Costanti animazione (MODIFICATE)
    private static final int RUN_FRAMES = 9;
    private static final int HURT_FRAMES = 5; // <-- MODIFICA (da 8 png)
    private static final int DEAD_FRAMES = 7; // <-- MODIFICA (da 8 png)
    private static final int DEFAULT_FRAME_DELAY = 5;

    private static final double DEFAULT_SPEED = 1.0;
    private static final double MIN_DISTANCE = 200;

    // ---- Stato ----
    private final int attackPower;
    private final transient TileMap tileMap;

    private final double speedX = DEFAULT_SPEED;
    private final double speedY = DEFAULT_SPEED;

    // Array delle animazioni (MODIFICATI)
    private BufferedImage[] runFrames;
    private BufferedImage[] hurtFrames;
    private BufferedImage[] deadFrames;
    
    private int currentFrame;
    private final int frameDelay = DEFAULT_FRAME_DELAY;
    private int frameCounter;

    private long lastAttackTime;
    private EnemyState state = EnemyState.RUNNING; // <-- MODIFICA: Stato iniziale

    /**
     * Crea un nemico.
     */
    public DummyEnemy(final int x, final int y, final int health, final String name,
                      final int attackPower, final TileMap tileMap) {
        super(x, y, FRAME_WIDTH, FRAME_HEIGHT, name, new LifeComponent(health));
        this.attackPower = attackPower;
        this.tileMap = Objects.requireNonNull(tileMap, "tileMap must not be null");
        loadFrames(); // <-- MODIFICA: Nome metodo cambiato
    }

    // ------------------- Resources -------------------

    /**
     * Carica TUTTI i frame di animazione (Run, Hurt, Dead).
     */
    private void loadFrames() {
        runFrames = new BufferedImage[RUN_FRAMES];
        hurtFrames = new BufferedImage[HURT_FRAMES];
        deadFrames = new BufferedImage[DEAD_FRAMES];

        // 1. Carica animazione RUN (Codice esistente)
        for (int i = 0; i < RUN_FRAMES; i++) {
            final String framePath = String.format("/images/dummyenemy_run/__TRAINEE_Run_00%d.png", i + 1);
            runFrames[i] = loadImage(framePath);
        }

        // 2. Carica animazione HURT (NUOVO)
        for (int i = 0; i < HURT_FRAMES; i++) {
            // --- ATTENZIONE: Correggi questo percorso! ---
            // Sostituisci "06-Hurt" e "Hurt_File_Name_00%d.png" con i tuoi percorsi e nomi file
            final String framePath = String.format("/images/dummyenemy_run/06-Hurt/__TRAINEE_Hurt_00%d.png", i + 1);
            hurtFrames[i] = loadImage(framePath);
        }

        // 3. Carica animazione DEAD (NUOVO)
        for (int i = 0; i < DEAD_FRAMES; i++) {
            // --- ATTENZIONE: Correggi questo percorso! ---
            // Sostituisci "07-Dead" e "Dead_File_Name_00%d.png" con i tuoi percorsi e nomi file
            final String framePath = String.format("/images/dummyenemy_run/07-Dead/__TRAINEE_Dead_00%d.png", i + 1);
            deadFrames[i] = loadImage(framePath);
        }
    }

    /**
     * Metodo helper per caricare una singola immagine. (NUOVO)
     */
    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            } else {
                LoggerUtils.error("Frame non trovato: " + path);
                return null;
            }
        } catch (final IOException e) {
            LoggerUtils.error("Errore caricamento frame " + path + ": " + e.getMessage());
            return null;
        }
    }

    // ------------------- Combatant -------------------

    @Override
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * Applica danno e cambia stato in HURT o DYING. (MODIFICATO)
     */
    @Override
    public void takeDamage(final int damage) {
        // Non può essere colpito se sta già morendo o è morto
        if (state == EnemyState.DYING || state == EnemyState.DEAD) {
            return;
        }

        this.getLifeComponent().damageTaken(damage);
        currentFrame = 0; // Resetta l'animazione
        frameCounter = 0;

        if (this.getLifeComponent().isDead()) {
            state = EnemyState.DYING; // Inizia l'animazione di morte
        } else {
            state = EnemyState.HURT; // Inizia l'animazione di colpo
        }
    }

    public void heal(final int amount) {
        this.getLifeComponent().heal(amount);
    }

    // ------------------- Animation & Render -------------------

    /**
     * Avanza l'animazione in base allo stato. (MODIFICATO)
     */
    public void updateAnimation() {
        frameCounter++;
        if (frameCounter < frameDelay) {
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
                    state = EnemyState.RUNNING; // Torna a correre
                }
                break;

            case DYING:
                if (currentFrame >= DEAD_FRAMES) {
                    currentFrame = DEAD_FRAMES - 1; // Rimane sull'ultimo frame
                    state = EnemyState.DEAD; // Ora è completamente morto
                }
                break;

            case DEAD:
                // Rimane sull'ultimo frame di morte
                currentFrame = DEAD_FRAMES - 1;
                break;
        }
    }

    /**
     * Rende il frame corretto in base allo stato. (MODIFICATO)
     */
    public void render(final Graphics g) {
        BufferedImage frame = null;

        // 1. Seleziona l'array di frame corretto
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
        
        // 2. Disegna il frame
        if (frame != null) {
            if (!isFacingRight()) {
                g.drawImage(frame, getX(), getY(), RENDER_SIZE, RENDER_SIZE, null);
            } else {
                g.drawImage(frame, getX() + RENDER_SIZE, getY(),
                            -RENDER_SIZE, RENDER_SIZE, null);
            }
        } else {
            // Fallback se le animazioni non sono caricate
            g.setColor(Color.RED);
            g.fillRect(getX(), getY(), RENDER_SIZE, RENDER_SIZE);
        }
    }

    // ------------------- AI & Movement -------------------

    /**
     * Segue il giocatore SOLO se è in stato RUNNING. (MODIFICATO)
     */
    public void followPlayer(final Bald bald) {
        // Non si muove se è colpito, sta morendo, è morto, o il player non c'è
        if (state != EnemyState.RUNNING || bald == null) {
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
    
    // ... (moveWithCollision e isColliding rimangono invariati) ...
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

    // ------------------- Misc -------------------

    /**
     * @return true se il nemico non è ancora nello stato DYING o DEAD
     */
    @Override
    public boolean isAlive() {
        // È "vivo" (può essere attaccato) finché non sta morendo
        return state == EnemyState.RUNNING || state == EnemyState.HURT;
    }

    /**
     * (NUOVO) Usato da GamePanel per sapere quando rimuovere il corpo.
     * @return true solo se il nemico è nello stato finale DEAD.
     */
    public boolean isRemovable() {
        return state == EnemyState.DEAD;
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
        final double dx = this.getX() - bald.getX();
        final double dy = this.getY() - bald.getY();
        final double distance = Math.hypot(dx, dy);
        return distance < MIN_DISTANCE;
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