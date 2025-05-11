package com.thelegendofbald.api.settingsmenu;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.JComponent;

import com.thelegendofbald.api.buttons.JButtonFactory;
import com.thelegendofbald.view.buttons.JButtonFactoryImpl;

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
public enum KeybindsSettings implements SettingOption {
    /**
     * Represents the "Up" key binding.
     */
    UP("UP", KeyEvent.getKeyText(KeyEvent.VK_UP)),
    /**
     * Represents the "Down" key binding.
     */
    DOWN("DOWN", KeyEvent.getKeyText(KeyEvent.VK_DOWN)),
    /**
     * Represents the "Left" key binding.
     */
    LEFT("LEFT", KeyEvent.getKeyText(KeyEvent.VK_LEFT)),
    /**
     * Represents the "Right" key binding.
     */
    RIGHT("RIGHT", KeyEvent.getKeyText(KeyEvent.VK_RIGHT));

    private static final double BUTTONS_ARC_PROPORTION = 0.05;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();

    private final String text;
    private final String buttonText;
    private final JComponent jcomponent;

    /**
     * Constructs a new {@code KeybindsSettings} instance with the specified label text and button text.
     * Initializes the associated keybinding button component using the provided parameters.
     *
     * @param text the label or description for the keybinding setting
     * @param buttonText the text to display on the keybinding button
     */
    KeybindsSettings(final String text, final String buttonText) {
        this.text = text;
        this.buttonText = buttonText;
        this.jcomponent = jbFactory.createKeybindingButton(text, new Dimension(0, 0), Optional.empty(),
                BUTTONS_ARC_PROPORTION, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Returns the text displayed on the button associated with this keybind setting.
     *
     * @return the button text as a {@code String}
     */
    public String getButtonText() {
        return this.buttonText;
    }

    @Override
    public JComponent getJcomponent() {
        return this.jcomponent;
    }

}
