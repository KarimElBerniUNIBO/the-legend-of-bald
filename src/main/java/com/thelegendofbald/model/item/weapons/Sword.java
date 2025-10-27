package com.thelegendofbald.model.item.weapons;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.weapons.LightMeleeWeapon;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * The Sword class represents a sword in the game.
 * It is a light melee weapon and can be purchased from a shop.
 */
public final class Sword extends LightMeleeWeapon implements ShopItem {

    private static final String NAME = "Sword";
    private static final String DESCRIPTION = "A simple iron sword, suitable for beginners.";
    private static final int DAMAGE = 30;
    private static final int ATTACK_RANGE = 75;
    private static final int PRICE = 10;

    /**
     * Constructs a new Sword object.
     *
     * @param x              the initial x-coordinate of the sword.
     * @param y              the initial y-coordinate of the sword.
     * @param preferredSizeX the preferred width for the sword's sprite.
     * @param preferredSizeY the preferred height for the sword's sprite.
     * @param combatManager  the combat manager that will handle the sword's attacks.
     */
    public Sword(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
                 final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
        try {
            setSprite(ImageIO.read(getClass().getResource("/images/weapon/sword.png")));
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
