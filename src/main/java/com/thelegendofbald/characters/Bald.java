package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import java.awt.Rectangle;

import javax.imageio.ImageIO;

import com.thelegendofbald.view.main.TileMap;

public class Bald extends Entity {
    private int attackPower; // Potenza d'attacco
    private BufferedImage image;
    private String path = "/images/bald.png"; // Percorso dell'immagine
    private double speedX = 0.0; // Velocità lungo l'asse X
    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    private double speedY = 0.0; // Velocità lungo l'asse Y
    private BufferedImage[] runFrames; // Array di immagini per l'animazione della corsa
    private int currentFrame = 0; // Indice del frame corrente
    private int frameDelay = 5; // Numero di aggiornamenti prima di cambiare frame
    private int frameCounter = 0; // Contatore per il ritardo tra i frame
    private boolean facingRight = false; // Direzione in cui Bald sta guardando

    public Bald(int x, int y, int health, String name, int attackPower ) {
        super(x, y, health, name);
        this.attackPower = attackPower;

        loadRunFrames();
    }

    private void loadRunFrames() {
        try {
            int numFrames = 9; // Supponiamo di avere 6 frame
            runFrames = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                String framePath = String.format("/images/bald_run/PS_BALD GUY_Run_00%d.png", i + 1); // Percorso dei frame
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

    public void setAttackPower(int attackPower) { 
        this.attackPower = attackPower; 
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

    public void attack(Entity target) {

            target.setHealth(target.getHealth() - attackPower);

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
            if (facingRight) {
                // Disegna normalmente se Bald è girato verso destra
                g.drawImage(runFrames[currentFrame], x, y, 50, 50, null);
            } else {
                // Disegna riflettendo l'immagine orizzontalmente
                g.drawImage(runFrames[currentFrame], x + 50, y, -50, 50, null);
            }
        } else {
            // Fallback al quadrato rosso se i frame non sono caricati
            g.setColor(Color.RED);
            g.fillRect(x, y, 50, 50);
        }
    }
   
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
        //this.updateAnimation();
    }
    
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
        //this.updateAnimation();
    }
    
    public void move(TileMap tileMap) {

        int hitboxX = 15;
        int hitboxY = 25;
        
        //Check for hitbox on x
        double nextX = x + speedX;
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
                if (tileMap.getTile(tx, ty) == 2) {
                    collisionX = true;
                    break;
                }
            }
            if (collisionX) break;
        }

        if (!collisionX) {
            x = (int) nextX;
        }

        //Check for hitbox on y
        double nextY = y + speedY;
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
                if (tileMap.getTile(tx, ty) == 2) {
                    collisionY = true;
                    break;
                }
            }
            if (collisionY) break;
        }

        if (!collisionY) {
            y = (int) nextY;
        }
    }

    public Rectangle getHitbox() {
        int width = 15;
        int height = 25;
        int offsetX = x + (50 - width) / 2;
        int offsetY = y + (50 - height) / 2;
        return new Rectangle(offsetX, offsetY, width, height);
    }
}
