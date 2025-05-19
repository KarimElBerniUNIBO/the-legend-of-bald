package com.thelegendofbald.api.panels;

import java.util.Arrays;
import java.util.Optional;

import com.thelegendofbald.api.mainmenu.Buttons;
import com.thelegendofbald.view.game.GamePanel;
import com.thelegendofbald.view.leaderboard.LeaderBoardPanel;
import com.thelegendofbald.view.mainmenu.MainPanel;
import com.thelegendofbald.view.settingsmenu.SettingsPanel;

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
    MAIN_MENU("MAIN", new MainPanel(), Optional.empty()),
    /**
     * The settings menu panel.
     */
    SETTINGS_MENU("SETTINGS", new SettingsPanel(), Optional.of(Buttons.SETTINGS)),
    /**
     * The leaderboard menu panel.
     */
    LEADERBOARD_MENU("LEADERBOARD", new LeaderBoardPanel(), Optional.of(Buttons.LEADERBOARD)),
    /**
     * The game panel.
     */
    PLAY_MENU("PLAY", new GamePanel(), Optional.of(Buttons.PLAY));

    private final String name;
    private final MenuPanel panel;
    private final Optional<Buttons> enumButton;

    Panels(final String name, final MenuPanel panel, final Optional<Buttons> enumButton) {
        this.name = name;
        this.panel = panel;
        this.enumButton = enumButton;
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
     * Returns the current instance of {@link MenuPanel}.
     *
     * @return the {@code MenuPanel} associated with this object
     */
    public MenuPanel getPanel() {
        return this.panel;
    }

    /**
     * Returns an {@link Optional} containing the {@link Buttons} instance associated with this object, if present.
     *
     * @return an {@code Optional<Buttons>} representing the button, or an empty {@code Optional} if no button is present
     */
    public Optional<Buttons> getEnumButton() {
        return this.enumButton;
    }

    /**
     * Returns the index of this panel.
     * <p>
     * The index is determined by the ordinal value of the enum constant.
     *
     * @return the index of the panel
     */
    public int getIndex() {
        return this.ordinal();
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
