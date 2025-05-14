package com.thelegendofbald.api.panels;

import java.util.Arrays;

/**
 * Enum representing the different panels available in the application UI.
 * Each panel has a unique name and index.
 *
 * <ul>
 *   <li>{@link #MAIN_MENU} - The main menu panel.</li>
 *   <li>{@link #SETTINGS_MENU} - The settings menu panel.</li>
 *   <li>{@link #LEADERBOARD_MENU} - The leaderboard menu panel.</li>
 *   <li>{@link #PLAY_MENU} - The play menu panel.</li>
 * </ul>
 *
 * Provides methods to retrieve the panel's name, index, and the maximum index among all panels.
 */
public enum Panels {
    /**
     * The main menu panel.
     */
    MAIN_MENU("main", 0),
    /**
     * The settings menu panel.
     */
    SETTINGS_MENU("settings", 1),
    /**
     * The leaderboard menu panel.
     */
    LEADERBOARD_MENU("leaderboard", 2),
    /**
     * The game panel.
     */
    PLAY_MENU("play", 3);

    private final String name;
    private final int index;

    Panels(final String name, final int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Returns the name associated with this panel.
     *
     * @return the name of the panel
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the index associated with this instance.
     *
     * @return the index value of this object
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the maximum index value among all {@link Panels} enum constants.
     * <p>
     * This method streams all values of the {@code Panels} enum, extracts their index values
     * using {@link Panels#getIndex()}, and returns the highest index found. If there are no
     * enum constants, it returns {@code 0}.
     *
     * @return the maximum index value of all {@code Panels} enum constants, or {@code 0} if none exist
     */
    public static int getMaxIndex() {
        return Arrays.stream(values())
                    .mapToInt(Panels::getIndex)
                    .max()
                    .orElse(0);
    }
}
