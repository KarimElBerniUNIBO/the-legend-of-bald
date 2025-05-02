package com.thelegendofbald.utils;

import java.awt.Color;

public class ColorUtils {

    public static Color getDarkenColor(Color color, double factorOfDarkness) {
        int red = (int) (color.getRed() * factorOfDarkness);
        int green = (int) (color.getGreen() * factorOfDarkness);
        int blue = (int) (color.getBlue() * factorOfDarkness);

        return new Color(red, green, blue);
    }

}
