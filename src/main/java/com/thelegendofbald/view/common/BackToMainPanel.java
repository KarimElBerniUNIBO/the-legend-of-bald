package com.thelegendofbald.view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.api.buttons.JButtonFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.controller.ui.mainmenu.SwitchToOtherPanel;
import com.thelegendofbald.utils.ImageUtils;
import com.thelegendofbald.view.buttons.JButtonFactoryImpl;
import com.thelegendofbald.view.main.GameWindow;

/**
 * BackToMainPanel is a custom panel that displays a "Back" button, typically used to return to the main menu.
 * <p>
 * The panel is transparent and uses a proportion of its parent container's size to determine its own dimensions.
 * The "Back" button displays an image, which is resized dynamically based on the panel's size.
 * When the button is clicked, it triggers a switch to the main menu panel.
 * </p>
 *
 * <ul>
 *   <li>WIDTH_PROPORTION and HEIGHT_PROPORTION define the panel's size relative to its parent.</li>
 *   <li>IMAGE_PROPORTION determines the size of the button's image relative to the panel's height.</li>
 *   <li>The button is created using a JButtonFactory and is styled to be transparent with a white foreground.</li>
 *   <li>Image resizing maintains the aspect ratio and ensures the image fits within the panel.</li>
 *   <li>Component sizes and images are updated dynamically when the panel's size changes.</li>
 * </ul>
 *
 * @see AdapterPanel
 * @see JButtonFactory
 * @see ImageUtils
 */
public final class BackToMainPanel extends AdapterPanel {

    /**
     * The proportion of the panel's width relative to its parent container.
     * A value of 0.1 means the panel will occupy 10% of the available width.
     */
    public static final double WIDTH_PROPORTION = 0.1;
    /**
     * The proportion of the panel's height relative to its parent container.
     * A value of 0.2 means the panel will occupy 20% of the available height.
     */
    public static final double HEIGHT_PROPORTION = 0.2;

    private static final String PATH = "/images/buttons/back.png";
    private static final double IMAGE_PROPORTION = 0.25;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private Optional<JButton> backButton = Optional.empty();

    private Optional<ImageIcon> originalImage = Optional.empty();

    /**
     * Constructs a new {@code BackToMainPanel} with the specified size.
     * The panel is set to be non-opaque and uses a centered {@link FlowLayout}.
     *
     * @param size the preferred size of the panel
     */
    public BackToMainPanel(final Dimension size) {
        super(size);
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    @Override
    protected void initializeComponents() {
        this.originalImage = Optional.of(new ImageIcon(this.getClass().getResource(PATH)));
        final var imageProportion = this.getWidth() > 0 && this.getHeight() > 0
                ? new Fraction(this.getWidth() / this.getHeight())
                : new Fraction(1, 1);

        this.originalImage.ifPresent(
                image -> this.backButton = Optional.of(jbFactory.createTrasparentButton(this.getImageResized(image),
                        new Dimension(imageProportion.getNumerator(), imageProportion.getDenominator()),
                        Optional.empty(), Optional.of(Color.WHITE))));
        this.backButton.ifPresent(button -> button.addActionListener(e -> {
            final var parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
            new SwitchToOtherPanel(parent, Panels.MAIN_MENU).actionPerformed(e);
        }));
    }

    private ImageIcon getImageResized(final ImageIcon image) {
        final var panelWidth = this.getWidth();
        final var panelHeight = this.getHeight();

        final int originalWidth = image.getIconWidth();
        final int originalHeight = image.getIconHeight();

        final double aspectRatio = originalWidth / originalHeight;

        int newWidth = (int) ((panelHeight * IMAGE_PROPORTION) * aspectRatio);
        int newHeight = (int) (panelHeight * IMAGE_PROPORTION);

        if (newWidth > panelWidth) {
            newWidth = (int) panelWidth;
            newHeight = (int) (panelWidth / aspectRatio);
        }

        newWidth = Math.max(newWidth, 1);
        newHeight = Math.max(newHeight, 1);

        return ImageUtils.scaleImageIcon(image, newWidth, newHeight);
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        backButton.ifPresent(this::add);
    }

    @Override
    public void updateComponentsSize() {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
            this.originalImage
                    .ifPresent(image -> backButton.ifPresent(button -> button.setIcon(this.getImageResized(image))));
        }
    }

    @Override
    public void setPreferredSize(final Dimension windowSize) {
        super.setPreferredSize(new Dimension((int) (windowSize.getWidth() * WIDTH_PROPORTION),
                (int) (windowSize.getHeight() * HEIGHT_PROPORTION)));
        this.updateComponentsSize();
    }

}
