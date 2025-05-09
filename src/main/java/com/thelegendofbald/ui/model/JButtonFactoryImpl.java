package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.ImageIcon;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.settingsmenu.model.KeybindingButton;

/**
 * Implementation of JButtonFactory.
 */
public final class JButtonFactoryImpl implements JButtonFactory {

        private static final Pair<Double, Double> DEFAULT_MOLTIPLICATOR = new Pair<>(1.0, 1.0);
        private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
        private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
        private static final String DEFAULT_FONT_NAME = Font.SANS_SERIF;
        private static final int DEFAULT_FONT_TYPE = Font.BOLD;

        @Override
        public RectangleButton createRectangleButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> bgColor,
                        Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType) {
                return new RectangleButton(text, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public RectangleButton createRectangleButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> bgColor,
                        Optional<Color> fgColor) {
                return new RectangleButton(icon, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public RoundedButton createRoundedButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion,
                        Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor,
                        Optional<Integer> fontType) {
                return new RoundedButton(text, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public RoundedButton createRoundedButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion,
                        Optional<Color> bgColor, Optional<Color> fgColor) {
                return new RoundedButton(icon, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public TrasparentBackgroundButton createTrasparentButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<String> fontName,
                        Optional<Color> fontColor, Optional<Integer> fontType) {
                return new TrasparentBackgroundButton(text, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), DEFAULT_BACKGROUND_COLOR,
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public TrasparentBackgroundButton createTrasparentButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> fgColor) {
                return new TrasparentBackgroundButton(icon, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), DEFAULT_BACKGROUND_COLOR,
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public KeybindingButton createKeybindingButton(String text, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion,
                        Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor,
                        Optional<Integer> fontType) {
                return new KeybindingButton(text, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public KeybindingButton createKeybindingButton(ImageIcon icon, Dimension parentSize, Optional<Pair<Double, Double>> moltiplicator, double arcProportion, 
                        Optional<Color> bgColor, Optional<Color> fgColor) {
                return new KeybindingButton(icon, parentSize, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

}
