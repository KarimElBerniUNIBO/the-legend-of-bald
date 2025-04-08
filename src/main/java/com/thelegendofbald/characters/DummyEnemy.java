package com.thelegendofbald.characters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class DummyEnemy extends Entity {

    private BufferedImage spritesheet; 
    private BufferedImage walkFrames[];

    private int attackPower; 
    private int speedY = 2;
    private int speedX = 2;


    private int currentFrame = 0; // Indice del frame corrente
    private int frameDelay = 5;  // Numero di aggiornamenti prima di cambiare frame
    private int frameCounter = 0; // Contatore per il ritardo tra i frame

    private int width = 128; // Larghezza del frame
    private int height = 128; // Altezza del frame
    

    public DummyEnemy(int x, int y, int health, String name, int attackPower) {
        super(x, y, health, name);
        this.attackPower = attackPower;
        loadImage();
        extractFrames(128, 128, 7);
    }
    private String path = "/images/enemyWalkSpritesheet.png"; 

    private void loadImage() {
       try {
          InputStream is = getClass().getResourceAsStream(this.path); // Cambia il percorso se necessario
          if (is != null) {
              spritesheet = ImageIO.read(is);
          } else {
              System.err.println("Image for DummyEnemy not found");
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
    }



    private void extractFrames(int frameWidth, int frameHeight, int numFrames) {
        walkFrames = new BufferedImage[numFrames];
        for (int i = 0; i < numFrames; i++) {
           walkFrames[i] = spritesheet.getSubimage(
                i * frameWidth, // Posizione X del frame
                0,              // Posizione Y del frame (supponendo che i frame siano su una sola riga)
                frameWidth,     // Larghezza del frame
               frameHeight     // Altezza del frame
           );
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
            currentFrame = (currentFrame + 1) % walkFrames.length; // Passa al frame successivo
        }
    }

    
    public void render(Graphics g) {
        if (spritesheet != null) {
            g.drawImage(walkFrames[currentFrame], x, y, width, height, null); // Disegna l'immagine di Bald
        } else {
            // Fallback al quadrato rosso se l'immagine non è caricata
            g.setColor(Color.RED);
            g.fillRect(x, y, 50, 50);
        }
    }

    public void followPlayer(Bald bald) {
        if (bald.getX() > this.x) {
            x += speedX;
        } else if (bald.getX() < this.x) {
            x -= speedX;
        }
    
        if (bald.getY() > this.y) {
            y += speedY;
        } else if (bald.getY() < this.y) {
            y -= speedY;
        }
    }

    
}
