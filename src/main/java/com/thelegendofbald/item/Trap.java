package com.thelegendofbald.item;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.buffs.PoisonBuff;
import com.thelegendofbald.api.interactable.Interactable;
import com.thelegendofbald.api.inventory.Inventory;

import java.awt.Graphics; // Import Graphics
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Represents a generic trap in the game world.
 * When a player interacts with it (e.g., by colliding), it applies a specific effect.
 * This class now includes animation for its idle and triggered states.
 * The trap remains visible on the map after being triggered, but only activates once.
 */
public class Trap extends GameItem implements Interactable {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final String ITEM_NAME = "Trap";
    private static final int POISON_DAMAGE_PER_TICK = 5;
    private static final long POISON_TICK_INTERVAL_MS = 1000;
    private static final long POISON_DURATION_MS = 5000;

    private boolean isTriggered; // Flag per l'attivazione
    private BufferedImage[] idleFrames; // Array per i frame dell'animazione inattiva
    private BufferedImage[] triggeredFrames; // Array per i frame dell'animazione attivata (opzionale)
    private BufferedImage currentSprite; // Il frame corrente da disegnare
    private int currentFrameIndex = 0; // Indice del frame corrente
    private int frameDelay = 8; // Velocità dell'animazione (più alto = più lento)
    private int frameCounter = 0; // Contatore per il ritardo dei frame

    /**
     * Constructs a new Trap.
     *
     * @param x The x-coordinate of the trap.
     * @param y The y-coordinate of the trap.
     */
    public Trap(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, ITEM_NAME);
        setDescription("Inflicts " + POISON_DAMAGE_PER_TICK + " damage every " + (POISON_TICK_INTERVAL_MS / 1000) + " second for " + (POISON_DURATION_MS / 1000) + " seconds.");
        
        // Carica la sprite sheet e ne estrae i frame
        loadAnimationFrames("/images/items/trap_spritesheet.png", 8); // Assumi una spritesheet con 6 frame
        this.isTriggered = false;
        this.currentSprite = idleFrames[0]; // Inizia con il primo frame
    }

    /**
     * Loads the animation frames from a sprite sheet.
     *
     * @param spriteSheetPath The path to the sprite sheet image.
     * @param numFrames The number of individual frames in the sprite sheet.
     */
    private void loadAnimationFrames(String spriteSheetPath, int numFrames) {
        try (InputStream is = getClass().getResourceAsStream(spriteSheetPath)) {
            if (is == null) {
                System.err.println("Sprite sheet not found: " + spriteSheetPath);
                // In questo caso, puoi assegnare un'immagine di fallback o un rettangolo colorato
                idleFrames = new BufferedImage[]{ new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB) };
                Graphics g = idleFrames[0].getGraphics();
                g.setColor(java.awt.Color.MAGENTA);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.dispose();
                return;
            }
            BufferedImage spriteSheet = ImageIO.read(is);
            idleFrames = new BufferedImage[numFrames];
            int frameWidth = spriteSheet.getWidth() / numFrames;
            // Assumi che tutti i frame siano sulla stessa riga e abbiano la stessa altezza della spritesheet
            for (int i = 0; i < numFrames; i++) {
                idleFrames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, spriteSheet.getHeight());
            }
            // Puoi caricare anche i triggeredFrames da un'altra spritesheet o da qui
            triggeredFrames = idleFrames; // Per ora, usiamo gli stessi frame per semplicità
                                          // In futuro potresti caricare una spritesheet diversa qui
        } catch (IOException e) {
            System.err.println("Error loading sprite sheet: " + spriteSheetPath + " - " + e.getMessage());
            e.printStackTrace();
            // Gestione del fallback come sopra
            idleFrames = new BufferedImage[]{ new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB) };
            Graphics g = idleFrames[0].getGraphics();
            g.setColor(java.awt.Color.MAGENTA);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.dispose();
        }
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
            // Inizia un'animazione di attivazione se diversa dall'idle
            // currentFrameIndex = 0; // Reset dell'animazione
            // frameCounter = 0;
            // Potresti voler cambiare frameDelay qui per una velocità diversa
            System.out.printf("You stepped on a %s! You are now poisoned.%n", ITEM_NAME);
        } else {
            System.out.println("This trap has already been sprung.");
        }
    }

    /**
     * Updates the animation state of the trap.
     * This method should be called every game tick.
     */
    public void updateAnimation() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            if (isTriggered && triggeredFrames != null && triggeredFrames.length > 0) {
                currentFrameIndex = (currentFrameIndex + 1) % triggeredFrames.length;
                currentSprite = triggeredFrames[currentFrameIndex];
            } else if (idleFrames != null && idleFrames.length > 0) {
                currentFrameIndex = (currentFrameIndex + 1) % idleFrames.length;
                currentSprite = idleFrames[currentFrameIndex];
            }
        }
    }

    /**
     * Renders the current animation frame of the trap on the screen.
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void render(Graphics g) {
        if (currentSprite != null) {
            g.drawImage(currentSprite, x, y, WIDTH, HEIGHT, null);
        } else {
            // Fallback: Disegna un quadrato magenta se l'immagine non è caricata
            g.setColor(java.awt.Color.MAGENTA);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }
}