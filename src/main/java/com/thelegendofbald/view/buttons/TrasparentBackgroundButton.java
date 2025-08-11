package com.thelegendofbald.view.buttons;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.buttons.TemplateButton;
import com.thelegendofbald.controller.listeners.buttons.TrasparentBackgroundButtonMouseListener;

/**
 * A custom button with a transparent background and selectable foreground
 * color.
 * <p>
 * The {@code TrasparentBackgroundButton} extends {@link TemplateButton} and
 * provides
 * a button that does not fill its content area or paint its border, making it
 * appear
 * transparent. It supports selection state, changing its foreground color when
 * selected.
 * </p>
 *
 * <ul>
 * <li>Foreground color changes to yellow when selected.</li>
 * <li>Supports both text and icon-based constructors.</li>
 * <li>Mouse listener is added for custom interaction handling.</li>
 * </ul>
 */
public class TrasparentBackgroundButton extends TemplateButton {

    private static final long serialVersionUID = 1L;

    private final Color buttonFGColor;
    private final Color buttonFGSelectedColor;

    private boolean selected;

    /**
     * Constructs a button with text and custom properties.
     *
     * @param text         the text to display on the button
     * @param moltiplicator the scaling factor for the button
     * @param bgColor      the background color of the button
     * @param fontName     the name of the font
     * @param fontColor    the color of the font
     * @param fontType     the type of the font (e.g., bold, italic)
     */
    public TrasparentBackgroundButton(final String text, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final String fontName,
            final Color fontColor, final int fontType) {
        super(text, moltiplicator, bgColor, fontName, fontColor, fontType);
        this.buttonFGColor = fontColor;
        this.buttonFGSelectedColor = Color.YELLOW;
        this.initialize();
    }

    /**
     * Constructs a button with an icon and custom properties.
     *
     * @param icon         the icon to display on the button
     * @param moltiplicator the scaling factor for the button
     * @param bgColor      the background color of the button
     * @param fgColor      the foreground color of the button
     */
    public TrasparentBackgroundButton(final ImageIcon icon, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final Color fgColor) {
        super(icon, moltiplicator, bgColor, fgColor);
        this.buttonFGColor = fgColor;
        this.buttonFGSelectedColor = Color.YELLOW;
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.addMouseListener(new TrasparentBackgroundButtonMouseListener(this));
        });
    }

    /**
     * Called when the component is added to a container or made displayable.
     * This override ensures that if the button is currently selected when it is added,
     * its foreground color is set to the selected color.
     * <p>
     * Always calls the superclass implementation first.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (this.isSelected()) {
            this.setForeground(buttonFGSelectedColor);
        }
    }

    /**
     * Selects the button, changing its foreground color to the selected color.
     */
    public void select() {
        this.selected = true;
        SwingUtilities.invokeLater(() -> {
            this.setForeground(buttonFGSelectedColor);
            this.repaint();
        });
    }

    /**
     * Unselects the button, restoring its original foreground color.
     */
    public void unselect() {
        this.selected = false;
        SwingUtilities.invokeLater(() -> {
            this.setForeground(buttonFGColor);
            this.repaint();
        });
    }

    /**
     * Checks if the button is selected.
     *
     * @return {@code true} if the button is selected, {@code false} otherwise
     */
    @Override
    public boolean isSelected() {
        return this.selected;
    }

}
