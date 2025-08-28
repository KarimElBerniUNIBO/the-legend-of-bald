package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.characters.Bald;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * A trap that immobilizes the player for a short duration when triggered.
 * This class extends the abstract {@link Trap} class and implements the
 * specific behavior of an immobilizing trap.
 */
public class ImmobilizingTrap extends Trap {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final String ITEM_NAME = "Immobilizing Trap";

    /**
     * Constructs a new ImmobilizingTrap instance at the specified coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public ImmobilizingTrap(final int x,final int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        this.removeOnTrigger = true; 
        setDescription("Stops you for 2 seconds.");
        loadStaticImage("/images/items/immobilizing_trap.png");
    }

    private void loadStaticImage(final String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            currentSprite = ImageIO.read(is);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact(final Bald bald) {
        if (!isTriggered) {
            isTriggered = true;
            bald.immobilize(2000); 
            System.out.println("You are immobilized!");
    }
}

}
