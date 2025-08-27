package com.thelegendofbald.model.item.weapons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.weapons.LightMeleeWeapon;
import com.thelegendofbald.utils.LoggerUtils;

public class Sword extends LightMeleeWeapon implements ShopItem {

    private static final String NAME = "Sword";
    private static final String DESCRIPTION = "A simple iron sword, suitable for beginners.";
    private static final int DAMAGE = 30;
    private static final int ATTACK_RANGE = 75;
    private static final int PRICE = 10;

    public Sword(final int x, final int y, final int preferredSizeX, final int preferredSizeY,
                 final CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, NAME, DAMAGE, combatManager, ATTACK_RANGE);
        try {
            this.sprite = ImageIO.read(getClass().getResource("/images/weapon/sword.png"));
        } catch (IOException | IllegalArgumentException e) {
            LoggerUtils.error(NAME + " sprite not found");
        }
    }

    // Metodi dell’interfaccia ShopItem

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
        return sprite;
    }
}
