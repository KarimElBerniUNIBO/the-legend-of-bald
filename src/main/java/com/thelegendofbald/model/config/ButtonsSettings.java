package com.thelegendofbald.model.config;

import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JButton;

import com.thelegendofbald.view.factory.JButtonFactory;
import com.thelegendofbald.view.panel.base.Panels;
import com.thelegendofbald.view.component.JButtonFactoryImpl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Enum representing the buttons available in the game options.
 * Each button has a text label and an associated panel (if applicable).
 */
public enum ButtonsSettings {
    /**
     * Button to resume the game.
     */
    RESUME("RESUME", Optional.empty()),
    /**
     * Button to open the settings menu.
     */
    SETTINGS("SETTINGS", Optional.of(Panels.SETTINGS)),
    /**
     * Button to leave the game and return to the main menu.
     */
    LEAVE("LEAVE", Optional.of(Panels.MAIN_MENU)),;

    private static final double DEFAULT_ARC_PROPORTION = 0.2;

    private final String text;
    private final Optional<Panels> panel;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private final JButton button;

    ButtonsSettings(final String text, final Optional<Panels> panel) {
        this.text = text;
        this.panel = panel;
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
     * Returns the {@link Panels} associated with this button.
     *
     * @return the {@code Panels} associated with this button
     */
    public Optional<Panels> getPanel() {
        return this.panel;
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
     * {@link ButtonsSettings#getIndex()}, and returns the highest index found. If there are no enum
     * constants, returns {@code 0}.
     *
     * @return the maximum index value of all {@code Buttons}, or {@code 0} if none exist
     */
    public static int getMaxIndex() {
        return Arrays.stream(values())
                    .mapToInt(ButtonsSettings::getIndex)
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
    public static ButtonsSettings getIndex(final int index) {
        return Arrays.stream(values())
                .filter(b -> b.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}


