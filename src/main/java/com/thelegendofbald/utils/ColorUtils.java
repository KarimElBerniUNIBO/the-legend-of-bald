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
     * Returns a new {@link Color} that is a darkened version of the specified color.
     * The darkness is determined by multiplying each RGB component by the given factor.
     *
     * @param color the original {@link Color} to darken
     * @param factorOfDarkness the factor by which to darken the color (should be between 0.0 and 1.0)
     * @return a new {@link Color} with each RGB component scaled by the factorOfDarkness
     * @throws IllegalArgumentException if color is null or if the resulting RGB values are out of range
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

        return new Color(red, green, blue);
    }

    /**
     * Returns a brighter version of the specified {@link Color} by dividing each RGB component
     * by the given brightness factor. The resulting color components are clamped to the range [0, 255].
     *
     * @param color the original color to brighten; must not be {@code null}
     * @param factorOfBrightness the factor by which to increase brightness; must be between 0.0 and 1.0 (exclusive)
     * @return a new {@link Color} object that is a brighter version of the input color
     * @throws IllegalArgumentException if {@code color} is {@code null} or if {@code factorOfBrightness} is not in (0.0, 1.0]
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

        return new Color(red, green, blue);
    }

}
