package com.thelegendofbald.model.item.potions;

import com.thelegendofbald.api.item.UsableItem;
import com.thelegendofbald.characters.Bald;

/**
 * A potion that restores a certain amount of the player's health.
 * This class extends the abstract {@link Potion} and implements the
 * {@link UsableItem} interface, allowing it to be used from the inventory.
 */
public class HealthPotion extends Potion implements UsableItem {

    private static final int DEFAULT_HEAL_AMOUNT = 20;
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private static final int DEFAULT_PRICE = 30;

    private final int healAmount;

    /**
     * Constructs a new HealthPotion instance.
     *
     * @param x The x-coordinate of the potion.
     * @param y The y-coordinate of the potion.
     */
    public HealthPotion(final int x, final int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "Healing Potion");
        this.healAmount = DEFAULT_HEAL_AMOUNT;
        setDescription("Restores " + healAmount + " health points.");
        setPrice(DEFAULT_PRICE);
        loadImage("/images/potions/health_potion.png");
    }

    /**
     * Applies the healing effect to the player.
     *
     * @param player The player instance to be healed.
     */
    @Override
    public void applyEffect(final Bald player) {
        player.getLifeComponent().heal(healAmount);
        System.out.printf("You used a %s and recovered %d health points.%n", getName(), healAmount);
        System.out.println("Bald now has: " + player.getLifeComponent().getCurrentHealth());
    }
}
