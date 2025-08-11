package com.thelegendofbald.item.weapons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.item.ShopItem;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.weapons.RangedWeapon;

/**
 * The {@code Magic} class represents a magic weapon in the game.
 * It extends the {@code RangedWeapon} class and provides specific properties for magic attacks.
 */
public class FireBall extends RangedWeapon implements ShopItem {

    private static final String NAME = "Palla di fuoco";
    private static final int DAMAGE = 10;
    private static final int ATTACK_COOLDOWN = 300; // milliseconds
    private static final int PRICE = 25;

    public FireBall(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
                 final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, ATTACK_COOLDOWN, combatManager);
        try {
            this.sprite = ImageIO.read(getClass().getResource("/images/weapon/fireball.png"));
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
        return "Una serie di sfere infuocate che possono essere lanciate a distanza.";
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public Image getSprite() {
        return this.sprite;
    }
}
