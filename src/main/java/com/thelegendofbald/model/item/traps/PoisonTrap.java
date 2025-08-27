package com.thelegendofbald.model.item.traps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.thelegendofbald.api.common.animation.Animatable;
import com.thelegendofbald.buffs.PoisonBuff;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.utils.LoggerUtils;

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

    public PoisonTrap(int x, int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.removeOnTrigger = false; // NON viene rimossa dopo attivazione
        setDescription("Inflicts " + POISON_DAMAGE_PER_TICK + " damage every " +
            (POISON_TICK_INTERVAL_MS / 1000) + " second for " +
            (POISON_DURATION_MS / 1000) + " seconds.");

        loadAnimationFrames("/images/items/poison_trap.png", 8);
        this.currentSprite = idleFrames[0];
    }

    private void loadAnimationFrames(String path, int numFrames) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            BufferedImage spriteSheet = ImageIO.read(is);
            idleFrames = new BufferedImage[numFrames];
            int frameWidth = spriteSheet.getWidth() / numFrames;
            for (int i = 0; i < numFrames; i++) {
                idleFrames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, spriteSheet.getHeight());
            }
        } catch (IOException | NullPointerException e) {
            LoggerUtils.error("Failed to load image: " + path);
        }
    }

    @Override
    public void interact(Bald bald) {
        long now = System.currentTimeMillis();
        if (now - lastTriggerTime >= REACTIVATION_DELAY_MS) {
            bald.applyBuff(new PoisonBuff(POISON_DURATION_MS, POISON_DAMAGE_PER_TICK, POISON_TICK_INTERVAL_MS));
            lastTriggerTime = now;
            LoggerUtils.info("You stepped on a poison trap! You are now poisoned.");
        }
    }   

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
