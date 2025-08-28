package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.characters.Bald;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * A trap that immobilizes the player for a short duration when triggered.
 * Once activated, the player cannot move for {@value #IMMOBILIZE_DURATION_MS} milliseconds.
 */
public final class ImmobilizingTrap extends Trap {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final String NAME = "Immobilizing Trap";
    private static final int IMMOBILIZE_DURATION_MS = 2000;

    /**
     * Constructs a new ImmobilizingTrap at the specified coordinates.
     *
     * @param x the X-coordinate
     * @param y the Y-coordinate
     */
    public ImmobilizingTrap(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, NAME);
        setRemoveOnTrigger(true);
        setDescription("Stops you for 2 seconds.");
        loadStaticImage("/images/items/immobilizing_trap.png");
    }

    /**
     * Loads a static image for the trap.
     *
     * @param path the path to the image resource
     */
    private void loadStaticImage(final String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            setCurrentSprite(ImageIO.read(is));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Triggers the trap and immobilizes the player if not already triggered.
     *
     * @param bald the player affected by the trap
     */
    @Override
    public void interact(final Bald bald) {
        if (!isTriggered()) {
            setTriggered(true);
            bald.immobilize(IMMOBILIZE_DURATION_MS);
            System.out.println("You are immobilized!");
        }
    }
}
