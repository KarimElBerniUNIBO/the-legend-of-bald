package com.thelegendofbald.item.potions;

import com.thelegendofbald.characters.Bald;

public class StrengthPotion extends Potion {

    private int bonusDamage;
    private int duration; // in secondi

    public StrengthPotion(int x, int y) {
        super(x, y, 32, 32, "Pozione di Forza");
        this.bonusDamage = 5;
        this.duration = 10;
        setDescription("Aumenta il danno di " + bonusDamage + " per " + duration + " secondi");
        setPrice(50);
        setImagePath("/sprites/items/strength_potion.png");
    }

    @Override
    public void applyEffect(Bald player) {
        int oldAttack = player.getAttackPower();
        player.setAttackPower(oldAttack + bonusDamage);

        // Rimuove il bonus dopo X secondi
        new Thread(() -> {
            try {
                Thread.sleep(duration * 1000);
                player.setAttackPower(oldAttack);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}