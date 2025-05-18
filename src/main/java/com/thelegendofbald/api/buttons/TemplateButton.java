package com.thelegendofbald.api.buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.utils.ImageUtils;

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

    private static final long serialVersionUID = 1L;

    private static final double TEXT_PROPORTION = 0.05;
    private static final double IMAGE_PROPORTION = 0.25;

    private Optional<String> text = Optional.empty();
    private Optional<String> fontName = Optional.empty();
    private Optional<Integer> fontType = Optional.empty();

    private Optional<ImageIcon> icon = Optional.empty();

    private final Pair<Double, Double> moltiplicator;
    private final Color bgColor;
    private final Color fgColor;

    /**
     * Constructs a new {@code TemplateButton} with the specified properties.
     *
     * @param text          the text to be displayed on the button
     * @param moltiplicator a pair of double values used as multipliers for sizing
     *                      or positioning
     * @param bgColor       the background color of the button
     * @param fontName      the name of the font to be used for the button text
     * @param fgColor       the color of the button text
     * @param fontType      the style of the font (e.g., {@link Font#PLAIN},
     *                      {@link Font#BOLD})
     */
    public TemplateButton(final String text, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final String fontName, final Color fgColor, final int fontType) {
        super();
        this.text = Optional.of(text);
        this.moltiplicator = moltiplicator;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.fontName = Optional.of(fontName);
        this.fontType = Optional.of(fontType);
        this.initialize();
    }

    /**
     * Constructs a new {@code TemplateButton} with the specified icon, parent size,
     * size multipliers, background color, and foreground color.
     *
     * @param icon          the {@link ImageIcon} to display on the button
     * @param moltiplicator a {@link Pair} of {@link Double} values used as size
     *                      multipliers
     * @param bgColor       the background {@link Color} of the button
     * @param fgColor       the foreground {@link Color} (text/icon color) of the
     *                      button
     */
    public TemplateButton(final ImageIcon icon, final Pair<Double, Double> moltiplicator,
            final Color bgColor, final Color fgColor) {
        super();
        this.icon = Optional.of(icon);
        this.moltiplicator = moltiplicator;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.initialize();
    }

    /**
     * Called when the component is added to a container or made displayable.
     * <p>
     * Subclasses can override this method to perform additional initialization
     * when the button is added to a parent container. If overridden, ensure
     * that {@code super.addNotify()} is called at the beginning of the method
     * to preserve the default behavior.
     * </p>
     */
    @Override
    public void addNotify() {
        super.addNotify();
        this.text.ifPresent(t -> {
            this.setText(t);
            Optional.ofNullable(this.getFont()).ifPresentOrElse(
                                font -> this.setFont(font.deriveFont((float) this.getFontSize(this.getParent().getSize()))),
                                () -> {
                                    this.setFont(new Font(this.fontName.get(), this.fontType.get(),
                                    this.getFontSize(this.getParent().getSize())));
                                });
        });
        this.icon.ifPresent(i -> this.setIcon(this.getImageResized(i)));
        this.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setBackground(bgColor);
            this.setForeground(fgColor);
            this.setFocusable(true);
            this.setFocusTraversalKeysEnabled(false);
            this.setFocusPainted(false);
        });
    }

    /**
     * Calculates the font size for the button based on the parent container's size
     * and the scaling multipliers.
     * <p>
     * Subclasses can override this method to provide custom font size calculations.
     * If overridden, ensure that the returned value is a positive integer.
     * </p>
     *
     * @param parentSize the size of the parent container
     * @return the calculated font size, always greater than or equal to 1
     */
    public int getFontSize(final Dimension parentSize) {
        final double width = parentSize.getWidth() * this.moltiplicator.getLeft();
        final double height = parentSize.getHeight() * this.moltiplicator.getRight();
        final double aspectRatio = Math.min(1.2, width / height);
        final int scalingFactor = (int) (Math.sqrt(width * height) * aspectRatio * TEXT_PROPORTION);

        return Math.max(1, scalingFactor);
    }

    /**
     * Resizes the provided {@link ImageIcon} to fit within the parent container,
     * maintaining the original aspect ratio.
     * <p>
     * Subclasses can override this method to provide custom resizing logic. If
     * overridden, ensure that the returned {@link ImageIcon} is not null and has
     * valid dimensions.
     * </p>
     *
     * @param image the {@link ImageIcon} to resize
     * @return the resized {@link ImageIcon}
     */
    public ImageIcon getImageResized(final ImageIcon image) {
        final var panelWidth = this.getParent().getWidth();
        final var panelHeight = this.getParent().getHeight();

        final int originalWidth = image.getIconWidth();
        final int originalHeight = image.getIconHeight();

        final double aspectRatio = (double) originalWidth / originalHeight;

        int newWidth = (int) (panelHeight * IMAGE_PROPORTION * aspectRatio);
        int newHeight = (int) (panelHeight * IMAGE_PROPORTION);

        if (newWidth > panelWidth) {
            newWidth = panelWidth;
            newHeight = (int) (panelWidth / aspectRatio);
        }

        newWidth = Math.max(newWidth, 1);
        newHeight = Math.max(newHeight, 1);

        return ImageUtils.scaleImageIcon(image, newWidth, newHeight);
    }

}
