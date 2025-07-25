package com.thelegendofbald.view.buttons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.buttons.TemplateButton;
import com.thelegendofbald.controller.listeners.buttons.RoundedButtonMouseListener;

/**
 * A custom JButton implementation that renders a button with rounded corners.
 * The arc radius for the corners is determined by a proportion of the button's
 * width.
 * 
 * <p>
 * RoundedButton supports both text and icon-based constructors, allowing
 * flexible
 * customization of background color, font, and corner roundness.
 * </p>
 *
 * <p>
 * Features:
 * <ul>
 * <li>Customizable arc proportion for rounded corners.</li>
 * <li>Supports both text and icon content.</li>
 * <li>Custom background and font settings.</li>
 * <li>Antialiased rendering for smooth edges.</li>
 * <li>Mouse listener for custom interaction effects.</li>
 * </ul>
 * </p>
 *
 * @see TemplateButton
 * @see RoundedButtonMouseListener
 */
public class RoundedButton extends TemplateButton {

    private static final long serialVersionUID = 1L;

    private final double arcProportion;

    /**
     * Constructs a new {@code RoundedButton} with the specified properties.
     *
     * @param text          the text to be displayed on the button
     * @param moltiplicator a pair of multipliers to scale the button's width and
     *                      height relative to the parent
     * @param arcProportion the proportion of the button's arc (corner roundness)
     * @param bgColor       the background color of the button
     * @param fontName      the name of the font to use for the button text
     * @param fontColor     the color of the button text
     * @param fontType      the style of the font (e.g.,
     *                      {@link java.awt.Font#PLAIN}, {@link java.awt.Font#BOLD})
     */
    public RoundedButton(final String text, final Pair<Double, Double> moltiplicator,
            final double arcProportion, final Color bgColor, final String fontName,
            final Color fontColor, final int fontType) {
        super(text, moltiplicator, bgColor, fontName, fontColor, fontType);
        this.arcProportion = arcProportion;
        this.initialize();
    }

    /**
     * Constructs a new {@code RoundedButton} with the specified icon, parent size,
     * size multipliers,
     * arc proportion for rounded corners, background color, and foreground color.
     *
     * @param icon          the {@link ImageIcon} to display on the button
     * @param moltiplicator a {@link Pair} of {@code Double} values used to scale
     *                      the button's size
     * @param arcProportion the proportion (as a double) used to determine the arc
     *                      radius for rounded corners
     * @param bgColor       the background {@link Color} of the button
     * @param fgColor       the foreground {@link Color} (typically the text or icon
     *                      color) of the button
     */
    public RoundedButton(final ImageIcon icon, final Pair<Double, Double> moltiplicator,
            final double arcProportion,
            final Color bgColor, final Color fgColor) {
        super(icon, moltiplicator, bgColor, fgColor);
        this.arcProportion = arcProportion;
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setBorderPainted(false);
            this.setContentAreaFilled(false);
            this.addMouseListener(new RoundedButtonMouseListener(this));
        });
    }

    private int getArcValue() {
        return (int) (this.getWidth() * this.arcProportion);
    }

    /**
     * Paints the component with rounded corners.
     * <p>
     * Subclasses can override this method to customize the painting logic. If overridden,
     * ensure that {@code super.paintComponent(Graphics)} is called to preserve the default
     * behavior of rendering the button's content.
     * </p>
     *
     * @param g the {@link Graphics} object used for painting
     */
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        final int arcValue = this.getArcValue();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getBackground());
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), arcValue, arcValue);

        super.paintComponent(g2);
    }

}
