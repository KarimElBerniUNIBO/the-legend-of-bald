package com.thelegendofbald.view.panel.base;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import com.thelegendofbald.model.config.ButtonsMenu;
import com.thelegendofbald.view.panel.game.GamePanel;
import com.thelegendofbald.view.panel.leaderboard.LeaderBoardPanel;
import com.thelegendofbald.view.panel.menu.MainPanel;
import com.thelegendofbald.view.panel.settings.SettingsPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Enum representing the different panels available in the application UI.
 * Each panel has a unique name and index.
 *
 * <ul>
 *   <li>{@link #MAIN_MENU} - The main menu panel.</li>
 *   <li>{@link #SETTINGS} - The settings menu panel.</li>
 *   <li>{@link #LEADERBOARD} - The leaderboard menu panel.</li>
 *   <li>{@link #GAME_MENU} - The play menu panel.</li>
 * </ul>
 *
 * Provides methods to retrieve the panel's name, index, and the maximum index among all panels.
 */
public enum Panels {
    /**
     * The main menu panel.
     */
    MAIN_MENU("MAIN MENU", MainPanel::new, Optional.empty()),
    /**
     * The settings menu panel.
     */
    SETTINGS("SETTINGS", SettingsPanel::new, Optional.of(ButtonsMenu.SETTINGS)),
    /**
     * The leaderboard menu panel.
     */
    LEADERBOARD("LEADERBOARD", LeaderBoardPanel::new, Optional.of(ButtonsMenu.LEADERBOARD)),
    /**
     * The game panel.
     */
    GAME_MENU("GAME", GamePanel::new, Optional.of(ButtonsMenu.PLAY));

    private final String name;
    private final Supplier<MenuPanel> panelSupplier;
    private MenuPanel panel;
    private final Optional<ButtonsMenu> enumButton;

    Panels(final String name, final Supplier<MenuPanel> panelSupplier, final Optional<ButtonsMenu> enumButton) {
        this.name = name;
        this.panelSupplier = panelSupplier;
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
    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is designed to return the MenuPanel instance without throwing exceptions."
    )
    public MenuPanel getPanel() {
        if (Optional.ofNullable(this.panel).isEmpty()) {
            this.panel = this.panelSupplier.get();
        }
        return this.panel;
    }

    /**
     * Returns an {@link Optional} containing the {@link ButtonsMenu} instance associated with this object, if present.
     *
     * @return an {@code Optional<Buttons>} representing the button, or an empty {@code Optional} if no button is present
     */
    public Optional<ButtonsMenu> getEnumButton() {
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
