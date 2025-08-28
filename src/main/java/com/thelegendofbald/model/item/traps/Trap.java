package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.api.interactable.Interactable;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * An abstract base class for all traps in the game.
 * This class extends {@link GameItem} and implements the
 * {@link Interactable} interface, providing a common structure
 * for traps that can be triggered by the player.
 */
public abstract class Trap extends GameItem implements Interactable {

    protected boolean isTriggered = false;
    protected boolean removeOnTrigger = false;

    protected BufferedImage currentSprite;

    /**
     * Constructs a new Trap instance.
     *
     * @param x The x-coordinate of the trap.
     * @param y The y-coordinate of the trap.
     * @param width The width of the trap's bounding box.
     * @param height The height of the trap's bounding box.
     * @param name The name of the trap.
     */
    public Trap(final int x,final int y,final int width,final int height,final String name) {
        super(x, y, width, height, name);
    }

    /**
     * Checks if the trap has been triggered.
     * @return true if the trap is triggered, false otherwise.
     */
    public boolean isTriggered() {
        return isTriggered;
    }

    /**
     * Determines if the trap should be removed from the game after being triggered.
     * @return true if the trap should be removed, false otherwise.
     */
    public boolean shouldRemoveOnTrigger() {
        return removeOnTrigger;
    }

    /**
     * Defines the interaction behavior when the trap is triggered by Bald.
     */
    @Override
    public abstract void interact(Bald bald);

    /**
     * Updates the trap's animation state.
     */
    public void updateAnimation() {
        // Default: no animation update
    }

    /*
     * Renders the trap on the screen.
     * @param g the Graphics context to draw on
     */
    @Override
    public void render(final Graphics g) {
        if (currentSprite != null) {
            g.drawImage(currentSprite, x, y, width, height, null);
        }
    }

    /**
     * Determines if the trap should be triggered based on Bald's position.
     * @param bald the Bald {@code Bald} player
     * @return true if the trap should be triggered, false otherwise
     */
    public boolean shouldTrigger(final Bald bald) {
        // Default: trigger se il centro di Bald è dentro la trappola
        int baldCenterX = bald.getX() + bald.getWidth() / 2;
        int baldCenterY = bald.getY() + bald.getHeight() / 2;
        return !isTriggered() && getBounds().contains(baldCenterX, baldCenterY);
    }
}
