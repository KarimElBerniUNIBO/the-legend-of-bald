package com.thelegendofbald.view.buttons;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.controller.listeners.buttons.KeybindingButtonKeyListener;
import com.thelegendofbald.controller.listeners.buttons.KeybindingButtonMouseListener;

/**
 * A custom button component for keybinding settings in the UI.
 * <p>
 * The {@code KeybindingButton} extends {@link RoundedButton} and provides
 * additional
 * functionality to handle keybinding changes. It manages a "changing" state to
 * indicate
 * when the button is waiting for a new key input from the user.
 * </p>
 * <p>
 * The button can be initialized with either text or an icon, and supports
 * custom
 * appearance parameters such as background color, font, and arc proportion.
 * </p>
 *
 * <p>
 * Listeners are attached to handle mouse and key events for keybinding
 * operations.
 * </p>
 *
 * @see RoundedButton
 */
public class KeybindingButton extends RoundedButton {

    private volatile boolean changing;

    /**
     * Constructs a new {@code KeybindingButton} with the specified properties.
     *
     * @param text           the text to display on the button
     * @param moltiplicator  a pair of doubles used to scale the button's size or position
     * @param arcProportion  the proportion of the button's corners to be rounded
     * @param bgColor        the background color of the button
     * @param fontName       the name of the font to use for the button's text
     * @param fontColor      the color of the button's text
     * @param fontType       the style of the font (e.g., plain, bold, italic)
     */
    public KeybindingButton(final String text, final Pair<Double, Double> moltiplicator,
            final double arcProportion,
            final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        super(text, moltiplicator, arcProportion, bgColor, fontName, fontColor, fontType);
        this.initialize();
    }

    /**
     * Constructs a new {@code KeybindingButton} with the specified icon, window size, 
     * size multipliers, arc proportion, background color, and foreground color.
     *
     * @param icon           the {@link ImageIcon} to display on the button
     * @param moltiplicator  a {@link Pair} of {@code Double} values used as size multipliers
     * @param arcProportion  the proportion of the button's arc (for rounded corners)
     * @param bgColor        the background {@link Color} of the button
     * @param fgColor        the foreground {@link Color} of the button
     */
    public KeybindingButton(final ImageIcon icon, final Pair<Double, Double> moltiplicator,
            final double arcProportion, final Color bgColor, final Color fgColor) {
        super(icon, moltiplicator, arcProportion, bgColor, fgColor);
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.addMouseListener(new KeybindingButtonMouseListener(this));
            this.addKeyListener(new KeybindingButtonKeyListener(this));
        });
    }

    /**
     * Sets the "changing" state of the button.
     * <p>
     * Subclasses can override this method to provide custom behavior when the
     * "changing" state is updated. If overridden, ensure that the state remains
     * consistent with the button's lifecycle.
     * </p>
     *
     * @param changing {@code true} to set the button to the "changing" state,
     *                 {@code false} otherwise
     */
    public void setChanging(final boolean changing) {
        this.changing = changing;
    }

    /**
     * Checks if the button is in the "changing" state.
     * <p>
     * Subclasses can override this method to provide custom logic for determining
     * the "changing" state. If overridden, ensure that the method remains
     * thread-safe and consistent with the button's lifecycle.
     * </p>
     *
     * @return {@code true} if the button is in the "changing" state, {@code false} otherwise
     */
    public boolean isChanging() {
        return this.changing;
    }

}
