package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.thelegendofbald.ui.view.TileMap;

public class Bald extends Entity {
    public static final String SpeedX = null;
    private TileMap tileMap;
    private int attackPower; // Potenza d'attacco
    private BufferedImage image;
    private String path = "/images/bald.png"; // Percorso dell'immagine
    public int speedX = 0; // Velocità lungo l'asse X
    public int speedY = 0; // Velocità lungo l'asse Y
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

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }    

    public void setSpawnPosition(int spawnTileId) {
        // Usa il metodo findSpawnPoint per ottenere le coordinate del tile con ID spawnTileId
        Point spawnPoint = tileMap.findSpawnPoint(spawnTileId);
        if (spawnPoint != null) {
            this.setX(spawnPoint.x); // Imposta la posizione X di Bald
            this.setY(spawnPoint.y); // Imposta la posizione Y di Bald
        }
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
                g.drawImage(runFrames[currentFrame], x, y, getWidth(), getHeight(), null);
            } else {
                // Disegna riflettendo l'immagine orizzontalmente
                g.drawImage(runFrames[currentFrame], x + 50, y, -50, getHeight(), null);
            }
        } else {
            // Fallback al quadrato rosso se i frame non sono caricati
            g.setColor(Color.RED);
            g.fillRect(x, y, 50, 50);
        }
    }
   
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }
    
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
        
    }
    
    public void move() {
        if (speedX > 0) {
            facingRight = false; // Bald si muove verso destra
        } else if (speedX < 0) {
            facingRight = true; // Bald si muove verso sinistra
        }
    
        // Spostamento
        this.x += speedX;
        this.y += speedY;
    }

    public int getHeight() {
        return 50;
    }

    public int getWidth() {
        return 50;
    }

}
