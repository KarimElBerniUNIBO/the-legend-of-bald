package com.thelegendofbald.ui.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.thelegendofbald.ui.api.JButtonFactory;

/**
 * Implementation of JButtonFactory.
 */
public final class JButtonFactoryImpl implements JButtonFactory {

        private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
        private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
        private static final String DEFAULT_FONT_NAME = Font.SANS_SERIF;
        private static final int DEFAULT_FONT_TYPE = Font.BOLD;

        @Override
        public JButton createRectangleButton(String text, Dimension parentSize, Optional<Color> bgColor,
                        Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType) {
                return new RectangleButton(text, parentSize, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public JButton createRectangleButton(ImageIcon icon, Dimension parentSize, Optional<Color> bgColor,
                        Optional<Color> fgColor) {
                return new RectangleButton(icon, parentSize, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public JButton createRoundedButton(String text, Dimension parentSize, double arcProportion,
                        Optional<Color> bgColor, Optional<String> fontName, Optional<Color> fontColor,
                        Optional<Integer> fontType) {
                return new RoundedButton(text, parentSize, arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public JButton createRoundedButton(ImageIcon icon, Dimension parentSize, double arcProportion,
                        Optional<Color> bgColor, Optional<Color> fgColor) {
                return new RoundedButton(icon, parentSize, arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public JButton createTrasparentButton(String text, Dimension parentSize, Optional<String> fontName,
                        Optional<Color> fontColor, Optional<Integer> fontType) {
                return new TrasparentBackgroundButton(text, parentSize, DEFAULT_BACKGROUND_COLOR,
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public JButton createTrasparentButton(ImageIcon icon, Dimension parentSize, Optional<Color> fgColor) {
                return new TrasparentBackgroundButton(icon, parentSize, DEFAULT_BACKGROUND_COLOR, fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

}
