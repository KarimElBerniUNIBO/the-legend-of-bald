package com.thelegendofbald.model.item.traps;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.api.interactable.Interactable;
import com.thelegendofbald.api.inventory.Inventory;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Trap extends GameItem implements Interactable {

    protected boolean isTriggered = false;
    protected boolean removeOnTrigger = false;

    protected BufferedImage currentSprite;

    public Trap(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
    }

    public boolean isTriggered() {
        return isTriggered;
    }

    public boolean shouldRemoveOnTrigger() {
        return removeOnTrigger;
    }

    @Override
    public abstract void interact(Bald bald, Inventory inventory);

    public void updateAnimation() {
        // di default non fa nulla, le sottoclassi animate lo sovrascrivono
    }

    @Override
    public void render(Graphics g) {
        if (currentSprite != null) {
            g.drawImage(currentSprite, x, y, width, height, null);
        }
    }

    public boolean shouldTrigger(Bald bald) {
        // Default: trigger se il centro di Bald è dentro la trappola
        int baldCenterX = bald.getX() + bald.getWidth() / 2;
        int baldCenterY = bald.getY() + bald.getHeight() / 2;
        return !isTriggered() && getBounds().contains(baldCenterX, baldCenterY);
    }
}
