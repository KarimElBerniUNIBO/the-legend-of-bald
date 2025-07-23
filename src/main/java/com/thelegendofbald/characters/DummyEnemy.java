package com.thelegendofbald.characters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import com.thelegendofbald.view.main.TileMap;

public class DummyEnemy extends Entity {

    private BufferedImage spritesheet; 
    private BufferedImage walkFrames[];
    private BufferedImage image; // Variable to store the loaded image

    private int attackPower; 

    private int width = 128; // Larghezza del frame
    private int height = 128; // Altezza del frame

    private BufferedImage[] runFrames; // Array di immagini per l'animazione della corsa
    private int currentFrame = 0; // Indice del frame corrente
    private int frameDelay = 5; // Numero di aggiornamenti prima di cambiare frame
    private int frameCounter = 0; // Contatore per il ritardo tra i frame

    private boolean facingRight = false; // Direzione in cui Bald sta guardando

    private double posX, posY; // posizione reale

    public DummyEnemy(int x, int y, int health, String name, int attackPower) {
        super(x, y, health, name);
        this.attackPower = attackPower;
        this.posX = x;
        this.posY = y;
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
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0; // Health non può essere negativa
        }
    }

    public void heal(int amount) {
        this.health += amount;
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
        double dirX = bald.getX() - this.posX;
        double dirY = bald.getY() - this.posY;

        // Normalizzo
        double magnitude = Math.hypot(dirX, dirY);
        double speedX = 0;
        double speedY = 0;
        if (magnitude != 0) {
            speedX = dirX / magnitude;
            speedY = dirY / magnitude;
        }

        facingRight = speedX >= 0;

        double nextX = posX + speedX * deltaTime * MOVE_SPEED;
        Rectangle nextHitboxX = new Rectangle(
            (int)(nextX + (50 - hitboxX) / 2),
            (int)(posY + (50 - hitboxY) / 2),
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
                if (tileMap.getTile(tx, ty) == 2) {
                    collisionX = true;
                    break;
                }
            }
            if (collisionX) break;
        }

        if (!collisionX) {
            posX = nextX;
        }

        double nextY = posY + speedY * deltaTime * MOVE_SPEED;
        Rectangle nextHitboxY = new Rectangle(
            (int)(posX + (50 - hitboxX) / 2),
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
                if (tileMap.getTile(tx, ty) == 2) {
                    collisionY = true;
                    break;
                }
            }
            if (collisionY) break;
        }

        if (!collisionY) {
            posY = nextY;
        }

        this.x = (int) posX;
        this.y = (int) posY;
    }

 

    
}
