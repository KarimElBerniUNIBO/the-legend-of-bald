package com.thelegendofbald.model.item.potions;

import com.thelegendofbald.api.item.UsableItem;
import com.thelegendofbald.characters.Bald;

/**
 * A potion that restores a certain amount of the player's health.
 * This class extends the abstract {@link Potion} and implements the
 * {@link UsableItem} interface, allowing it to be used from the inventory.
 */
public class HealthPotion extends Potion implements UsableItem {

    private final int healAmount;

    /**
     * Constructs a new HealthPotion instance.
     *
     * @param x The x-coordinate of the potion.
     * @param y The y-coordinate of the potion.
     */
    public HealthPotion(int x, int y) {
        super(x, y, 32, 32, "Healing Potion");
        this.healAmount = 20;
        setDescription("Restores " + healAmount + " health points.");
        setPrice(30);
        loadImage("/images/potions/health_potion.png");
    }

    /**
     * Applies the healing effect to the player.
     *
     * @param player The player instance to be healed.
     */
    @Override
    public void applyEffect(Bald player) {
        player.getLifeComponent().heal(healAmount);
        System.out.printf("You used a %s and recovered %d health points.%n", getName(), healAmount);
        System.out.println("Bald now has: " + player.getLifeComponent().getCurrentHealth());
    }
}