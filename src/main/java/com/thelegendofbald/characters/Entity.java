package com.thelegendofbald.characters;

import java.awt.Rectangle;
import com.thelegendofbald.life.LifeComponent;

/**
 * Base class for all entities in the game (player, enemies, NPCs).
 * Holds common properties like position, size, name and life component.
 */
public class Entity {

    /** X position in pixels. */
    private int x;

    /** Y position in pixels. */
    private int y;

    /** Render width in pixels. */
    private int width;

    /** Render height in pixels. */
    private int height;

    /** Entity name. */
    private String name;

    /** Life component that handles health. */
    private LifeComponent lifeComponent;

    /** Whether the entity is facing right. */
    private boolean facingRight = true;

    /**
     * Constructs an entity.
     *
     * @param x             x position in pixels
     * @param y             y position in pixels
     * @param width         entity width in pixels
     * @param height        entity height in pixels
     * @param name          entity name
     * @param lifeComponent component handling life/health
     */
    public Entity(final int x, final int y, final int width, final int height,
                  final String name, final LifeComponent lifeComponent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.lifeComponent = new LifeComponent(lifeComponent);
    }

    /**
     * @return the x coordinate in pixels
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x new x coordinate in pixels
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * @return the y coordinate in pixels
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y new y coordinate in pixels
     */
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * @return entity width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the entity width.
     *
     * @param width new width in pixels
     */
    public void setWidth(final int width) {
        this.width = width;
    }

    /**
     * @return entity height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the entity height.
     *
     * @param height new height in pixels
     */
    public void setHeight(final int height) {
        this.height = height;
    }

    /**
     * @return the entity name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the entity name.
     *
     * @param name new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return life component of this entity
     */
    public LifeComponent getLifeComponent() {
        return this.lifeComponent;
    }

    /**
     * Sets the life component.
     *
     * @param lifeComponent new life component
     */
    public void setLifeComponent(final LifeComponent lifeComponent) {
        this.lifeComponent = new LifeComponent(lifeComponent);
    }

    /**
     * @return true if facing right, false otherwise
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * Sets the facing direction.
     *
     * @param facingRight true if facing right, false otherwise
     */
    public void setFacingRight(final boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * @return bounding box of the entity for collisions
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
