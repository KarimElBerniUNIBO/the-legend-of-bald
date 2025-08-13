package com.thelegendofbald.buffs;

import java.util.ArrayList;
import java.util.List;

import com.thelegendofbald.characters.Bald;

public class BuffManager {
    private final Bald owner;
    private final List<Buff> activeBuffs = new ArrayList<>();

    public BuffManager(Bald owner) {
        this.owner = owner;
    }

    /** Aggiunge o sostituisce un buff con lo stesso nome. */
    public void applyBuff(Buff buff) {
        // rimuovi eventuale buff omonimo
        activeBuffs.removeIf(b -> b.getName().equals(buff.getName()));

        buff.activate();
        activeBuffs.add(buff);       // lo rendiamo “visibile” subito
        buff.apply(owner);           // hook di applicazione
        System.out.println("Buff di " + buff.getName() + " attivato!");
    }

    /** Tick di gioco: aggiorna e rimuove quelli scaduti. */
    public void update() {
        List<Buff> expired = new ArrayList<>();

        for (Buff buff : activeBuffs) {
            // Effetti periodici (DoT/HoT) e aggiornamenti di stato
            buff.onTick(owner);
            buff.update();

            if (buff.isExpired()) {
                buff.remove(owner);
                expired.add(buff);
            }
        }

        if (!expired.isEmpty()) {
            activeBuffs.removeAll(expired);
        }
    }

    /** Calcola l’attacco effettivo piegando i modificatori dei buff. */
    public int modifyAttackPower(int basePower) {
        int power = basePower;
        for (Buff buff : activeBuffs) {
            power = buff.modifyAttackPower(owner, power);
        }
        return power;
    }

    public List<Buff> getActiveBuffs() {
        return List.copyOf(activeBuffs);
    }

    /** Rimuove manualmente un buff per nome (es. dispel). */
    public void removeBuffByName(String name) {
        activeBuffs.removeIf(b -> {
            if (b.getName().equals(name)) {
                b.remove(owner);
                b.deactivate();
                return true;
            }
            return false;
        });
    }

    /** Rimuove tutti i buff (es. cambio scena, morte, cleanse totale). */
    public void clearAll() {
        for (Buff b : activeBuffs) {
            b.remove(owner);
            b.deactivate();
        }
        activeBuffs.clear();
    }
}
