package com.thelegendofbald.utils;

import java.awt.Color;
import java.util.Optional;

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
     * Returns a darker version of the specified {@link Color} by multiplying each RGB component
     * by the given darkness factor. The resulting color components are clamped to the range [0, 255].
     *
     * @param color the original color to darken; must not be {@code null}
     * @param factorOfDarkness the factor by which to decrease brightness; must be between 0.0 and 1.0 (exclusive)
     * @throws IllegalArgumentException if {@code color} is {@code null} or if {@code factorOfDarkness} is not in (0.0, 1.0]
     * @return a new {@link Color} object that is a darker version of the input color
     */
    public static Color getDarkenColor(final Color color, final double factorOfDarkness) {
        if (Optional.ofNullable(color).isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (factorOfDarkness < 0.0 || factorOfDarkness > 1.0) {
            throw new IllegalArgumentException("Factor of darkness must be between 0.0 and 1.0");
        }

        final int red = Math.max(0, Math.min(255, (int) (color.getRed() * factorOfDarkness)));
        final int green = Math.max(0, Math.min(255, (int) (color.getGreen() * factorOfDarkness)));
        final int blue = Math.max(0, Math.min(255, (int) (color.getBlue() * factorOfDarkness)));

        return new Color(red, green, blue, color.getAlpha());
    }

    /**
     * Reutns a brighter version of the specified {@link Color} by dividing each RGB component
     * by the given factor of brightness. The resulting color components are clamped to the range [0, 255].
     * 
     * @param color the original color to brighten; must not be {@code null}
     * @param factorOfBrightness the factor by which to increase brightness; must be between 0.0 and 1.0 (exclusive)
     * @throws IllegalArgumentException if {@code color} is {@code null} or if {@code factorOfBrightness} is not in (0.0, 1.0]
     * @return a new {@link Color} object that is a brighter version of the input color
     */
    public static Color getBrightenColor(final Color color, final double factorOfBrightness) {
        if (Optional.ofNullable(color).isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (factorOfBrightness < 0.0 || factorOfBrightness > 1.0) {
            throw new IllegalArgumentException("Factor of brightness must be between 0.0 and 1.0");
        }

        final int red = Math.max(0, Math.min(255, (int) (color.getRed() / factorOfBrightness)));
        final int green = Math.max(0, Math.min(255, (int) (color.getGreen() / factorOfBrightness)));
        final int blue = Math.max(0, Math.min(255, (int) (color.getBlue() / factorOfBrightness)));

        return new Color(red, green, blue, color.getAlpha());
    }

}
