package com.thelegendofbald.model.item.traps;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.utils.LoggerUtils;

public class ImmobilizingTrap extends Trap {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final String ITEM_NAME = "Immobilizing Trap";

    private double savedSpeedX;
    private double savedSpeedY;

    public ImmobilizingTrap(int x, int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.removeOnTrigger = true; // si rimuove dopo attivazione
        setDescription("Stops you for 2 seconds.");
        loadStaticImage("/images/items/immobilizing_trap.png");
    }

    private void loadStaticImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            currentSprite = ImageIO.read(is);
        } catch (IOException | NullPointerException e) {
            LoggerUtils.error("Failed to load image: " + path);
        }
    }

    @Override
    public void interact(Bald bald) {
        if (!isTriggered) {
            isTriggered = true;
            bald.immobilize(2000);
            LoggerUtils.info("You are immobilized!");
    }
}

}
