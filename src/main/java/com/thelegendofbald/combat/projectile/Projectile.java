package com.thelegendofbald.combat.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.thelegendofbald.characters.Entity;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;


public class Projectile extends Entity implements Combatant {
    private int x, y;
    private int speed;
    private int direction; // 0 = destra, 1 = sinistra, ecc.
    private boolean active = true;
    private final int damage;

    public Projectile(int x, int y, int direction, int speed, int damage) {
        super(x, y, 6, 6, "bullet", null); // Pass null or a valid LifeComponent instance if available
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
    }

    public void move(TileMap tileMap) {
        int nextX = x;
        int nextY = y;
        if (direction == 0) nextX += speed;
        if (direction == 1) nextX -= speed;
        // aggiungi altre direzioni se vuoi (sopra, sotto)

        // Controllo collisione su tutti gli angoli del proiettile
        int[][] points = {
            {nextX, nextY},
            {nextX + 5, nextY},
            {nextX, nextY + 5},
            {nextX + 5, nextY + 5}
        };
        for (int[] p : points) {
            int tileX = p[0] / tileMap.getTileSize();
            int tileY = p[1] / tileMap.getTileSize();
            Tile tile = tileMap.getTileAt(tileX, tileY);
            if (tile != null && tile.isSolid()) {
                this.active = false;
                return;
            }
        }

        this.x = nextX;
        this.y = nextY;
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
