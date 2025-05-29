package com.thelegendofbald.api.settingsmenu;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.thelegendofbald.api.buttons.JButtonFactory;
import com.thelegendofbald.view.buttons.JButtonFactoryImpl;
import com.thelegendofbald.view.buttons.KeybindingButton;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The {@code KeybindsSettings} enum represents the configurable key binding
 * options
 * available in the settings menu of the application. Each enum constant
 * corresponds
 * to a directional control (UP, DOWN, LEFT, RIGHT) and provides associated
 * display text,
 * button text, and a Swing {@link JComponent} for UI representation.
 * <p>
 * Each keybind setting is initialized with a display label and the default key
 * text,
 * and constructs a keybinding button using a {@link JButtonFactory}.
 * </p>
 *
 * <ul>
 * <li>{@link #UP} - Represents the "Up" key binding.</li>
 * <li>{@link #DOWN} - Represents the "Down" key binding.</li>
 * <li>{@link #LEFT} - Represents the "Left" key binding.</li>
 * <li>{@link #RIGHT} - Represents the "Right" key binding.</li>
 * </ul>
 *
 * @see SettingOption
 * @see JButtonFactory
 * @see JComponent
 */
public enum ControlsSettings implements SettingOption {
    /**
     * Represents the "Up" key binding.
     */
    UP("UP", KeyEvent.VK_UP),
    /**
     * Represents the "Down" key binding.
     */
    DOWN("DOWN", KeyEvent.VK_DOWN),
    /**
     * Represents the "Left" key binding.
     */
    LEFT("LEFT", KeyEvent.VK_LEFT),
    /**
     * Represents the "Right" key binding.
     */
    RIGHT("RIGHT", KeyEvent.VK_RIGHT);

    private static final double BUTTONS_ARC_PROPORTION = 0.05;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();

    private final String text;
    private final JComponent jcomponent;
    private int key;

    /**
     * Constructs a new {@code KeybindsSettings} instance with the specified label text and button text.
     * Initializes the associated keybinding button component using the provided parameters.
     *
     * @param text the label or description for the keybinding setting
     * @param keycode the key code associated with the keybinding
     */
    ControlsSettings(final String text, final int keycode) {
        this.text = text;
        this.key = keycode;
        this.jcomponent = jbFactory.createKeybindingButton(KeyEvent.getKeyText(keycode), Optional.empty(),
                BUTTONS_ARC_PROPORTION, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        this.jcomponent.setName(text);
    }

    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Returns the key code associated with this keybinding setting.
     *
     * @return the key code as an {@code int}
     * @see KeyEvent
     */
    /**
     * Returns the key code associated with this key binding.
     *
     * @return the integer value representing the key code
     */
    public int getKey() {
        return this.key;
    }

    /**
     * Sets the key code for this keybinding setting.
     *
     * @param key the new key code to set
     */
    public void setKey(final int key) {
        this.key = key;
        updateButtonText((JButton) this.jcomponent, key);
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "JComponent must be mutable for UI interaction; safe in enum context."
    )
    @Override
    public JComponent getJcomponent() {
        return this.jcomponent;
    }

    /**
     * Returns the key code associated with the specified {@link ControlsSettings} instance.
     *
     * @param keybind the {@code KeybindsSettings} instance for which to retrieve the key code
     * @return the integer key code associated with the given keybind
     */
    public static int getKeyCode(final ControlsSettings keybind) {
        return keybind.getKey();
    }

    private static void updateButtonText(final JButton button, final int keycode) {
        button.setText(KeyEvent.getKeyText(keycode));
        button.repaint();

    }

    /**
     * Sets the key code for the specified {@link ControlsSettings} instance and updates the associated button's text
     * to reflect the new key binding.
     *
     * @param keybind the {@code KeybindsSettings} instance whose key code is to be set
     * @param key the new key code to assign
     */
    public static void setKeyCode(final ControlsSettings keybind, final int key) {
        keybind.setKey(key);
        updateButtonText((JButton) keybind.getJcomponent(), key);
    }

    /**
     * Retrieves the {@code KeybindsSettings} instance associated with the specified {@link KeybindingButton}.
     * <p>
     * This method searches through all available {@code KeybindsSettings} values and returns the one whose
     * display text matches the name of the provided button.
     * </p>
     *
     * @param button the {@code KeybindingButton} for which to find the corresponding keybind setting
     * @return the matching {@code KeybindsSettings} instance
     * @throws IllegalArgumentException if no matching keybind setting is found for the given button
     */
    public static ControlsSettings getKeybind(final KeybindingButton button) {
        return Arrays.stream(values())
                .filter(ks -> ks.getText().equals(button.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Keybind not found for button: " + button.getName()));
    }

}
