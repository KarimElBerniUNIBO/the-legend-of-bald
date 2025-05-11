package com.thelegendofbald.api.buttons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.util.Pair;

/**
 * An abstract template for custom JButton components with configurable
 * appearance and scaling.
 * <p>
 * TemplateButton provides constructors for both text and icon buttons, allowing
 * dynamic sizing
 * and styling based on the parent container's dimensions and provided scaling
 * factors.
 * </p>
 *
 * <ul>
 * <li>Supports proportional font sizing based on parent size and custom scaling
 * multipliers.</li>
 * <li>Allows customization of background, foreground, font, and icon.</li>
 * <li>Disables focus painting and traversal keys for a cleaner UI
 * experience.</li>
 * </ul>
 *
 * <p>
 * Subclasses should implement specific button behaviors as needed.
 * </p>
 */
public abstract class TemplateButton extends JButton {

    private static final double TEXT_PROPORTION = 0.05;

    private final Pair<Double, Double> moltiplicator;

    /**
     * Constructs a new {@code TemplateButton} with the specified properties.
     *
     * @param text          the text to be displayed on the button
     * @param parentSize    the size of the parent component, used to determine the button's font size
     * @param moltiplicator a pair of double values used as multipliers for sizing or positioning
     * @param bgColor       the background color of the button
     * @param fontName      the name of the font to be used for the button text
     * @param fontColor     the color of the button text
     * @param fontType      the style of the font (e.g., {@link Font#PLAIN}, {@link Font#BOLD})
     */
    public TemplateButton(final String text, final Dimension parentSize, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final String fontName, final Color fontColor, final int fontType) {
        super();
        this.moltiplicator = moltiplicator;
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fontColor);
        this.setFont(new Font(fontName, fontType, this.getFontSize(parentSize)));
        this.initialize();
    }

    /**
     * Constructs a new {@code TemplateButton} with the specified icon, parent size, 
     * size multipliers, background color, and foreground color.
     *
     * @param icon         the {@link ImageIcon} to display on the button
     * @param parentSize   the {@link Dimension} representing the parent component's size
     * @param moltiplicator a {@link Pair} of {@link Double} values used as size multipliers
     * @param bgColor      the background {@link Color} of the button
     * @param fgColor      the foreground {@link Color} (text/icon color) of the button
     */
    public TemplateButton(final ImageIcon icon, final Dimension parentSize, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final Color fgColor) {
        super();
        this.moltiplicator = moltiplicator;
        this.setIcon(icon);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(bgColor);
        this.setForeground(fgColor);
        this.initialize();
    }

    private void initialize() {
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.setFocusPainted(false);
    }

    /**
     * Called when the component is added to a container or made displayable.
     * <p>
     * Subclasses can override this method to perform additional initialization
     * when the button is added to a parent container. If overridden, ensure
     * that {@code super.addNotify()} is called to preserve the default behavior.
     * </p>
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (this.getText() != null && !this.getText().isEmpty()) {
            SwingUtilities.invokeLater(() -> Optional.ofNullable(this.getParent()).ifPresent(
                    parent -> this.setFont(this.getFont().deriveFont((float) this.getFontSize(parent.getSize())))));
        }
    }

    private int getFontSize(final Dimension parentSize) {
        final double width = parentSize.getWidth() * this.moltiplicator.getFirst();
        final double height = parentSize.getHeight() * this.moltiplicator.getSecond();
        final double aspectRatio = Math.min(1.2, width / height);
        final int scalingFactor = (int) (Math.sqrt(width * height) * aspectRatio * TEXT_PROPORTION);

        return Math.max(1, scalingFactor);
    }

}
