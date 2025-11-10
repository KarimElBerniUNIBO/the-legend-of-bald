package com.thelegendofbald.model.item;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.utils.LoggerUtils;

/**
 * The base class for all items in the game.
 * Provides position, size, name, description, price, sprite, and rendering/collision utilities.
 */
public class GameItem {

    /** The x-coordinate of the item in the game world. */
    private final int x;

    /** The y-coordinate of the item in the game world. */
    private final int y;

    /** The width of the item's bounding box. */
    private final int width;

    /** The height of the item's bounding box. */
    private final int height;

    /** The name of the item. */
    private final String name;

    /** The description of the item. */
    private String description;

    /** The price of the item. */
    private int price;

    /** The sprite image representing the item. */
    private Image sprite;

    /**
     * Constructs a new GameItem with the specified position, size, and name.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width of the item
     * @param height the height of the item
     * @param name the name of the item
     */
    public GameItem(final int x, final int y, final int width, final int height, final String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.description = "";
        this.price = 0;
    }

    /**
     * Renders the item's sprite at its current position.
     *
     * @param g the Graphics object used to render
     */
    public void render(final Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }

    /**
     * Returns the name of the item.
     *
     * @return the item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the item.
     *
     * @return the item's description
     */
    public String getDescription() {
        return description != null ? description : "";
    }

    /**
     * Returns the price of the item.
     *
     * @return the item's price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns the x-coordinate of the item.
     *
     * @return the item's x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the item.
     *
     * @return the item's y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the width of the item.
     *
     * @return the item's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the item.
     *
     * @return the item's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the sprite image of the item.
     *
     * @return defensive copy of the item's sprite image
     */
    public Image getSprite() {
        if (sprite == null) {
            LoggerUtils.warning("Sprite for item " + name + " is not set!");
        }

        BufferedImage copy = new BufferedImage(
            sprite.getWidth(null),
            sprite.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );
        final Graphics g = copy.getGraphics();
        g.drawImage(sprite, 0, 0, null);
        g.dispose();
        return copy;
    }

    /**
     * Returns a Rectangle representing the item's bounding box.
     * Useful for collision detection.
     *
     * @return a Rectangle defining the item's bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Sets the price of the item.
     *
     * @param price the price to set
     */
    public void setPrice(final int price) {
        this.price = price;
    }

    /**
     * Sets the description of the item.
     *
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the sprite image of the item.
     *
     * @param sprite the sprite image to set
     */
    public void setSprite(final Image sprite) {
        if (sprite == null) {
            LoggerUtils.error("Attempted to set a null sprite for item: " + name);
        }

        BufferedImage copy = new BufferedImage(
            sprite.getWidth(null),
            sprite.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );
        final Graphics g = copy.getGraphics();
        g.drawImage(sprite, 0, 0, null);
        g.dispose();
        this.sprite = copy;
    }

    /**
     * Loads a sprite image for the item from the specified path.
     * This method should be called by subclasses in their constructors.
     *
     * @param imagePath the path to the image resource
     */
    protected void loadImage(final String imagePath) {
        try {
            this.sprite = ImageIO.read(GameItem.class.getResourceAsStream(imagePath));
        } catch (final IOException e) {
            LoggerUtils.error("Error loading image: " + imagePath);
        } catch (final IllegalArgumentException e) {
            LoggerUtils.error("Invalid image path: " + imagePath);
        }
    }
}
