package com.thelegendofbald.item;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class GameItem {

    // Coordinate e dimensioni nel mondo di gioco (se servono)
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    // Dati comuni
    protected String name;
    protected String description;
    protected int price;

    // Sprite/immagine
    protected Image sprite;

    // Costruttore base
    public GameItem(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    // Render nel mondo di gioco (opzionale)
    public void render(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }

    // --- METODI UTILI PER SHOP UI ---

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public int getPrice() {
        return price;
    }

    public Image getSprite() {
        return sprite;
    }

    // --- SETTER ---

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.sprite = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));
    }

    

}
