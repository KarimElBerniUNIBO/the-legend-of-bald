package com.thelegendofbald.item;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.buffs.PoisonBuff;
import com.thelegendofbald.api.interactable.Interactable; // Import Interactable
import com.thelegendofbald.api.inventory.Inventory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Represents a generic trap in the game world.
 * When a player interacts with it (e.g., by colliding), it applies a specific effect.
 * In this implementation, it applies a temporary poison effect.
 * The trap remains visible on the map after being triggered, but only activates once.
 */
public class Trap extends GameItem implements Interactable { // Implements Interactable now

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final String ITEM_NAME = "Trap";
    private static final int POISON_DAMAGE_PER_TICK = 5;
    private static final long POISON_TICK_INTERVAL_MS = 1000; // Damage every 1 second
    private static final long POISON_DURATION_MS = 5000; // Poison lasts 5 seconds (5 ticks)

    private boolean isTriggered; // New flag to ensure it only activates once

    /**
     * Constructs a new Trap.
     *
     * @param x The x-coordinate of the trap.
     * @param y The y-coordinate of the trap.
     */
    public Trap(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        setDescription("Inflicts " + POISON_DAMAGE_PER_TICK + " damage every " + (POISON_TICK_INTERVAL_MS / 1000) + " second for " + (POISON_DURATION_MS / 1000) + " seconds.");
        loadImage("/images/items/spike.png"); // Image for the trap
        this.isTriggered = false; // Initially not triggered
    }

    /**
     * Checks if the trap has already been triggered.
     * @return true if triggered, false otherwise.
     */
    public boolean isTriggered() {
        return isTriggered;
    }

    /**
     * Handles the player's interaction with the trap.
     * If the trap has not been triggered yet, it applies a {@link PoisonBuff}
     * to the player and marks itself as triggered. The trap remains on the map
     * but will not activate again.
     *
     * @param bald The {@link Bald} player instance that interacted with the trap.
     * @param inventory The player's inventory (not directly used by trap's effect).
     */
    @Override
    public void interact(final Bald bald, final Inventory inventory) {
        if (!isTriggered) {
            bald.applyBuff(new PoisonBuff(POISON_DURATION_MS, POISON_DAMAGE_PER_TICK, POISON_TICK_INTERVAL_MS));
            this.isTriggered = true; // Mark as triggered so it won't activate again
            System.out.printf("You stepped on a %s! You are now poisoned.%n", ITEM_NAME);
            // Optional: Change sprite to indicate it's been sprung
            // loadImage("/images/items/sprung_trap.png");
        } else {
            System.out.println("This trap has already been sprung.");
        }
    }
}