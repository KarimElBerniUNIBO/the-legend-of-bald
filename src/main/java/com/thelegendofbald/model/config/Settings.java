package com.thelegendofbald.model.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;

import com.thelegendofbald.view.panel.settings.SettingsEditor;

/**
 * The {@code Settings} enum represents different categories of application
 * settings,
 * such as VIDEO, AUDIO, and KEYBINDS. Each enum constant holds an array of
 * {@link SettingOption}
 * configurations, and optionally references to a {@link SettingsEditor} and a
 * linked {@link JButton}.
 * <p>
 * This enum provides methods to access and modify the associated editor and
 * button,
 * retrieve configuration options, and utility methods for working with enum
 * indices.
 *
 * <ul>
 * <li>{@link #VIDEO} - Video-related settings.</li>
 * <li>{@link #AUDIO} - Audio-related settings.</li>
 * <li>{@link #CONTROLS} - Keybinding-related settings.</li>
 * </ul>
 *
 * @see SettingOption
 * @see SettingsEditor
 * @see JButton
 */
public enum Settings {
    /**
     * Enum constant representing video settings.
     * <p>
     * This constant holds an array of {@link SettingOption} related to video
     * configurations.
     * </p>
     */
    VIDEO(VideoSettings.values(), Optional.empty(), Optional.empty()),
    /**
     * Enum constant representing audio settings.
     * <p>
     * This constant holds an array of {@link SettingOption} related to audio
     * configurations.
     * </p>
     */
    AUDIO(AudioSettings.values(), Optional.empty(), Optional.empty()),
    /**
     * Enum constant representing keybinding settings.
     * <p>
     * This constant holds an array of {@link SettingOption} related to keybinding
     * configurations.
     * </p>
     */
    CONTROLS(ControlsSettings.values(), Optional.empty(), Optional.empty());

    private final SettingOption[] configs;
    private Optional<SettingsEditor> settingsEditor;
    private Optional<JButton> linkedButton;

    /**
     * Constructs a new {@code Settings} instance with the specified configuration
     * options,
     * an optional settings editor, and an optional linked button.
     *
     * @param settings       an array of {@link SettingOption} representing the
     *                       available settings configurations
     * @param settingsEditor an {@link Optional} containing a {@link SettingsEditor}
     *                       for editing settings, or empty if not available
     * @param linkedButton   an {@link Optional} containing a {@link JButton} that
     *                       is linked to these settings, or empty if not available
     */
    Settings(final SettingOption[] settings, final Optional<SettingsEditor> settingsEditor,
            final Optional<JButton> linkedButton) {
        this.configs = settings;
        this.settingsEditor = settingsEditor;
        this.linkedButton = linkedButton;
    }

    /**
     * Returns the name of this enum constant as a String.
     *
     * @return the name of this enum constant
     */
    public String getName() {
        return this.name();
    }

    /**
     * Returns a list of all configuration options available in this settings
     * instance.
     *
     * @return a {@link List} of {@link SettingOption} representing the current
     *         configuration options
     */
    public List<SettingOption> getConfigs() {
        return Arrays.stream(configs).toList();
    }

    /**
     * Returns the {@link SettingsEditor} instance associated with this
     * {@code Settings} object.
     * <p>
     * If the {@code settingsEditor} is not present, this method throws a
     * {@link NullPointerException}.
     *
     * @return the {@code SettingsEditor} instance
     * @throws NullPointerException if the {@code settingsEditor} is not present
     */
    public SettingsEditor getSettingsEditor() {
        return this.settingsEditor.orElseThrow(() -> new NullPointerException());
    }

    /**
     * Sets the {@link SettingsEditor} instance to be used for editing settings.
     *
     * @param settingsEditor the {@code SettingsEditor} to associate with this
     *                       settings object
     */
    public void setSettingsEditor(final SettingsEditor settingsEditor) {
        this.settingsEditor = Optional.of(settingsEditor);
    }

    /**
     * Returns the linked {@link JButton} associated with this settings instance.
     *
     * @return the linked {@code JButton}
     * @throws NullPointerException if the linked button is not present
     */
    public JButton getLinkedButton() {
        return this.linkedButton.orElseThrow(() -> new NullPointerException());
    }

    /**
     * Sets the button that is linked to this settings instance.
     * This method wraps the provided {@link JButton} in an {@link Optional}
     * and assigns it to the {@code linkedButton} field.
     *
     * @param button the {@link JButton} to be linked; must not be {@code null}
     */
    public void setLinkedButton(final JButton button) {
        this.linkedButton = Optional.of(button);
    }

    /**
     * Returns the ordinal index of this enum constant.
     * The ordinal is the position of the enum constant in its enum declaration,
     * where the initial constant is assigned an ordinal of zero.
     *
     * @return the ordinal index of this enum constant
     */
    public int getIndex() {
        return this.ordinal();
    }

    /**
     * Returns the maximum index value among all {@code Settings} enum constants.
     * <p>
     * This method iterates through all values of the {@code Settings} enum,
     * retrieves their index using {@link #getIndex()}, and returns the highest
     * index found.
     * If there are no enum constants, it returns {@code 0}.
     *
     * @return the maximum index value of all {@code Settings} enum constants, or
     *         {@code 0} if none exist
     */
    public static int getMaxIndex() {
        return Arrays.stream(values())
                .mapToInt(Settings::getIndex)
                .max()
                .orElse(0);
    }

    /**
     * Retrieves the {@code Settings} enum constant corresponding to the specified
     * index.
     *
     * <p>
     * This method searches through all available {@code Settings} enum values and
     * returns the one
     * whose index matches the provided {@code index} parameter.
     * </p>
     *
     * @param index the index of the desired {@code Settings} enum constant
     * @return the {@code Settings} enum constant with the specified index
     * @throws IllegalArgumentException if no {@code Settings} enum constant with
     *                                  the given index exists
     */
    public static Settings getSettingByIndex(final int index) {
        return Arrays.stream(values())
                .filter(b -> b.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
