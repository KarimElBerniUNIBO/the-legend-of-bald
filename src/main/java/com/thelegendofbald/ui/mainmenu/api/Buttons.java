package com.thelegendofbald.ui.mainmenu.api;

import java.util.Arrays;

/**
 * Enum representing the main menu buttons in the application.
 * Each button has a display name and an associated index.
 *
 * <p>Available buttons:
 * <ul>
 *     <li>{@link #PLAY} - Play button</li>
 *     <li>{@link #SETTINGS} - Settings button</li>
 *     <li>{@link #LEADERBOARD} - Leaderboard button</li>
 * </ul>
 *
 * <p>Provides utility methods to retrieve button information by index and to get the maximum index value.
 */
public enum Buttons {
    /**
     * Button to start the game.
     */
    PLAY("PLAY", 0),
    /**
     * Button to access settings.
     */
    SETTINGS("SETTINGS", 1),
    /**
     * Button to view the leaderboard.
     */
    LEADERBOARD("LEADERBOARD", 2);

    private final String name;
    private final int index;

    Buttons(final String name, final int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Returns the name associated with this button.
     *
     * @return the name of the button
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the index associated with this object.
     *
     * @return the index value of this object
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the maximum index value among all {@code Buttons} enum constants.
     * <p>
     * Iterates through all values of the {@code Buttons} enum, retrieves their index using
     * {@link Buttons#getIndex()}, and returns the highest index found. If there are no enum
     * constants, returns {@code 0}.
     *
     * @return the maximum index value of all {@code Buttons}, or {@code 0} if none exist
     */
    public static int getMaxIndex() {
        return Arrays.stream(Buttons.values())
                    .mapToInt(Buttons::getIndex)
                    .max()
                    .orElse(0);
    }

    /**
     * Returns the {@code Buttons} enum constant with the specified index.
     *
     * @param index the index of the button to retrieve
     * @return the {@code Buttons} enum constant with the given index
     * @throws IllegalArgumentException if no button with the specified index exists
     */
    public static Buttons getIndex(final int index) {
        return Arrays.stream(Buttons.values())
                .filter(b -> b.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}

