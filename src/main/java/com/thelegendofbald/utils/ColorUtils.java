package com.thelegendofbald.utils;

import java.awt.Color;

/**
 * Utility class for color manipulation operations.
 * <p>
 * This class provides static methods to perform operations on {@link Color} objects,
 * such as darkening a color by a specified factor.
 * </p>
 */
public final class ColorUtils {

    private ColorUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Returns a new {@link Color} that is a darkened version of the specified color.
     * The darkness is determined by multiplying each RGB component by the given factor.
     *
     * @param color the original {@link Color} to darken
     * @param factorOfDarkness the factor by which to darken the color (should be between 0.0 and 1.0)
     * @return a new {@link Color} with each RGB component scaled by the factorOfDarkness
     * @throws IllegalArgumentException if color is null or if the resulting RGB values are out of range
     */
    public static Color getDarkenColor(final Color color, final double factorOfDarkness) {
        if (factorOfDarkness < 0.0 || factorOfDarkness > 1.0) {
            throw new IllegalArgumentException("Factor of darkness must be between 0.0 and 1.0");
        }
    
        final int red = Math.max(0, Math.min(255, (int) (color.getRed() * factorOfDarkness)));
        final int green = Math.max(0, Math.min(255, (int) (color.getGreen() * factorOfDarkness)));
        final int blue = Math.max(0, Math.min(255, (int) (color.getBlue() * factorOfDarkness)));
    
        return new Color(red, green, blue);
    }

}
