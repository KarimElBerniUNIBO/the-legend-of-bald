package com.thelegendofbald.model.item;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The base class for all items within the game.
 * It provides common properties such as position, dimensions, name, and sprite,
 * as well as fundamental behaviors like rendering and collision detection.
 */
public class GameItem {

    // Common game world properties. Using 'protected' for subclasses to access.
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    // Item-specific properties, often used for UI or game logic.
    protected String name;
    protected String description;
    protected int price;

    // The visual representation of the item.
    protected Image sprite;

    /**
     * Constructs a new GameItem with specified coordinates and dimensions.
     * This is the base constructor used by all subclasses.
     *
     * @param x The x-coordinate of the item in the game world.
     * @param y The y-coordinate of the item in the game world.
     * @param width The width of the item's bounding box.
     * @param height The height of the item's bounding box.
     * @param name The name of the item.
     */
    public GameItem(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.description = ""; // Initialize with an empty string
        this.price = 0;        // Initialize with a default price
    }

    /**
     * Renders the item's sprite on the screen at its current position.
     * This method should be called by the game loop to draw the item.
     *
     * @param g The Graphics object used for drawing.
     */
    public void render(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }

    // --- GETTERS FOR ITEM PROPERTIES ---
    
    /**
     * Retrieves the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description of the item.
     *
     * @return The description of the item, or an empty string if none is set.
     */
    public String getDescription() {
        return description != null ? description : "";
    }

    /**
     * Retrieves the price of the item.
     *
     * @return The price of the item.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Retrieves the x-coordinate of the item in the game world.
     *
     * @return The x-coordinate of the item.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the item in the game world.
     *
     * @return The y-coordinate of the item.
     */

    public int getY() {
        return y;
    }

    /**
     * Retrieves the width of the item's bounding box.
     *
     * @return The width of the item.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the item's bounding box.
     *
     * @return The height of the item.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the sprite image of the item.
     *
     * @return The Image object representing the item's sprite.
     */
    public Image getSprite() {
        return sprite;
    }

    /**
     * Returns a Rectangle representing the item's bounding box.
     * Useful for collision detection.
     *
     * @return A Rectangle object defining the item's boundaries.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // --- SETTERS FOR ITEM PROPERTIES ---
    
    /**
     * Sets the price of the item.
     *
     * @param price The new price to set.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Sets the description of the item.
     *
     * @param description The new description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Synchronously loads the item's sprite image from the given path.
     * This method should be called by subclasses in their constructors.
     *
     * @param imagePath The resource path to the image file (e.g., "/images/items/key.png").
     */
    protected void loadImage(String imagePath) {
        try {
            // Use getClass().getResourceAsStream() for robust resource loading.
            this.sprite = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image path: " + imagePath);
            e.printStackTrace();
        }
    }
}