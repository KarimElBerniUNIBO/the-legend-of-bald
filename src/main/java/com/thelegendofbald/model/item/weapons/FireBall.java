package com.thelegendofbald.model.item.weapons;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.weapons.RangedWeapon;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * The FireBall class represents a fireball weapon in the game.
 * It is a ranged weapon and can be purchased from a shop.
 */
public final class FireBall extends RangedWeapon implements ShopItem {

    private static final String NAME = "Fireball";
    private static final String DESCRIPTION = "A fiery projectile";
    private static final int DAMAGE = 25;
    private static final int ATTACK_COOLDOWN = 600; // milliseconds
    private static final int PRICE = 25;

    /**
     * Constructs a new FireBall object.
     *
     * @param x              the initial x-coordinate of the fireball.
     * @param y              the initial y-coordinate of the fireball.
     * @param preferredSizeX the preferred width for the fireball's sprite.
     * @param preferredSizeY the preferred height for the fireball's sprite.
     * @param combatManager  the combat manager that will handle the fireball's attacks.
     */
    public FireBall(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
                 final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, ATTACK_COOLDOWN, combatManager);
        try {
            setSprite(ImageIO.read(getClass().getResource("/images/weapon/fireball.png")));
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

}
