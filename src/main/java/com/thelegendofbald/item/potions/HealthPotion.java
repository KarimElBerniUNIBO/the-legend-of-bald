package com.thelegendofbald.item.potions;

import com.thelegendofbald.characters.Bald;

public class HealthPotion extends Potion {

    private int healAmount;

    public HealthPotion(int x, int y) {
        super(x, y, 32, 32, "Pozione Curativa");
        this.healAmount = 20;
        setDescription("Recupera " + healAmount + " punti vita");
        setPrice(30);
        setImagePath("/sprites/items/health_potion.png");
    }

    @Override
    public void applyEffect(Bald player) {
        player.getLifeComponent().heal(healAmount);
    }
}
