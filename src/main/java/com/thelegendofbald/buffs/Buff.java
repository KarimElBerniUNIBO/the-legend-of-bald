package com.thelegendofbald.buffs;

import com.thelegendofbald.characters.Bald;

public abstract class Buff {
    private final String name;
    private final long durationMs;
    private long startTime;
    private boolean isActive;

    public Buff(String name, long durationMs) {
        this.name = name;
        this.durationMs = durationMs;
        this.isActive = false;
    }

    public void activate() {
        this.startTime = System.currentTimeMillis();
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isExpired() {
        if (!isActive) return true;
        return System.currentTimeMillis() - startTime >= durationMs;
    }

    public String getName() { return name; }

    public boolean isActive() { return isActive; }

    public long getRemainingTime() {
        if (!isActive) return 0;
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, (durationMs - elapsed) / 1000);
    }

    /** Chiamato una volta al momento dell’applicazione. */
    public abstract void apply(Bald player);

    /** Chiamato una volta quando il buff viene rimosso o scade. */
    public abstract void remove(Bald player);

    /** Aggiornamento “generico” del buff (se serve stato interno). */
    public void update() { }

    /* ----------------------- NUOVI HOOK OCP ----------------------- */

    /** Permette a un buff di modificare la potenza d’attacco. Di default no-op. */
    public int modifyAttackPower(Bald player, int basePower) {
        return basePower;
    }

    /** Effetti periodici (DoT/HoT, ecc.). Di default no-op. */
    public void onTick(Bald player) { }
}
