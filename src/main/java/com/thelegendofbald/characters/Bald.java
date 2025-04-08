package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Bald extends Entity {
    private int attackPower; // Potenza d'attacco
    private BufferedImage image;
    private String path = "/images/bald.png"; // Percorso dell'immagine
    private int speedX = 0; // Velocità lungo l'asse X
    private int speedY = 0; // Velocità lungo l'asse Y

    public Bald(int x, int y, int health, String name, int attackPower ) {
        super(x, y, health, name);
        this.attackPower = attackPower;
        loadImage();
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



    private void loadImage() {
       try {
          InputStream is = getClass().getResourceAsStream(this.path); // Cambia il percorso se necessario
          if (is != null) {
              image = ImageIO.read(is);
          } else {
              System.err.println("Image for Bald not found");
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
    }


    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 50, 50, null); // Disegna l'immagine di Bald
        } else {
            // Fallback al quadrato rosso se l'immagine non è caricata
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
        this.x += speedX;
        this.y += speedY;
    }

    
}
