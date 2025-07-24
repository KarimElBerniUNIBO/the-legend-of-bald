package com.thelegendofbald.combat.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.thelegendofbald.characters.Entity;
import com.thelegendofbald.combat.Combatant;


public class Projectile extends Entity implements Combatant {
    public int x, y;
    public int speed;
    public int direction; // 0 = destra, 1 = sinistra, ecc.
    public boolean active = true;
    public final int damage;

    public Projectile(int x, int y, int direction, int speed, int damage) {
        super(x, y, 6, 6, "bullet", null); // Pass null or a valid LifeComponent instance if available
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
    }

    public void move() {
        if (direction == 0) x += speed;
        if (direction == 1) x -= speed;
        // aggiungi altre direzioni se vuoi (sopra, sotto)
    }

    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, 6, 6);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 6, 6);
    }

    public int getAttackPower() {
        return this.damage;
    }
    public void takeDamage(int damage) {
        
    }

    public boolean isAlive(){
        return this.active;
    }



    




}
