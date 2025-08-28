package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.characters.Bald;
import java.awt.image.BufferedImage;

/**
 * Abstract base class for all traps in the game.
 * Handles the triggered state and whether the trap should be removed after activation.
 */
public abstract class Trap extends GameItem {

    /** True if the trap has been triggered. */
    private boolean triggered;

    /** True if the trap should be removed after being triggered. */
    private boolean removeOnTrigger;

    /** The current sprite image of the trap. */
    private BufferedImage currentSprite;

    /**
     * Constructs a new Trap instance.
     *
     * @param x the X-coordinate
     * @param y the Y-coordinate
     * @param width the width of the trap
     * @param height the height of the trap
     * @param name the trap's name
     */
    public Trap(final int x, final int y, final int width, final int height, final String name) {
        super(x, y, width, height, name);
    }

    /**
     * Returns true if the trap has been triggered.
     *
     * @return true if the trap is triggered, false otherwise
     */
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * Sets the triggered state of the trap.
     *
     * @param triggered the new triggered state
     */
    public void setTriggered(final boolean triggered) {
        this.triggered = triggered;
    }

    /**
     * Returns true if the trap should be removed after being triggered.
     *
     * @return true if the trap should be removed after trigger
     */
    public boolean shouldRemoveOnTrigger() {
        return removeOnTrigger;
    }

    /**
     * Sets whether the trap should be removed after triggering.
     *
     * @param remove true if the trap should be removed after trigger, false otherwise
     */
    public void setRemoveOnTrigger(final boolean remove) {
        this.removeOnTrigger = remove;
    }

    /**
     * Returns the current sprite of the trap.
     *
     * @return the current sprite image
     */
    public BufferedImage getCurrentSprite() {
        return currentSprite;
    }

    /**
     * Sets the current sprite of the trap.
     *
     * @param sprite the sprite image to set
     */
    public void setCurrentSprite(final BufferedImage sprite) {
        this.currentSprite = sprite;
    }

    /**
     * Defines the interaction behavior when the trap is triggered by the player.
     * Subclasses must implement their specific effect.
     *
     * @param player the player character affected by the trap
     */
    public abstract void interact(Bald player);
}
