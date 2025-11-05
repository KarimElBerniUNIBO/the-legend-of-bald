package com.thelegendofbald.model.common;

public class Wallet {

    private int coins;

    // Costruttore vuoto
    public Wallet() {
        this.coins = 0;
    }
    public Wallet(Wallet other) { 
        this.coins = other.coins;
    }


    // Costruttore con inizializzazione
    public Wallet(int initialCoins) {
        this.coins = initialCoins;
    }

    // Getter
    public int getCoins() {
        return coins;
    }

    public int getGold() { // 👈 Alias per chiarezza nello shop
        return coins;
    }

    // Aggiungi oro
    public void addCoins(int amount) {
        this.coins += amount;
    }

    // Spendi oro
    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }
}


