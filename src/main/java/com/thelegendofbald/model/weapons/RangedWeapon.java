package com.thelegendofbald.model.weapons;

import java.util.List;

import com.thelegendofbald.characters.Entity;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.model.combat.CombatManager;

public abstract class RangedWeapon extends Weapon {

    private static final int PROJECTILE_SPEED = 10;

    protected RangedWeapon(int x, int y, int preferredSizeX, int preferredSizeY, String name, int damage, int attack_cooldown, CombatManager combatManager) {
        super(x, y, preferredSizeX, preferredSizeY, name, damage, attack_cooldown, combatManager);
    }

    private void createProjectile(int x, int y, int direction, int speed) {
        Projectile projectile = new Projectile(x, y, direction, speed, this.damage);
        this.combatManager.addProjectile(projectile);

    }

    @Override
    public void performAttack(Combatant attacker, List<? extends Combatant> targets) {
        Entity entityAttacker = (Entity) attacker;
        int attackX = entityAttacker.getX() + entityAttacker.getWidth() / 2;
        int attackY = entityAttacker.getY() + entityAttacker.getHeight() / 3;
        int correction = 22;

        if (entityAttacker.isFacingRight()) {
            this.createProjectile(attackX - correction, attackY, 0, PROJECTILE_SPEED);
        } else {
            this.createProjectile(attackX + correction, attackY, 1, PROJECTILE_SPEED);
        }
    }

}
