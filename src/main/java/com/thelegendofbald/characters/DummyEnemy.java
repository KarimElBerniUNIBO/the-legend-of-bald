package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import java.awt.Rectangle;
import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;

public class DummyEnemy extends Entity  implements Combatant{

    private static final int WIDTH = 128; // Larghezza del frame
    private static final int HEIGHT = 128; // Altezza del frame

    private BufferedImage spritesheet; 
    private BufferedImage walkFrames[];
    private BufferedImage image; // Variable to store the loaded image

    private int attackPower; 
    private int speedY = 1;
    private int speedX = 1;

    private BufferedImage[] runFrames; // Array di immagini per l'animazione della corsa
    private int currentFrame = 0; // Indice del frame corrente
    private int frameDelay = 5; // Numero di aggiornamenti prima di cambiare frame
    private int frameCounter = 0; // Contatore per il ritardo tra i frame

    private long lastAttackTime = 0; // Tempo dell'ultimo attacco
    private final double minDistance = 150;

    private TileMap tileMap;

    public DummyEnemy(int x, int y, int health, String name, int attackPower) {
        super(x, y, WIDTH, HEIGHT, name, new LifeComponent(health));
        this.attackPower = attackPower;
        loadRunFrames();
    }
    private String path = "/images/enemyWalkSpritesheet.png"; 

    private void loadRunFrames() {
        try {
            int numFrames = 9; // Supponiamo di avere 6 frame
            runFrames = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                String framePath = String.format("/images/dummyenemy_run/__TRAINEE_Run_00%d.png", i + 1); // Percorso dei frame
                InputStream is = getClass().getResourceAsStream(framePath);
                if (is != null) {
                    runFrames[i] = ImageIO.read(is);
                } else {
                    System.err.println("Frame " + framePath + " not found");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAttackPower() { 
        return attackPower; 
    }


    public void takeDamage(int damage) {
       this.lifeComponent.damageTaken(damage);
    }

    public void heal(int amount) {
        this.lifeComponent.heal(amount);
    }



    public void updateAnimation() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            currentFrame = (currentFrame + 1) % runFrames.length; // Cicla tra i frame
        }
    }

    
    public void render(Graphics g) {
        if (runFrames != null && runFrames[currentFrame] != null) {
            if (!facingRight) {
                // Draw normally if facing right
                g.drawImage(runFrames[currentFrame], x, y, 50, 50, null);
            } else {
                // Draw flipped horizontally without changing the position
                g.drawImage(runFrames[currentFrame], x + 50, y, -50, 50, null);
            }
        } else {
            // Fallback to a red square if frames are not loaded
            g.setColor(Color.RED);
            g.fillRect(x, y, 50, 50);
        }
    }

    private static final double MOVE_SPEED = 90; // pixels per second

    /**
     * Aggiorna la posizione del nemico seguendo il giocatore, evitando ostacoli sulla mappa.
     *
     * @param bald il giocatore da inseguire
     * @param tileMap la mappa di gioco
     * @param deltaTime il tempo trascorso dall'ultimo aggiornamento
     */
    public void followPlayer(final Bald bald, final TileMap tileMap, final double deltaTime) {
        final int HITBOX_WIDTH = 15;
        final int HITBOX_HEIGHT = 25;
        final int ENTITY_SIZE = 50;
        final int TILE_SIZE = 32;

        final double dirX = bald.getX() - this.x;
        final double dirY = bald.getY() - this.y;

        final double magnitude = Math.hypot(dirX, dirY);
        final double speedX = (magnitude != 0) ? dirX / magnitude : 0;
        final double speedY = (magnitude != 0) ? dirY / magnitude : 0;

        facingRight = speedX >= 0;

        final double nextX = x + speedX * deltaTime * MOVE_SPEED;
        final Rectangle nextHitboxX = new Rectangle(
            (int) (nextX + (ENTITY_SIZE - HITBOX_WIDTH) / 2),
            (int) (y + (ENTITY_SIZE - HITBOX_HEIGHT) / 2),
            HITBOX_WIDTH, HITBOX_HEIGHT
        );

        final int leftX = nextHitboxX.x / TILE_SIZE;
        final int rightX = (nextHitboxX.x + nextHitboxX.width - 1) / TILE_SIZE;
        final int topX = nextHitboxX.y / TILE_SIZE;
        final int bottomX = (nextHitboxX.y + nextHitboxX.height - 1) / TILE_SIZE;

        boolean collisionX = false;
        outerX:
        for (int tx = leftX; tx <= rightX; tx++) {
            for (int ty = topX; ty <= bottomX; ty++) {
                final Tile tileX = tileMap.getTileAt(tx, ty);
                if (tileX != null && tileX.getId() == 2) {
                    collisionX = true;
                    break outerX;
                }
            }
        }

        if (!collisionX) {
            x = (int) nextX;
        }

        final double nextY = y + speedY * deltaTime * MOVE_SPEED;
        final Rectangle nextHitboxY = new Rectangle(
            (int) (x + (ENTITY_SIZE - HITBOX_WIDTH) / 2),
            (int) (nextY + (ENTITY_SIZE - HITBOX_HEIGHT) / 2),
            HITBOX_WIDTH, HITBOX_HEIGHT
        );

        final int leftY = nextHitboxY.x / TILE_SIZE;
        final int rightY = (nextHitboxY.x + nextHitboxY.width - 1) / TILE_SIZE;
        final int topY = nextHitboxY.y / TILE_SIZE;
        final int bottomY = (nextHitboxY.y + nextHitboxY.height - 1) / TILE_SIZE;

        boolean collisionY = false;
        outerY:
        for (int tx = leftY; tx <= rightY; tx++) {
            for (int ty = topY; ty <= bottomY; ty++) {
                final Tile tileY = tileMap.getTileAt(tx, ty);
                if (tileY != null && tileY.getId() == 2) {
                    collisionY = true;
                    break outerY;
                }
            }
        }

        if (!collisionY) {
            y = (int) nextY;
        }

        this.setX((int) x);
        this.setY((int) y);
    }


    public boolean isAlive() {
        return !this.lifeComponent.isDead();
    }

 
    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long time) {
        this.lastAttackTime = time;
    }

    public boolean isCloseTo(Bald bald) {
            double dx = this.x - bald.getX();
            double dy = this.y - bald.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < this.minDistance;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }    

    public void setSpawnPosition(int spawnTileId, int tileSize) {
        Point spawnPoint = tileMap.findSpawnPoint(spawnTileId);
        if (spawnPoint != null) {
            // Centra i piedi di Bald nel tile di spawn
            int x = spawnPoint.x + (tileSize - getWidth()) / 2;
            int y = spawnPoint.y + tileSize - getHeight();
            this.setX(x);
            this.setY(y);
        }
    }
}
