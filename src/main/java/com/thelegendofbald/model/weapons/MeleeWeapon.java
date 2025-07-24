package com.thelegendofbald.model.weapons;

import java.awt.geom.Arc2D;
import java.util.List;

import com.thelegendofbald.characters.Entity;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.model.combat.CombatManager;

public abstract class MeleeWeapon extends Weapon {

    protected Arc2D attackArea = new Arc2D.Double(0, 0, 0, 0, 0, 0, Arc2D.PIE);
    protected int attackRange;

    protected MeleeWeapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage, int attackCooldown, CombatManager combatManager, int attackRange) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, attackCooldown, combatManager);
        this.attackRange = attackRange;
    }

    @Override
    public void performAttack(Combatant attacker, List<? extends Combatant> targets) {
        Entity entityAttacker = (Entity) attacker;
        int attackX = entityAttacker.getX() + entityAttacker.getWidth() / 2;
        int attackY = entityAttacker.getY();
        int width = this.attackRange;
        int height = entityAttacker.getHeight();
        int correction = 22;

        if (entityAttacker.isFacingRight()) {
            attackArea = new Arc2D.Double(attackX - correction, attackY, width, height, 270, 180, Arc2D.PIE);
        } else {
            attackArea = new Arc2D.Double(attackX - width + correction, attackY, width, height, 90, 180, Arc2D.PIE);
        }

        targets.stream()
            .filter(target -> target.isAlive() && attackArea.intersects(target.getBounds()))
            .forEach(target -> target.takeDamage(this.getDamage()));
    }

    public int getAttackRange() {
        return attackRange;
    }

    public Arc2D getAttackArea() {
        return (Arc2D) attackArea.clone();
    }

}
