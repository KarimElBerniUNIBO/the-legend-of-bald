package com.thelegendofbald.ui.api;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Factory for creating template of buttons for User Interface.
 */
public interface JButtonFactory {

    JButton createRectangleButton(String text, Dimension parentSize, Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);
    JButton createRectangleButton(ImageIcon icon, Dimension parentSize, Optional<Color> bgColor, Optional<Color> fgColor);

    JButton createRoundedButton(String text, Dimension parentSize, double arcProportion, Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);
    JButton createRoundedButton(ImageIcon icon, Dimension parentSize, double arcProportion, Optional<Color> bgColor, Optional<Color> fgColor);

    JButton createTrasparentButton(String text, Dimension parentSize, Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);
    JButton createTrasparentButton(ImageIcon icon, Dimension parentSize, Optional<Color> fgColor);

}
