package com.thelegendofbald.model.item.weapons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.weapons.HeavyMeleeWeapon;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * The Axe class represents an axe weapon in the game.
 * It is a heavy melee weapon and can be purchased from a shop.
 */
public final class Axe extends HeavyMeleeWeapon implements ShopItem {

    private static final String NAME = "Axe";
    private static final int DAMAGE = 50;
    private static final int ATTACK_RANGE = 60;
    private static final int PRICE = 40;

    /**
     * Constructs a new FireBall object.
     *
     * @param x              the initial x-coordinate of the fireball.
     * @param y              the initial y-coordinate of the fireball.
     * @param preferredSizeX the preferred width for the fireball's sprite.
     * @param preferredSizeY the preferred height for the fireball's sprite.
     * @param combatManager  the combat manager that will handle the fireball's attacks.
     */
    public Axe(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
               final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
        try {
            setSprite(ImageIO.read(getClass().getResource("/images/weapon/axe.png")));
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
