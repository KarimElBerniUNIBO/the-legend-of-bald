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
    private final double minDistance = 1000;

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

    public void followPlayer(Bald bald, TileMap tileMap, double deltaTime) {
        int hitboxX = 15;
        int hitboxY = 25;

        // Calcola la direzione verso Bald
        double dirX = bald.getX() - this.x;
        double dirY = bald.getY() - this.y;

        // Normalizzo
        double magnitude = Math.hypot(dirX, dirY);
        double speedX = 0;
        double speedY = 0;
        if (magnitude != 0) {
            speedX = dirX / magnitude;
            speedY = dirY / magnitude;
        }

        facingRight = speedX >= 0;

        double nextX = x + speedX * deltaTime * MOVE_SPEED;
        Rectangle nextHitboxX = new Rectangle(
            (int)(nextX + (50 - hitboxX) / 2),
            (int)(y + (50 - hitboxY) / 2),
            hitboxX, hitboxY
        );

        int tileSize = 32;
        int leftX = nextHitboxX.x / tileSize;
        int rightX = (nextHitboxX.x + nextHitboxX.width - 1) / tileSize;
        int topX = nextHitboxX.y / tileSize;
        int bottomX = (nextHitboxX.y + nextHitboxX.height - 1) / tileSize;

        boolean collisionX = false;
        for (int tx = leftX; tx <= rightX; tx++) {
            for (int ty = topX; ty <= bottomX; ty++) {
                Tile tileX = tileMap.getTileAt(tx, ty);
                if (tileX != null && tileX.getId() == 2) {
                    collisionX = true;
                    break;
                }
            }
            if (collisionX) break;
        }

        if (!collisionX) {
            x = (int) nextX;
        }

        double nextY = y + speedY * deltaTime * MOVE_SPEED;
        Rectangle nextHitboxY = new Rectangle(
            (int)(x + (50 - hitboxX) / 2),
            (int)(nextY + (50 - hitboxY) / 2),
            hitboxX, hitboxY
        );

        int leftY = nextHitboxY.x / tileSize;
        int rightY = (nextHitboxY.x + nextHitboxY.width - 1) / tileSize;
        int topY = nextHitboxY.y / tileSize;
        int bottomY = (nextHitboxY.y + nextHitboxY.height - 1) / tileSize;

        boolean collisionY = false;
        for (int tx = leftY; tx <= rightY; tx++) {
            for (int ty = topY; ty <= bottomY; ty++) {
                Tile tileY = tileMap.getTileAt(tx, ty);
                if (tileY != null && tileY.getId() == 2) {
                    collisionY = true;
                    break;
                }
            }
            if (collisionY) break;
        }

        if (!collisionY) {
            y = (int) nextY;
        }

        this.x = (int) x;
        this.y = (int) y;
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
