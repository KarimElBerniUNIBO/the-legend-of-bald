package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.characters.Bald;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import com.thelegendofbald.utils.LoggerUtils;

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
     * Renders the trap on the screen.
     * If no sprite is set, it renders a magenta rectangle and logs a warning.
     * @param g the Graphics context to draw on
     */
    @Override
    public void render(final Graphics g) {
    if (currentSprite != null) {
            g.drawImage(currentSprite, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(java.awt.Color.MAGENTA);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            LoggerUtils.warning(getDescription() + " has no sprite set!");
        }
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
        if (currentSprite == null) {
            LoggerUtils.error("Attempted to get a null sprite for item: " + getName());
            return null;
        }

        final BufferedImage copy = new BufferedImage(
            currentSprite.getWidth(null),
            currentSprite.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );
        final Graphics g = copy.getGraphics();
        g.drawImage(currentSprite, 0, 0, null);
        g.dispose();
        return copy;
    }

    /**
     * Sets the current sprite of the trap.
     *
     * @param sprite the sprite image to set
     */
    public void setCurrentSprite(final BufferedImage sprite) {
        if (sprite == null) {
            LoggerUtils.error("Attempted to set a null sprite for item: " + getName());
        }

        final BufferedImage copy = new BufferedImage(
            sprite.getWidth(null),
            sprite.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );
        final Graphics g = copy.getGraphics();
        g.drawImage(sprite, 0, 0, null);
        g.dispose();
        this.currentSprite = copy;
    }

    /**
     * Defines the interaction behavior when the trap is triggered by the player.
     * Subclasses must implement their specific effect.
     *
     * @param player the player character affected by the trap
     */
    public abstract void interact(Bald player);
}
