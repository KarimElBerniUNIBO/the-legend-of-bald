package com.thelegendofbald.buffs;

import com.thelegendofbald.characters.Bald;

public class PoisonBuff extends Buff {
    private final int damagePerTick;
    private final long tickIntervalMs;
    private long lastTickTime;

    public PoisonBuff(long durationMs, int damagePerTick, long tickIntervalMs) {
        super("Poison", durationMs);
        this.damagePerTick = damagePerTick;
        this.tickIntervalMs = tickIntervalMs;
        this.lastTickTime = System.currentTimeMillis();
    }

    @Override
    public void apply(Bald player) {
        System.out.println("Player is poisoned!");
    }

    @Override
    public void remove(Bald player) {
        System.out.println("Poison effect worn off!");
    }

    @Override
    public void onTick(Bald player) {
        long now = System.currentTimeMillis();
        if (now - lastTickTime >= tickIntervalMs) {
            player.takeDamage(damagePerTick); // stampa e gestisce invincibilità già in Bald
            lastTickTime = now;
        }
    }

    @Override
    public void update() {
        // Se ti serve logica extra oltre al tick, lasciala qui.
        super.update();
    }
}
