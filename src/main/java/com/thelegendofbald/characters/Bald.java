package com.thelegendofbald.characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;

public class Bald extends Entity implements Combatant{
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

    public Bald(int x, int y,int maxHealth, String name, int attackPower ) {
        super(x, y, name , new LifeComponent(maxHealth));
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
    
    public void move() {
        if (speedX > 0) {
            facingRight = false; // Bald si muove verso destra
        } else if (speedX < 0) {
            facingRight = true; // Bald si muove verso sinistra
        }
        this.x += speedX;
        this.y += speedY;
        
    }

    @Override
    public void takeDamage(int damage) {
        this.lifeComponent.damageTaken(damage);
        System.out.println(lifeComponent.getCurrentHealth());
        
    }

    public boolean isAlive() {
        return !this.lifeComponent.isDead();
    }

    public boolean canShoot() {
        return true;
    }

    public void shootProjectile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shootProjectile'");
    }

    public boolean isFacingRight(){
        return this.facingRight;
    }

}
