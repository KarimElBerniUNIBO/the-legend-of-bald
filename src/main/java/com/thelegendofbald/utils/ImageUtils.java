package com.thelegendofbald.utils;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Utility class for image-related operations.
 * <p>
 * This class provides static methods to manipulate and scale images.
 * It cannot be instantiated.
 * </p>
 */
public final class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Scales the given {@link ImageIcon} to the specified width and height using smooth scaling.
     *
     * @param icon   the {@code ImageIcon} to be scaled
     * @param width  the desired width of the scaled icon
     * @param height the desired height of the scaled icon
     * @return a new {@code ImageIcon} scaled to the specified dimensions
     */
    public static ImageIcon scaleImageIcon(final ImageIcon icon, final int width, final int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

}
