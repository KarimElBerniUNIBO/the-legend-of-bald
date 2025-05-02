package com.thelegendofbald.utils;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtils {

    public static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

}
