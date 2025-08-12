// File: com/thelegendofbald/buffs/Buff.java

package com.thelegendofbald.buffs;

public class Buff {
    
    private final String name;
    private final long duration; 
    private long startTime;
    private boolean isActive;

    public Buff(String name, long duration) {
        this.name = name;
        this.duration = duration;
        this.isActive = false;
    }

    public void activate() {
        this.startTime = System.currentTimeMillis();
        this.isActive = true;
    }

    public boolean isExpired() {
        if (!isActive) {
            return true;
        }
        return System.currentTimeMillis() - startTime >= duration;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public long getRemainingTime() {
        if (!isActive) {
            return 0;
        }
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, (duration - elapsed) / 1000); // Divisione per 1000
    }
}