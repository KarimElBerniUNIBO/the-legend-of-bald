package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.api.inventory.Inventory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

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
            e.printStackTrace();
        }
    }

    @Override
    public void interact(Bald bald, Inventory inventory) {
        if (!isTriggered) {
            isTriggered = true;
            bald.immobilize(2000); 
            System.out.println("You are immobilized!");
    }
}

}
