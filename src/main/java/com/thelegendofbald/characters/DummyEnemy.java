package com.thelegendofbald.characters;

import java.awt.Color;
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

public class DummyEnemy extends Entity  implements Combatant{

    private static final int WIDTH = 50; // Larghezza del frame
    private static final int HEIGHT = 50; // Altezza del frame

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
    private final double minDistance = 100;
    private transient TileMap tileMap;

    public DummyEnemy(int x, int y, int health, String name, int attackPower, TileMap tileMap) {
        super(x, y, WIDTH, HEIGHT, name, new LifeComponent(health));
        this.attackPower = attackPower;
        this.tileMap = tileMap;
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
                    LoggerUtils.error("Frame " + framePath + " not found");
                }
            }
        } catch (IOException e) {
            LoggerUtils.error("Error loading run frames: " + e.getMessage());
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

    public void followPlayer(Bald bald) {
        double dx = 0;
        double dy = 0;

        if (bald.getX() > this.x) {
            dx = speedX;
            facingRight = true;
        } else if (bald.getX() < this.x) {
            dx = -speedX;
            facingRight = false;
        }

        if (bald.getY() > this.y) {
            dy = speedY;
        } else if (bald.getY() < this.y) {
            dy = -speedY;
        }

        move(dx, dy);
    }

    private void move(double dx, double dy) {
        double nextX = x + dx;
        double nextY = y + dy;

        // Controlla collisione sull'asse X
        if (!isColliding(nextX, y)) {
            x = (int) nextX;
        }

        // Controlla collisione sull'asse Y
        if (!isColliding(x, nextY)) {
            y = (int) nextY;
        }
    }

    private boolean isColliding(double nextX, double nextY) {
        if (tileMap == null) {
            return false; // Non fare nulla se la mappa non è impostata
        }

        int tileSize = tileMap.getTileSize();
        int entityWidth = getWidth();
        int entityHeight = getHeight();

        // Calcola i tile con cui la bounding box potrebbe collidere
        int leftTile = (int) nextX / tileSize;
        int rightTile = (int) (nextX + entityWidth - 1) / tileSize;
        int topTile = (int) nextY / tileSize;
        int bottomTile = (int) (nextY + entityHeight - 1) / tileSize;

        for (int row = topTile; row <= bottomTile; row++) {
            for (int col = leftTile; col <= rightTile; col++) {
                Tile tile = tileMap.getTileAt(col, row);
                if (tile != null && tile.isSolid()) {
                    return true; // Collisione rilevata
                }
            }
        }
        return false; // Nessuna collisione
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
}
