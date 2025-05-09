package com.thelegendofbald.ui.api;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.ImageIcon;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.model.RectangleButton;
import com.thelegendofbald.ui.model.RoundedButton;
import com.thelegendofbald.ui.model.TrasparentBackgroundButton;
import com.thelegendofbald.ui.settingsmenu.model.KeybindingButton;

/**
 * Factory for creating template of buttons for User Interface.
 */
public interface JButtonFactory {

    RectangleButton createRectangleButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);
    RectangleButton createRectangleButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> bgColor, Optional<Color> fgColor);

    RoundedButton createRoundedButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion, Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);
    RoundedButton createRoundedButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion, Optional<Color> bgColor, Optional<Color> fgColor);

    TrasparentBackgroundButton createTrasparentButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);
    TrasparentBackgroundButton createTrasparentButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> fgColor);

    KeybindingButton createKeybindingButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion, Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);
    KeybindingButton createKeybindingButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion, Optional<Color> bgColor, Optional<Color> fgColor);


}
