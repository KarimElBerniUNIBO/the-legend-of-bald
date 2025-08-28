package com.thelegendofbald.model.item.weapons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.HeavyMeleeWeapon;

/**
 * The {@code Axe} class represents an axe weapon in the game.
 * It extends the {@code HeavyMeleeWeapon} class and provides specific properties for axe attacks.
 */
public class Axe extends HeavyMeleeWeapon implements ShopItem {

    private static final String NAME = "Ascia pesante";
    private static final int DAMAGE = 50;
    private static final int ATTACK_RANGE = 60;
    private static final int PRICE = 40;

    public Axe(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
               final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
        try {
            setSprite(ImageIO.read(getClass().getResource("/images/weapon/axe.png")));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace(); // Così non crasha se l'immagine non viene trovata
        }
    }

    // Metodi dell’interfaccia ShopItem

    @Override
    public String getDisplayName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "Un’ascia pesante e letale. Ideale per infliggere grandi danni.";
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public Image getSprite() {
        return super.getSprite();
    }
}
