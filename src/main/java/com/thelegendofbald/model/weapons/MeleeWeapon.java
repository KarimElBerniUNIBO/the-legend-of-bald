package com.thelegendofbald.model.weapons;

import java.awt.Rectangle;
import java.util.List;

import com.thelegendofbald.characters.Entity;
import com.thelegendofbald.combat.Combatant;

public abstract class MeleeWeapon extends Weapon {

    protected int attackRange;

    protected MeleeWeapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage, int attackCooldown, int attackRange) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, attackCooldown);
        this.attackRange = attackRange;
    }

    @Override
    public void performAttack(Combatant attacker, List<? extends Combatant> targets) {
        Entity entityAttacker = (Entity) attacker;
        int attackX = entityAttacker.getX() + entityAttacker.getWidth() / 2;
        int attackY = entityAttacker.getY();
        int width = this.attackRange;
        int height = width;
        Rectangle attackArea;

        if (entityAttacker.isFacingRight()) {
            attackArea = new Rectangle(attackX, attackY, width, height);
        } else {
            attackArea = new Rectangle(attackX - width, attackY, width, height);
        }

        targets.stream()
            .filter(target -> target.isAlive() && attackArea.intersects(target.getBounds()))
            .forEach(target -> target.takeDamage(this.getDamage()));
    }

    public int getAttackRange() {
        return attackRange;
    }

}
