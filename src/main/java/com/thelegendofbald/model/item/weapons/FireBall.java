package com.thelegendofbald.model.item.weapons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.weapons.RangedWeapon;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * The {@code Magic} class represents a magic weapon in the game.
 * It extends the {@code RangedWeapon} class and provides specific properties for magic attacks.
 */
public class FireBall extends RangedWeapon implements ShopItem {

    private static final String NAME = "Fireball";
    private static final String DESCRIPTION = "A fiery projectile";
    private static final int DAMAGE = 25;
    private static final int ATTACK_COOLDOWN = 600; // milliseconds
    private static final int PRICE = 25;

    public FireBall(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
                 final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, ATTACK_COOLDOWN, combatManager);
        try {
            this.sprite = ImageIO.read(getClass().getResource("/images/weapon/fireball.png"));
        } catch (IOException | IllegalArgumentException e) {
            LoggerUtils.error(NAME + " sprite not found");
        }
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
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
