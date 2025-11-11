package com.thelegendofbald.model.common;

/**
 * Represent the class of the Wallet that contains coins.
 * 
 * It is used to interact with the shop.
 */
public final class Wallet {

    private int coins;

    /**
     * Default constructor of the wallet.
     */
    public Wallet() {
        this.coins = 0;
    }

    /**
     * Constructor that starts with the same amount of coins like other wallet.
     * 
     * @param other the wallet to copy the number of coins.
     */
    public Wallet(final Wallet other) {
        this.coins = other.coins;
    }

    /**
     * Constructor that start with amount of coins specified.
     * 
     * @param initialCoins the number of coins.
     */
    public Wallet(final int initialCoins) {
        this.coins = initialCoins;
    }

    /**
     * Returns the amount of the coins.
     * 
     * @return the amount of coints as int.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * The method to add a number of coins to the wallet.
     * 
     * @param amount the number of coins to add.
     */
    public void addCoins(final int amount) {
        this.coins += amount;
    }

    /**
     * The method that manage the spend of the coins.
     * 
     * @param amount the amount of the coins as int
     * @return the success of the operation as boolean.
     */
    public boolean spendCoins(final int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }
}


