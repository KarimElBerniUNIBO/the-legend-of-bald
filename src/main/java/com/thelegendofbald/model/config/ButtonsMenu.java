package com.thelegendofbald.model.config;

import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JButton;

import com.thelegendofbald.view.factory.JButtonFactory;
import com.thelegendofbald.view.component.JButtonFactoryImpl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
public enum ButtonsMenu {
    /**
     * Button to start the game.
     */
    PLAY("PLAY"),
    /**
     * Button to access settings.
     */
    SETTINGS("SETTINGS"),
    /**
     * Button to view the leaderboard.
     */
    LEADERBOARD("LEADERBOARD"),
    /**
     * Button to quit the application.
     */
    QUIT("QUIT");

    private static final double DEFAULT_ARC_PROPORTION = 0.2;

    private final String text;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private final JButton button;

    ButtonsMenu(final String text) {
        this.text = text;
        this.button = jbFactory.createRoundedButton(this.text, Optional.empty(), DEFAULT_ARC_PROPORTION, Optional.empty(),
                Optional.empty(), Optional.of(Color.BLACK), Optional.empty());
    }

    /**
     * Returns the name associated with this button.
     *
     * @return the name of the button
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns the index associated with this object.
     *
     * @return the index value of this object
     */
    public int getIndex() {
        return this.ordinal();
    }

    /**
     * Returns the underlying {@link JButton} instance associated with this object.
     *
     * @return the {@code JButton} managed by this class
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is designed to return the JButton instance without throwing exceptions."
    )
    public JButton getButton() {
        return this.button;
    }

    /**
     * Returns the maximum index value among all {@code Buttons} enum constants.
     * <p>
     * Iterates through all values of the {@code Buttons} enum, retrieves their index using
     * {@link ButtonsMenu#getIndex()}, and returns the highest index found. If there are no enum
     * constants, returns {@code 0}.
     *
     * @return the maximum index value of all {@code Buttons}, or {@code 0} if none exist
     */
    public static int getMaxIndex() {
        return Arrays.stream(values())
                    .mapToInt(ButtonsMenu::getIndex)
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
    public static ButtonsMenu getIndex(final int index) {
        return Arrays.stream(values())
                .filter(b -> b.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}

