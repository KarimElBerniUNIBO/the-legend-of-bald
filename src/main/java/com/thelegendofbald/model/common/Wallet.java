package com.thelegendofbald.model.common;

/**
 * Represents a wallet that stores the player's coins.
 * Used by the shop and other game systems.
 */
public final class Wallet {

    private int coins;

    /**
     * Creates an empty wallet.
     */
    public Wallet() {
        this.coins = 0;
    }

    /**
     * Creates a wallet copying the coin amount from another wallet.
     *
     * @param other the wallet to copy coins from
     */
    public Wallet(final Wallet other) {
        this.coins = other.coins;
    }

    /**
     * Creates a wallet with a predefined number of coins.
     *
     * @param initialCoins the initial amount of coins
     */
    public Wallet(final int initialCoins) {
        this.coins = initialCoins;
    }

    /**
     * Returns the number of coins stored in the wallet.
     *
     * @return the current coin amount
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Adds coins to the wallet.
     *
     * @param amount the amount to add
     */
    public void addCoins(final int amount) {
        this.coins += amount;
    }

    /**
     * Checks whether the wallet contains at least the given amount.
     *
     * @param amount the amount to check
     * @return true if the wallet contains enough coins
     */
    public boolean hasEnough(final int amount) {
        return this.coins >= amount;
    }

    /**
     * Removes the specified amount of coins.
     * Assumes that the wallet has already been validated with {@link #hasEnough(int)}.
     *
     * @param amount the amount to remove
     */
    public void removeCoins(final int amount) {
        this.coins -= amount;
    }
}
