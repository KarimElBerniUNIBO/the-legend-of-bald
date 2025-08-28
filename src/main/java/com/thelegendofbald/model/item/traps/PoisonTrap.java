package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.effects.debuffs.PoisonBuff;
import com.thelegendofbald.api.common.animation.Animatable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * A trap that poisons the player when triggered.
 * This class extends the abstract {@link Trap} class and implements the
 * {@link Animatable} interface to provide animated behavior.
 */
public class PoisonTrap extends Trap implements Animatable{

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final String ITEM_NAME = "Poison Trap";
    private static final int POISON_DAMAGE_PER_TICK = 5;
    private static final long POISON_TICK_INTERVAL_MS = 1000;
    private static final long POISON_DURATION_MS = 5000;
    private long lastTriggerTime = 0;
    private static final long REACTIVATION_DELAY_MS = 2000; // 2 secondi di cooldown

    private BufferedImage[] idleFrames;
    private int currentFrameIndex = 0;
    private int frameDelay = 8;
    private int frameCounter = 0;

    /**
     * Constructs a new PoisonTrap instance at the specified coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public PoisonTrap(final int x,final int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.removeOnTrigger = false; // NON viene rimossa dopo attivazione
        setDescription("Inflicts " + POISON_DAMAGE_PER_TICK + " damage every " +
            (POISON_TICK_INTERVAL_MS / 1000) + " second for " +
            (POISON_DURATION_MS / 1000) + " seconds.");

        loadAnimationFrames("/images/items/poison_trap.png", 8);
        this.currentSprite = idleFrames[0];
    }

    /**
     * Loads animation frames from a sprite sheet.
     * @param path the path to the sprite sheet
     * @param numFrames the number of frames in the sprite sheet
     */
    private void loadAnimationFrames(final String path,final int numFrames) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            BufferedImage spriteSheet = ImageIO.read(is);
            idleFrames = new BufferedImage[numFrames];
            int frameWidth = spriteSheet.getWidth() / numFrames;
            for (int i = 0; i < numFrames; i++) {
                idleFrames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, spriteSheet.getHeight());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Triggers the poison effect on the player if the trap is not on cooldown.
     * @param bald the Bald {@code Bald} player instance
     */
    @Override
    public void interact(final Bald bald) {
        long now = System.currentTimeMillis();
        if (now - lastTriggerTime >= REACTIVATION_DELAY_MS) {
            bald.applyBuff(new PoisonBuff(POISON_DURATION_MS, POISON_DAMAGE_PER_TICK, POISON_TICK_INTERVAL_MS));
            lastTriggerTime = now;
            System.out.println("You stepped on a poison trap! You are now poisoned.");
        }
    }   

    /**
     * Updates the animation frame of the trap.
     * This method should be called periodically to animate the trap.
     */
    @Override
    public void updateAnimation() {
        if (idleFrames != null && idleFrames.length > 0) {
            frameCounter++;
            if (frameCounter >= frameDelay) {
                frameCounter = 0;
                currentFrameIndex = (currentFrameIndex + 1) % idleFrames.length;
                currentSprite = idleFrames[currentFrameIndex];
            }
        }
    }
}
